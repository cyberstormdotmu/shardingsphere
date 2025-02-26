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

package org.apache.shardingsphere.dbdiscovery.spi;

import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithm;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

/**
 * Database discovery type.
 */
public interface DatabaseDiscoveryType extends ShardingSphereAlgorithm {
    
    /**
     * Check database discovery configuration.
     *
     * @param schemaName schema name
     * @param dataSource data source
     * @throws SQLException SQL exception
     */
    void checkDatabaseDiscoveryConfiguration(String schemaName, DataSource dataSource) throws SQLException;
    
    /**
     * Update primary data source.
     *
     * @param schemaName schema name
     * @param dataSourceMap data source map
     * @param disabledDataSourceNames disabled data source names
     * @param groupName group name
     */
    void updatePrimaryDataSource(String schemaName, Map<String, DataSource> dataSourceMap, Collection<String> disabledDataSourceNames, String groupName);
    
    /**
     * Update member state.
     *
     * @param schemaName schema name
     * @param dataSourceMap data source map
     * @param disabledDataSourceNames disabled data source names
     */
    void updateMemberState(String schemaName, Map<String, DataSource> dataSourceMap, Collection<String> disabledDataSourceNames);
    
    /**
     * Get primary data source.
     *
     * @return primary data source
     */
    String getPrimaryDataSource();
}
