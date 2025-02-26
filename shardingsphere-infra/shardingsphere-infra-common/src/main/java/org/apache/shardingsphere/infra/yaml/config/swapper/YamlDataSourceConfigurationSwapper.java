/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.infra.yaml.config.swapper;

import com.google.common.base.Preconditions;
import org.apache.shardingsphere.infra.config.datasource.DataSourceConfiguration;
import org.apache.shardingsphere.infra.config.datasource.pool.creator.DataSourcePoolCreatorUtil;
import org.apache.shardingsphere.infra.yaml.config.pojo.YamlRootConfiguration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * YAML data source configuration swapper.
 */
public final class YamlDataSourceConfigurationSwapper {
    
    private static final String DATA_SOURCE_CLASS_NAME_KEY = "dataSourceClassName";
    
    /**
     * Swap to data sources from YAML data sources.
     *
     * @param yamlDataSources YAML data sources map
     * @return data sources
     */
    public Map<String, DataSource> swapToDataSources(final Map<String, Map<String, Object>> yamlDataSources) {
        return DataSourcePoolCreatorUtil.getDataSourceMap(yamlDataSources.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> swapToDataSourceConfiguration(entry.getValue()))));
    }
    
    /**
     * Get data source configurations.
     *
     * @param yamlRootConfig yaml root configuration
     * @return data source name to data source configuration map
     */
    public Map<String, DataSourceConfiguration> getDataSourceConfigurations(final YamlRootConfiguration yamlRootConfig) {
        Map<String, Map<String, Object>> yamlDataSourceConfigs = yamlRootConfig.getDataSources();
        Map<String, DataSourceConfiguration> result = new LinkedHashMap<>(yamlDataSourceConfigs.size());
        yamlDataSourceConfigs.forEach((key, value) -> result.put(key, swapToDataSourceConfiguration(value)));
        return result;
    }
    
    /**
     * Swap to data source configuration.
     * 
     * @param yamlConfig YAML configurations
     * @return data source configuration
     */
    @SuppressWarnings("rawtypes")
    public DataSourceConfiguration swapToDataSourceConfiguration(final Map<String, Object> yamlConfig) {
        Preconditions.checkState(yamlConfig.containsKey(DATA_SOURCE_CLASS_NAME_KEY), "%s can not be null.", DATA_SOURCE_CLASS_NAME_KEY);
        Map<String, Object> newDataSourceMap = new HashMap<>(yamlConfig);
        newDataSourceMap.remove(DATA_SOURCE_CLASS_NAME_KEY);
        DataSourceConfiguration result = new DataSourceConfiguration(yamlConfig.get(DATA_SOURCE_CLASS_NAME_KEY).toString());
        if (null != newDataSourceMap.get(DataSourceConfiguration.CUSTOM_POOL_PROPS_KEY)) {
            result.getCustomPoolProps().putAll((Map) newDataSourceMap.get(DataSourceConfiguration.CUSTOM_POOL_PROPS_KEY));
            newDataSourceMap.remove(DataSourceConfiguration.CUSTOM_POOL_PROPS_KEY);
        }
        result.getProps().putAll(newDataSourceMap);
        return result;
    }
    
    /**
     * Swap to map from data source configuration.
     * 
     * @param dataSourceConfig data source configuration
     * @return data source map
     */
    public Map<String, Object> swapToMap(final DataSourceConfiguration dataSourceConfig) {
        Map<String, Object> result = new HashMap<>(dataSourceConfig.getProps());
        if (!dataSourceConfig.getCustomPoolProps().isEmpty()) {
            result.put(DataSourceConfiguration.CUSTOM_POOL_PROPS_KEY, dataSourceConfig.getCustomPoolProps());
        }
        result.put(DATA_SOURCE_CLASS_NAME_KEY, dataSourceConfig.getDataSourceClassName());
        return result;
    }
}
