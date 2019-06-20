/*
    Xpress Insight User Admin Cli
    ============================
    UpdateUserOperationResult.java
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

import com.fico.xpress.vos.UserInfoExtended;

import java.io.PrintStream;

public class UpdateUserOperationResult implements OperationResult{

    private UserInfoExtended userInfoExtended;

    public UpdateUserOperationResult(UserInfoExtended userInfoExtended) {
        this.userInfoExtended = userInfoExtended;
    }

    /**
     * Utility method that prints user updated successfully.
     * @param out
     */
    @Override
    public void print(PrintStream out) {
        out.println(userInfoExtended.getUsername() + " Successfully updated.");
    }

    /**
     * Utility method to set the userInfoExtended object.
     * @param userInfoExtended
     */
    public void setUserInfoExtended(UserInfoExtended userInfoExtended) {
        this.userInfoExtended = userInfoExtended;
    }

    /**
     * Utility method to get the userInfoExtended object.
     * @return
     */
    public UserInfoExtended getUserInfoExtended() {
        return userInfoExtended;
    }
}
