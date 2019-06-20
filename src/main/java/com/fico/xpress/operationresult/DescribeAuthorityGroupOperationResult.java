/*
    Xpress Insight User Admin Cli
    ============================
    DescribeAuthorityGroupOperationResult.java
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

import com.fico.xpress.vos.AuthorityGroupExtended;
import com.fico.xpress.vos.AuthorityListItem;

import java.io.PrintStream;
import java.util.Collection;
import java.util.stream.Collectors;

public class DescribeAuthorityGroupOperationResult implements OperationResult {

    private AuthorityGroupExtended authorityGroupExtended;

    public DescribeAuthorityGroupOperationResult(AuthorityGroupExtended authorityGroupExtended) {
        this.authorityGroupExtended = authorityGroupExtended;
    }

    public AuthorityGroupExtended getAuthorityGroupExtended() {
        return authorityGroupExtended;
    }

    /**
     * Utility method to print the Authority Group for the describe operation.
     * @param out
     */
    public void print(PrintStream out) {

        printAuthorityGroupInfo(out, "Authority Group Name", authorityGroupExtended.getName());
        printAuthorityGroupInfo(out, "Authority Group Description", authorityGroupExtended.getDescription());
        printAuthorityGroupInfo(out, "Authorities", getAuthorities(authorityGroupExtended.getAuthorities()));

    }

    private void printAuthorityGroupInfo(PrintStream out, String propertyName, String propertyValue) {
        String format = "%-30s:%s";
        out.format(format, propertyName, propertyValue);
        out.println();
    }

    /**
     * Utility method to return the list of authorities for a authority group.
     * @param authorityList
     * @return
     */
    public String getAuthorities(Collection<AuthorityListItem> authorityList) {
        String authorities = "";
        if (authorityList != null && authorityList.size() > 0) {
            authorities = authorityList.stream()
                    .filter(AuthorityListItem::getSelected)
                    .map(AuthorityListItem::getDisplayName)
                    .collect(Collectors.joining(", "));
        }
        return authorities;
    }
}
