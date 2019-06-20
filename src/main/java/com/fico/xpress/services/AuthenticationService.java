/*
    Xpress Insight User Admin Cli
    ============================
    AuthenticationService.java
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
package com.fico.xpress.services;

import com.fico.xpress.vos.InsightApplicationConfig;
import org.springframework.http.HttpStatus;

public interface AuthenticationService {
    /**
     * Method used to log in to an insight server and to fetch the token.
     * @param insightApplicationConfig
     * @return
     */
    String getToken(InsightApplicationConfig insightApplicationConfig);

    /**
     * Method used to log out from an insight server by passing back the token.
     * @param insightApplicationConfig
     * @param token
     * @return
     */
    HttpStatus invalidateToken(InsightApplicationConfig insightApplicationConfig, String token);
}
