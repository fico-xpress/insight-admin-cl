/*
    Xpress Insight User Admin Cli
    ============================
    ListUsersOperationResult.java
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

import java.io.PrintStream;
import java.util.List;

public class ListUsersOperationResult implements OperationResult {

    private List<String> userNames;

    /**
     * Utility method to print the list of users for the list operation.
     * @param out
     */
    @Override
    public void print(PrintStream out) {
        userNames.stream().sorted((str1, str2) -> str1.compareToIgnoreCase(str2)).forEach(userNames -> out.println(userNames));
    }

    /**
     * Method that returns usernames.
     * @return
     */
    public List<String> getUserNames() {
        return userNames;
    }

    /**
     * Method to set usernames.
     * @param userNames
     */
    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }
}
