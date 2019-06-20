/*
    Xpress Insight User Admin Cli
    ============================
    DescribeUserCmdLineTest.java
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
import java.util.ArrayList;
import java.util.List;

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.WINDOWS_COMMAND;
import static com.fico.xpress.insight.clisystemtest.UtilsCLITest.URL;
import static org.junit.Assert.assertTrue;

public class DescribeUserCmdLineTest {

    @Test
    public void testDescribeUserValidation() throws IOException {
        String[] testInvalidCredentialsCommand = {WINDOWS_COMMAND, "user", "describe", "-u", "admin", "-p", "admin123", "-tu", "admin","-url", URL};
        String expectedResult = "Tableau Username";
        assertTrue(UtilsCLITest.consoleCommandExecuter(testInvalidCredentialsCommand).contains(expectedResult));

    }

    @Test
    public void testDescribeOrderValidation() throws IOException {
        String[] testInvalidCredentialsCommand = {WINDOWS_COMMAND, "describe", "user", "-u", "admin", "-p", "admin123", "-tu", "admin","-url", URL};
        String expectedResult = "Invalid parameter: found 'describe', expected one of [authgroup|user]. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(testInvalidCredentialsCommand).contains(expectedResult));

    }

    @Test
    public void testDescribeInactiveUserValidation() throws IOException {
        String[] testInvalidCredentialsCommand = {WINDOWS_COMMAND, "user", "describe", "-u", "admin", "-p", "admin123", "-t u", "admin","-url", URL};
        String expectedResult = "Unrecognized option: -t u. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(testInvalidCredentialsCommand).contains(expectedResult));

    }

    @Test
    public void testDescribeValidUserWithPartialNameValidation() throws IOException {
        String[] testInvalidCredentialsCommand = {WINDOWS_COMMAND, "user", "describe", "-u", "admin", "-p", "admin123", "-tu", "admi n","-url", URL};
        String expectedResult = "Unable to retrieve the user information. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(testInvalidCredentialsCommand).contains(expectedResult));

    }

    @Test
    public void testDescribeUserAttributes() throws IOException {
        String[] testDescribeUserAttributes = {WINDOWS_COMMAND, "user", "describe", "-u", "admin", "-p", "admin123", "-tu", "admin","-url", URL};
        StringBuilder userDetails = new StringBuilder();
        userDetails.append("User Name");
        userDetails.append("First Name");
        userDetails.append("Last Name");
        userDetails.append("Email");
        userDetails.append("Tableau Enabled");
        userDetails.append("Tableau Username");
        userDetails.append("Enabled");
        userDetails.append("Locked");
        userDetails.append("Local Account");
        userDetails.append("Authority Groups");
        userDetails.append("Apps");

        StringBuilder tableauContents = new StringBuilder(UtilsCLITest.consoleCommandExecuter(testDescribeUserAttributes));
        for (String str : tableauContents.toString().split("\\n")) {
            if (str.contains(":")) {
                assertTrue(userDetails.toString().contains(str.split(":")[0].trim()));
            }
        }
    }

    @Test
    public void testDescribeValidUserWithOtherCommandsValidation() throws IOException {
        String[] testInvalidCredentialsCommand = {WINDOWS_COMMAND, "user", "describe", "-u", "admin", "-p", "admin123", "-tu", "admin", "-hs","-url", URL};
        String expectedResult = "usage: insightadmincli [authgroup|user] [add|describe|list|update]";
        assertTrue(UtilsCLITest.consoleCommandExecuter(testInvalidCredentialsCommand).contains(expectedResult));

    }

    @Test
    public void testDescribeUserOrder() throws IOException {
        String[] testDescribeUserAttributes = {WINDOWS_COMMAND, "user", "describe", "-u", "admin", "-p", "admin123", "-tu", "admin","-url", URL};
        List<String> userDetailsOrder = new ArrayList<>();
        userDetailsOrder.add("User Name");
        userDetailsOrder.add("First Name");
        userDetailsOrder.add("Last Name");
        userDetailsOrder.add("Email");
        userDetailsOrder.add("Tableau Enabled");
        userDetailsOrder.add("Tableau Username");
        userDetailsOrder.add("Enabled");
        userDetailsOrder.add("Locked");
        userDetailsOrder.add("Local Account");
        userDetailsOrder.add("Authority Groups");
        userDetailsOrder.add("Apps");
        StringBuilder tableauContents = new StringBuilder(UtilsCLITest.consoleCommandExecuter(testDescribeUserAttributes));
        List<String> fetchTableOrder = new ArrayList<>();
        for (String str : tableauContents.toString().split("\\n")) {
            if (str.contains(":")) {
                fetchTableOrder.add(str.split(":")[0].trim());
            }
        }
        assertTrue(userDetailsOrder.equals(fetchTableOrder));
    }


}
