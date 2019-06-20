/*
    Xpress Insight User Admin Cli
    ============================
    DescribeUserOperationResult.java
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

package com.fico.xpress.operationresult;

import com.fico.xpress.vos.AuthorityGroup;
import com.fico.xpress.vos.Project;
import com.fico.xpress.vos.UserInfoExtended;

import java.io.PrintStream;
import java.util.Collection;
import java.util.stream.Collectors;

public class DescribeUserOperationResult implements OperationResult {

    private UserInfoExtended userInfoExtended;

    /**
     * Utility method to print the User for the describe operation.
     * @param out
     */
    @Override
    public void print(PrintStream out) {

        printUserInfo(out, "User Name" , userInfoExtended.getUsername());
        printUserInfo(out, "First Name", userInfoExtended.getFirstName());
        printUserInfo(out, "Last Name", userInfoExtended.getLastName());
        printUserInfo(out, "Email", userInfoExtended.getEmail());
        printUserInfo(out, "Tableau Enabled", String.valueOf(userInfoExtended.getTableauEnabled()));
        printUserInfo(out, "Tableau Username", userInfoExtended.getTableauUsername());
        printUserInfo(out, "Enabled", String.valueOf(userInfoExtended.isEnabled()));
        printUserInfo(out, "Locked", String.valueOf(userInfoExtended.isLocked()));
        printUserInfo(out, "Local Account", String.valueOf(userInfoExtended.isLocalAccount()));
        printUserInfo(out, "Authority Groups", getAuthorityGroups(userInfoExtended.getAuthorityGroups()));
        printUserInfo(out, "Apps", getProjects(userInfoExtended.getProjects()));

    }

    private void printUserInfo(PrintStream out, String propertyName, String propertyValue) {
        String format = "%-20s:%s";
        out.format(format, propertyName, propertyValue);
        out.println();
    }

    /**
     * Utility method that returns the userInfoExtended object.
     * @return
     */
    public UserInfoExtended getUserInfoExtended() {
        return userInfoExtended;
    }

    /**
     * Utility method that sets the userInfoExtended object.
     * @param userInfoExtended
     */
    public void setUserInfoExtended(UserInfoExtended userInfoExtended) {
        this.userInfoExtended = userInfoExtended;
    }

    /**
     * Utility method to that returns the authority groups for a specific user.
     * @param authorityGroupList
     * @return authorityGroups
     */
    public String getAuthorityGroups(Collection<AuthorityGroup> authorityGroupList){
        String authorityGroups = "";
        if(authorityGroupList != null && authorityGroupList.size() > 0) {
            authorityGroups = authorityGroupList.stream()
                    .filter(AuthorityGroup::getSelected)
                    .map(AuthorityGroup::getName)
                    .collect(Collectors.joining(", "));
        }
        return authorityGroups;
    }

    /**
     * Utility method that returns the list of apps for a specific user.
     * @param projectList
     * @return projects
     */
    public String getProjects(Collection<Project> projectList){
        String projects = "";
        if(projectList != null && projectList.size() > 0) {
            projects = projectList.stream()
                    .filter(Project::getSelected)
                    .map(Project::getName)
                    .collect(Collectors.joining(", "));
        }
        return projects;
    }
}
