/*
 * ============LICENSE_START=======================================================================================
 * Copyright (c) 2019 AT&T Intellectual Property.
 * ===================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================================================
 *
 */

package org.onap.optf.cmso.liquibase;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/*
 * Make sure following exceptions before build:
 * Build errors
 * java.net.ConnectException: Connection refused: connect (DB Connection)
 * Make sure SQL files syntax are correct
 * Always place new SQL files in src\main\resources\dbchangelog folder
 * Please dont delete previous scripts
 * Make sure to follow naming convention for SQL file (1806-cmso-v2-schema.sql)
 * Please make sure to give author name and id for new SQL scripts (eg: changeSet author="bg4702"
 * id="unique id")
 ** IMPORTANT: Always do testing in local before pushing changes to Env's**
 */

@Component
@Configuration
public class LiquibaseData {

    @Value("${changeLogFile}")
    private String changeLogFile;

    /**
     * Liquibase.
     *
     * @return the spring liquibase
     */
    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog("classpath:" + changeLogFile);
        return liquibase;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

}
