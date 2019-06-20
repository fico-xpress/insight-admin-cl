/*
    Xpress Insight User Admin Cli
    ============================
    UpdateAuthorityGroupOperationResult.java
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

import java.io.PrintStream;

public class UpdateAuthorityGroupOperationResult implements OperationResult{
    private AuthorityGroupExtended authorityGroupExtended;

    public UpdateAuthorityGroupOperationResult(AuthorityGroupExtended authorityGroupExtended){
        this.authorityGroupExtended = authorityGroupExtended;
    }

    /**
     * Utility method that print authority group added successfully.
     * @param out
     */
    @Override
    public void print(PrintStream out) {
        out.println(authorityGroupExtended.getName() + " Successfully updated.");
    }


    /**
     * Utility method to get the authorityGroupExtended object.
     * @return
     */
    public AuthorityGroupExtended getAuthorityGroupExtended() {
        return authorityGroupExtended;
    }

}
