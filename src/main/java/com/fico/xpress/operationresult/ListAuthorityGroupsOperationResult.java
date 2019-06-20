/*
    Xpress Insight User Admin Cli
    ============================
    ListAuthorityGroupsOperationResult.java
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

import java.io.PrintStream;
import java.util.List;

public class ListAuthorityGroupsOperationResult implements OperationResult{

    private List<AuthorityGroup> authorityGroupList;

    public ListAuthorityGroupsOperationResult(List<AuthorityGroup> authorityGroupList) {
        this.authorityGroupList = authorityGroupList;
    }

    /**
     * Utility method to print the list of Authority Groups for the list operation.
     * @param out
     */
    @Override
    public void print(PrintStream out) {
        authorityGroupList.stream().forEach(authorityGroup -> out.println(authorityGroup.getName()));
    }

    /**
     * Utility method that returns the list of Authority Groups.
     * @return
     */
    public List<AuthorityGroup> getAuthorityGroups() {
        return authorityGroupList;
    }

    /**
     * Utility method that sets the list of Authority Groups.
     * @param authorityGroupsList
     */
    public void setAuthorityGroups(List<AuthorityGroup> authorityGroupsList) {
        this.authorityGroupList = authorityGroupsList;
    }

}
