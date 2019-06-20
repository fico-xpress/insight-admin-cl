/*
    Xpress Insight User Admin Cli
    ============================
    ListAuthorityGroupsCmdLineTest.java
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
import java.util.stream.Collectors;

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.WINDOWS_COMMAND;
import static com.fico.xpress.insight.clisystemtest.UtilsCLITest.URL;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ListAuthorityGroupsCmdLineTest {
   
    @Test
    public void verifyListAuthorityGroupsCmdLineTest() throws IOException{
        String[] VerifyListAuthorityGroupsCmdLineTest = {WINDOWS_COMMAND, "authgroup", "list", "-u", "admin", "-p", "admin123","-url", URL};
        List resultsVerifyListAuthorityGroupsCmdLineTest=Arrays.asList(UtilsCLITest.consoleCommandExecuter(VerifyListAuthorityGroupsCmdLineTest));
        assertNotNull(resultsVerifyListAuthorityGroupsCmdLineTest);
        assertTrue(resultsVerifyListAuthorityGroupsCmdLineTest.stream().sorted().collect(Collectors.toList()).equals(resultsVerifyListAuthorityGroupsCmdLineTest));

    }
    @Test
    public void verifyListAuthorityGroupsCmdLineTestWithMissingArgs() throws IOException{
        String[] VerifyListAuthorityGroupsCmdLineTest = {WINDOWS_COMMAND, "authgroup", "", "-u", "admin", "-p", "admin123","-url", URL};
        String resultsVerifyListAuthorityGroupsCmdLineTest  = UtilsCLITest.consoleCommandExecuter(VerifyListAuthorityGroupsCmdLineTest);
        String expectedResult="Insufficient command-line parameters, expected: [add|describe|list|update]. Process finished with exit code 1";
        assertTrue(resultsVerifyListAuthorityGroupsCmdLineTest.contains(expectedResult));

    }
    @Test
    public void verifyListAuthorityGroupsCmdLineTestWithWrongArgs() throws IOException{
        String[] verifyListAuthorityGroupsCmdLineTestWithWrongArgs = {WINDOWS_COMMAND, "authgroup", "LIST", "-u", "admin", "-p", "admin123","-url", URL};
        String resultsVerifyListAuthorityGroupsCmdLineTestWithWrongArgs  = UtilsCLITest.consoleCommandExecuter(verifyListAuthorityGroupsCmdLineTestWithWrongArgs);
        String expectedResult="Invalid parameter: found 'LIST', expected one of [add|describe|list|update]. Process finished with exit code 1";
        assertTrue(resultsVerifyListAuthorityGroupsCmdLineTestWithWrongArgs.contains(expectedResult));

    }
    @Test
    public void verifyListAuthorityGroupsCmdLineTestWithWrongAuthGrpName() throws IOException{
        String[] verifyListAuthorityGroupsCmdLineTestWithWrongAuthGrpName={WINDOWS_COMMAND, "authoritygroup", "LIST", "-u", "admin", "-p", "admin123","-url", URL};
        String resultsVerifyListAuthorityGroupsCmdLineTestWithWrongAuthGrpName  = UtilsCLITest.consoleCommandExecuter(verifyListAuthorityGroupsCmdLineTestWithWrongAuthGrpName);
        String expectedResult = "Invalid parameter: found 'authoritygroup', expected one of [authgroup|user]. Process finished with exit code 1";
        assertTrue(resultsVerifyListAuthorityGroupsCmdLineTestWithWrongAuthGrpName.contains(expectedResult));

    }
    @Test
    public void verifyListAuthorityGroupsCmdLineTestWithCapsAuthGrpName() throws IOException{
        String[] verifyListAuthorityGroupsCmdLineTestWithCapsAuthGrpName = {WINDOWS_COMMAND, "AUTHGROUP", "LIST", "-u", "admin", "-p", "admin123","-url", URL};
        String resultsVerifyListAuthorityGroupsCmdLineTestWithCapsAuthGrpName  = UtilsCLITest.consoleCommandExecuter(verifyListAuthorityGroupsCmdLineTestWithCapsAuthGrpName);
        String expectedResult = "Invalid parameter: found 'AUTHGROUP', expected one of [authgroup|user]. Process finished with exit code 1";
        assertTrue(resultsVerifyListAuthorityGroupsCmdLineTestWithCapsAuthGrpName.contains(expectedResult));

    }
    
}
