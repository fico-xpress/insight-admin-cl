/*
    Xpress Insight User Admin Cli
    ============================
    InsightAdminCliConfig.java
    ```````````````````````
    Command line tool to perform user management operations for Xpress Insight 4.x
    (c) Copyright 2019 Fair Isaac Corporation

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.fico.xpress.config;

import com.fico.xpress.commandline.InsightAdminCli;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InsightAdminCliConfig {

    /**  Added to autowire RestTemplate in service layer.  **/
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    public static InsightAdminCli initializeInsightAdminCliApplicationContext() {
        ApplicationContext context = new AnnotationConfigApplicationContext(InsightAdminCli.class);
        InsightAdminCli cmd = context.getBean(InsightAdminCli.class);
        return cmd;
    }

}
