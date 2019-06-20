/*
    Xpress Insight User Admin Cli
    ============================
    ListUserCmdLineTest.java
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
package com.fico.xpress.insight.clisystemtest;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.WINDOWS_COMMAND;
import static com.fico.xpress.insight.clisystemtest.UtilsCLITest.URL;
import static org.junit.Assert.assertTrue;

public class ListUserCmdLineTest {


    @Test
    public void testInvalidCredentials() throws IOException {
        String[] testInvalidCredentialsCommand = {WINDOWS_COMMAND, "user", "list", "-u", "admin", "-p", "admin1234", "-url", URL};
        String expectedResult = "Invalid credentials. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(testInvalidCredentialsCommand).contains(expectedResult));

    }

    @Test
    public void testListUserCLI() throws IOException {

        String[] testListUserCLICommand = {WINDOWS_COMMAND, "user", "list", "-u", "admin", "-p", "admin123", "-url", URL};
        List<String> listUserName = Arrays.asList(UtilsCLITest.consoleCommandExecuter(testListUserCLICommand).split("\\n"));
        String expectedResult = "admin";
        // assert stdout's content value
        assertTrue(listUserName.contains(expectedResult));
    }

    @Test
    public void testUserNameValidation() throws IOException {

        String[] testUserNameValidation = {WINDOWS_COMMAND, "user", "list", "u", "", "-url", URL};
        String expectedResult = "User Name is mandatory. Process finished with exit code 1";
        // assert stdout's content value
        assertTrue(UtilsCLITest.consoleCommandExecuter(testUserNameValidation).contains(expectedResult));
    }

    @Test
    public void testPasswordValidation() throws IOException {
        String[] testInvalidOrderInputValidation = {WINDOWS_COMMAND, "user", "list", "-u", "admin", "-p", "admin123"};
        assertTrue(UtilsCLITest.consoleCommandExecuter(testInvalidOrderInputValidation).contains("USER LIST OPERATION END"));

    }

    @Test
    public void testHelpUserValidation() throws IOException {
        String[] testHelpUserValidation = {WINDOWS_COMMAND, "user", "list", "-h", "-url", URL};
        String expectedResult = "usage: insightadmincli [authgroup|user] [add|describe|list|update]";
        // assert stdout's content value
        assertTrue(UtilsCLITest.consoleCommandExecuter(testHelpUserValidation).contains(expectedResult));

    }


    @Test
    public void testInvalidOrderInputValidation() throws IOException {
        String[] testInvalidOrderInputValidation = {WINDOWS_COMMAND, "user", "list", "-p", "admin", "-u", "admin123", "-url", URL};
        String expectedResult = "Invalid credentials. Process finished with exit code 1";
        // assert stdout's content value
        assertTrue(UtilsCLITest.consoleCommandExecuter(testInvalidOrderInputValidation).contains(expectedResult));

    }


    @Test
    public void testHelpWithValidUserNamePasswordValidation() throws IOException {
        String[] testHelpValidUserNamePasswordValidation = {WINDOWS_COMMAND, "user", "-h", "list", "-u", "admin", "-p", "admin123", "-url", URL};
        String expectedResult = "usage: insightadmincli [authgroup|user] [add|describe|list|update]";
        // assert stdout's content value
        assertTrue(UtilsCLITest.consoleCommandExecuter(testHelpValidUserNamePasswordValidation).contains(expectedResult));

    }


}
