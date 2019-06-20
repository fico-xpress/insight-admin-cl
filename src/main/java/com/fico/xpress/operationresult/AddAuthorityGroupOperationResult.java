/*
    Xpress Insight User Admin Cli
    ============================
    AddAuthorityGroupOperationResult.java
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

public class AddAuthorityGroupOperationResult implements OperationResult {

    private AuthorityGroupExtended authorityGroupExtended;
    public AddAuthorityGroupOperationResult(AuthorityGroupExtended authorityGroupExtended){
        this.authorityGroupExtended = authorityGroupExtended;
    }

    /**
     * Prints that Authority Group added successfully
     * @param out
     */
    @Override
    public void print(PrintStream out){
        out.println(authorityGroupExtended.getName() + " Successfully added.");
    }

    /**
     * Sets the authorityGroupExtended object
     * @param authorityGroupExtended
     */
    public void setAuthorityGroupExtended(AuthorityGroupExtended authorityGroupExtended) {
        this.authorityGroupExtended = authorityGroupExtended;
    }

    /**
     * Utility method to get the AuthorityGroupExtended object
     * @return
     */
    public AuthorityGroupExtended getAuthorityGroupExtended() {
        return authorityGroupExtended;
    }

}
