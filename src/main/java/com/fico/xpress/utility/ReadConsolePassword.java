/*
    Xpress Insight User Admin Cli
    ============================
    ReadConsolePassword.java
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

import java.io.Console;

public class ReadConsolePassword {

    public String readPassword() {
        Console console = System.console();
        String password = null;
        if (console != null) {
            password = new String(console.readPassword("Enter the password:"));
        }
        return password;
    }
}
