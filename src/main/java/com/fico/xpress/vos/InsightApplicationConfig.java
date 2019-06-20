/*
    Xpress Insight User Admin Cli
    ============================
    InsightApplicationConfig.java
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
package com.fico.xpress.vos;

import static com.fico.xpress.utility.InsightAdminUriConstants.DEFAULT_URL;
import static com.fico.xpress.utility.InsightAdminUriConstants.INSIGHTADMIN_URI;

public class InsightApplicationConfig {

    /**
     * Represents userName.
     */
    private String userName;


    /**
     * Represents password.
     */
    private String password;

    /**
     * Represents the url of the host.
     */
    private String hostUrl;

    public InsightApplicationConfig() {
    }

    public InsightApplicationConfig(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * Get the userName.
     * @return username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the userName.
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get the password.
     * @return password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the hostUrl.
     * @return hostUrl.
     */
    public String getHostUrl() {
        return hostUrl;
    }

    /**
     * Set the hostUrl.
     * @param hostUrl
     */
    public void setHostUrl(String hostUrl) {
        if (hostUrl == null || hostUrl.trim().isEmpty()) {
            this.hostUrl = DEFAULT_URL;
            return;
        }
        this.hostUrl = hostUrl;
    }

    /**
     * Get the url containing the resourceUrl.
     * @param resourceUrl
     * @return endPointUrl.
     */
    public String getEndPoint(String resourceUrl) {
        return getHostUrl() + INSIGHTADMIN_URI + resourceUrl;
    }
}
