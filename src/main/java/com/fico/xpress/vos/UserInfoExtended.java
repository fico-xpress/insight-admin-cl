/*
    Xpress Insight User Admin Cli
    ============================
    UserInfoExtended.java
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

import java.util.ArrayList;
import java.util.Collection;

import static com.fico.xpress.utility.InsightAdminCliConstants.EMAIL;
import static com.fico.xpress.utility.InsightAdminCliConstants.ENABLED;
import static com.fico.xpress.utility.InsightAdminCliConstants.FIRST_NAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.LAST_NAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.LOCAL_ACCOUNT_ENABLE;
import static com.fico.xpress.utility.InsightAdminCliConstants.LOCKED;
import static com.fico.xpress.utility.InsightAdminCliConstants.TABLEAU_ENABLED;
import static com.fico.xpress.utility.InsightAdminCliConstants.TABLEAU_USERNAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.TARGET_PASSWORD;
import static com.fico.xpress.utility.InsightAdminCliConstants.TARGET_USER_NAME;

public class UserInfoExtended extends UserInfo {

    /**
     * ID field.
     */
    private String id;

    /**
     * Collection of authorities.
     */
    protected Collection<AuthorityGroup> authorityGroups;


    /**
     * set the Id.
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * set the authority groups.
     * @param authorityGroups
     */
    public void setAuthorityGroups(Collection<AuthorityGroup> authorityGroups) {
        this.authorityGroups = authorityGroups;
    }

    /**
     * set the apps.
     * @param projects
     */
    public void setProjects(Collection<Project> projects) {
        this.projects = projects;
    }


    /**
     * Collection of authorities.
     */
    protected Collection<Project> projects;

    /**
     * Parameterised constructor
     * @param username
     * @param firstName
     * @param lastName
     * @param tableauEnabled
     * @param tableauUsername
     * @param email
     * @param enabled
     * @param locked
     * @param localAccount
     * @param password
     * @param id
     * @param authorityGroups
     * @param projects
     */
    public UserInfoExtended(String username,
                            String firstName,
                            String lastName,
                            boolean tableauEnabled,
                            String tableauUsername,
                            String email,
                            boolean enabled,
                            boolean locked,
                            boolean localAccount,
                            String password,
                            String id,
                            Collection<AuthorityGroup> authorityGroups,
                            Collection<Project> projects) {
        super(username, firstName, lastName, tableauEnabled, tableauUsername, email, enabled, locked, localAccount, password);
        this.id = id;
        this.authorityGroups = authorityGroups;
        this.projects = projects;
    }

    /**
     * Constructor that takes in command object and populates the userInfoExtended object.
     * @param command
     */
    public UserInfoExtended(Command command) {
        super(command.getStringArgumentValue(TARGET_USER_NAME),
            command.getStringArgumentValue(FIRST_NAME),
            command.getStringArgumentValue(LAST_NAME),
            command.getBooleanArgumentValue(TABLEAU_ENABLED),
            command.getStringArgumentValue(TABLEAU_USERNAME),
            command.getStringArgumentValue(EMAIL),
            command.getBooleanArgumentValue(ENABLED),
            command.getBooleanArgumentValue(LOCKED),
            command.getBooleanArgumentValue(LOCAL_ACCOUNT_ENABLE),
            command.getStringArgumentValue(TARGET_PASSWORD));
        this.id = null;
        this.authorityGroups = null;
        this.projects = null;
    }

    /**
     * Default constructor
     */
    public UserInfoExtended() {
        super();
        this.id = this.username;
        this.authorityGroups = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    /**
     * Get the id of the group.
     * @return id of the group.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the authority groups.
     * @return collection of authority groups.
     */
    public Collection<AuthorityGroup> getAuthorityGroups() {
        return authorityGroups;
    }

    /**
     * Add an authority group to the collection.
     * @param authorityGroup authority group to add.
     */
    public void addAuthorityGroup(AuthorityGroup authorityGroup) {
        this.authorityGroups.add(authorityGroup);
    }

    /**
     * Get the projects.
     * @return collection of projects.
     */
    public Collection<Project> getProjects() {
        return projects;
    }


    /**
     * Utility method to print the user details.
     * @return user details.
     */
    @Override
    public String toString() {
        return "username:" + username +
            "\nfirstName:" + firstName +
            "\nlastName:" + lastName +
            "\nemail:" + email +
            "\ntableauEnabled:" + tableauEnabled +
            "\ntableauUsername:"+ tableauUsername +
            "\nenabled:" + enabled +
            "\nlocked:" + locked +
            "\nlocalAccount:"+localAccount +
            "\n" + authorityGroups +
            "\n" + projects;
    }
}
