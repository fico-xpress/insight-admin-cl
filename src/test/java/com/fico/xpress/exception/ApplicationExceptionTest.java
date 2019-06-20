/*
    Xpress Insight User Admin Cli
    ============================
    ApplicationExceptionTest.java
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
package com.fico.xpress.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ApplicationExceptionTest {

    @Test
    public void testConstructor(){
        ApplicationException applicationException = new ApplicationException("message");
        assertEquals("message matches", "message", applicationException.getMessage());
    }
}
