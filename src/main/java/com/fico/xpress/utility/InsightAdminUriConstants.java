/*
    Xpress Insight User Admin Cli
    ============================
    InsightAdminCliUtil.java
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
package com.fico.xpress.utility;

public class InsightAdminUriConstants {

    public static final String DEFAULT_URL = "http://localhost:8860";
    public static final String INSIGHTADMIN_URI = "/insightadmin";
    public static final String LOGIN_URI = "/j_security_check";
    public static final String LIST_USER_URI = "/rest/user";
    public static final String LOGOUT_URI = "/rest/appres/logout";
    public static final String DESC_OR_UPDATE_USER_URI = "/rest/user/item/{username}";
    public static final String ADD_USER_URI = "/rest/user/new";
    public static final String LIST_AUTHGRP_URI = "/rest/authoritygroup";
    public static final String ADD_AUTHGRP_URI = "/rest/authoritygroup/new";
    public static final String DESC_OR_UPDATE_AUTHGRP_URI = "/rest/authoritygroup/item/{authgrpname}";
}
