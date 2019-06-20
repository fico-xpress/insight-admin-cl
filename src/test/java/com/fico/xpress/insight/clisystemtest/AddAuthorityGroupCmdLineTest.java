/*
    Xpress Insight User Admin Cli
    ============================
    AddAuthorityGroupCmdLineTest.java
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

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.WINDOWS_COMMAND;
import static com.fico.xpress.insight.clisystemtest.UtilsCLITest.URL;

import static org.junit.Assert.assertTrue;

public class AddAuthorityGroupCmdLineTest {
    @Test
    public void verifyAddAuthorityGroupCmdLineTest() throws IOException {
        Long newTime = new java.util.Date().getTime();
        String[] cmdVerifyListAuthorityGroup = (WINDOWS_COMMAND+" authgroup list -u admin  -p admin123 -url "+URL).split(" ");
        String listOFAuthgroupUser = UtilsCLITest.consoleCommandExecuter(cmdVerifyListAuthorityGroup);
        int sizeOfAuthGrp = listOFAuthgroupUser.length();
        String[] cmdToVerifyAddAuthorityGroup = {WINDOWS_COMMAND, "authgroup", "add", "-u", "admin", "-p", "admin123", "-an", "Test Auth grp" + newTime, "-ad", "\"This is for test\"", "-at", "DEVELOPER","-url", URL};
        String addedListMessage = UtilsCLITest.consoleCommandExecuter(cmdToVerifyAddAuthorityGroup);
        String listOFAuthgroupUserUpdated = UtilsCLITest.consoleCommandExecuter(cmdVerifyListAuthorityGroup);
        int sizeOfAuthGrpUpdated = listOFAuthgroupUserUpdated.length();
        assertTrue(sizeOfAuthGrpUpdated >= sizeOfAuthGrp);
        assertTrue(addedListMessage.contains("added"));


    }
    @Test
    public void verifyAddAuthorityGroupForExistingGroupCmdLineTest() throws IOException {
        String[] cmdVerifyListAuthorityGroup = (WINDOWS_COMMAND+" authgroup list -u admin  -p admin123 -url "+URL).split(" ");
        int sizeOfAuthGrp = UtilsCLITest.consoleCommandExecuter(cmdVerifyListAuthorityGroup).length();
        String[] cmdToVerifyAddAuthorityGroup = {WINDOWS_COMMAND, "authgroup", "add", "-u", "admin", "-p", "admin123", "-an", "Test Auth grp", "-ad", "\"This is for test\"", "-at", "DEVELOPER","-url", URL};
        String msgIfSameAuthGrpExits = UtilsCLITest.consoleCommandExecuter(cmdToVerifyAddAuthorityGroup);
        int sizeOfAuthGrpUpdated = UtilsCLITest.consoleCommandExecuter(cmdVerifyListAuthorityGroup).length();
        assertTrue(msgIfSameAuthGrpExits.contains("already exists"));
        assertTrue(sizeOfAuthGrpUpdated >= sizeOfAuthGrp);

    }

    @Test
    public void AddAuthorityGroupCmdValidationTest() throws IOException {
        String[] cmdToVerifyAddAuthorityGroup = {WINDOWS_COMMAND, "authority group", "add", "-u", "admin", "-p", "admin123", "-an", "Test Auth grp", "-ad", "\"This is for test\"", "-at", "DEVELOPE","-url", URL};
        String resultOfcmdToVerifyAddAuthorityGroup = UtilsCLITest.consoleCommandExecuter(cmdToVerifyAddAuthorityGroup);
        String expectedResult = "Invalid parameter: found 'authority group', expected one of [authgroup|user]. Process finished with exit code 1";
        assertTrue(resultOfcmdToVerifyAddAuthorityGroup.contains(expectedResult));
    }

    @Test
    public void AddAuthorityGroupNameCmdValidationTest() throws IOException {
        String[] cmdToVerifyAuthorityGroupNameCmdValidationTest = {WINDOWS_COMMAND, "authoritygroup", "add", "-u", "admin", "-p", "admin123", "-an", "", "-ad", "\"This is for test\"", "-at", "DEVELOPE","-url", URL};
        String resultOfcmdToVerifyAuthorityGroupName = UtilsCLITest.consoleCommandExecuter(cmdToVerifyAuthorityGroupNameCmdValidationTest);
        String expectedResult = "Missing argument for option: an. Process finished with exit code 1";
        assertTrue(resultOfcmdToVerifyAuthorityGroupName.contains(expectedResult));
    }

    @Test
    public void AddAuthorityGroupAuthoritiesValidationTest() throws IOException {
        String[] cmdToVerifyAddAuthorityGroupAuthoritiesValidationTest = {WINDOWS_COMMAND, "authgroup", "add", "-u", "admin", "-p", "admin123", "-an", "Test AG1", "-ad", "This is for test", "-at", "DEVELOPE","-url", URL};
        String resultOfCmdToVerifyAddAuthorityGroupAuthoritiesValidationTest = UtilsCLITest.consoleCommandExecuter(cmdToVerifyAddAuthorityGroupAuthoritiesValidationTest);
        String expectedResult = "Invalid Authorities [DEVELOPE]. Process finished with exit code 1";
        assertTrue(resultOfCmdToVerifyAddAuthorityGroupAuthoritiesValidationTest.contains(expectedResult));
    }

    @Test
    public void AddAuthorityGroupWithWrongOrderValidationTest() throws IOException {
        String[] cmdToVerifyAddAuthorityGroupWithWrongOrderValidationTest = {WINDOWS_COMMAND, "add", "authgroup", "-u", "admin", "-p", "admin123", "-an", "Test AG1", "-an", "Test AG1", "-ad", "This is for test", "-at", "DEVELOPER","-url", URL};
        String resultOfcmdToVerifyAddAuthorityGroupWithWrongOrderValidationTest = UtilsCLITest.consoleCommandExecuter(cmdToVerifyAddAuthorityGroupWithWrongOrderValidationTest);
        String expectedResult = "Invalid parameter: found 'add', expected one of [authgroup|user]. Process finished with exit code 1";
        assertTrue(resultOfcmdToVerifyAddAuthorityGroupWithWrongOrderValidationTest.contains(expectedResult));


    }

    @Test
    public void AddAuthorityGroupWithDuplicateAuthNameTest() throws IOException {
        String[] cmdToVerifyAddAuthorityGroupWithDuplicateAuthName = {WINDOWS_COMMAND, "authgroup", "add", "-u", "admin", "-p", "admin123", "-an", "Test AG", "Test AG", "-ad", "This is for test", "-at", "DEVELOPER","-url", URL};
        String resultOfVerifyAddAuthorityGroupWithDuplicateAuthName = UtilsCLITest.consoleCommandExecuter(cmdToVerifyAddAuthorityGroupWithDuplicateAuthName);
        String expectedResult = "The authority group already exists. Process finished with exit code 1";
        assertTrue(resultOfVerifyAddAuthorityGroupWithDuplicateAuthName.contains(expectedResult));

    }

    @Test
    public void AddAuthorityGroupWithDuplicateAuthoritiesTest() throws IOException {
        String[] cmdToVerifyAddAuthorityGroupWithDuplicateAuthoritiesTest = {WINDOWS_COMMAND, "authgroup", "add", "-u", "admin", "-p", "admin123", "-an", "TestAG", "-ad", "This is for test", "-at", "DEVELOPER", "-at", "DEVELOPER","-url", URL};
        String resultOfVerifycmdToVerifyAddAuthorityGroupWithDuplicateAuthoritiesTeste = UtilsCLITest.consoleCommandExecuter(cmdToVerifyAddAuthorityGroupWithDuplicateAuthoritiesTest);
        String expectedResult = "The authority group already exists. Process finished with exit code 1";
        assertTrue(resultOfVerifycmdToVerifyAddAuthorityGroupWithDuplicateAuthoritiesTeste.contains(expectedResult));

    }

    @Test
    public void AddAuthorityGroupWithDuplicateAuthDescripTest() throws IOException {
        String[] cmdToVerifyAddAuthorityGroupWithDuplicateAuthDescripTest = {WINDOWS_COMMAND, "authgroup", "add", "-u", "admin", "-p", "admin123", "-an", "TestAG", "-ad", " ", "-at", "DEVELOPER","-url", URL};
        String resultOfVerifycmdToVerifyAddAuthorityGroupWithDuplicateAuthDescripTest = UtilsCLITest.consoleCommandExecuter(cmdToVerifyAddAuthorityGroupWithDuplicateAuthDescripTest);
        String expectedResult = "The authority group already exists. Process finished with exit code 1";
        assertTrue(resultOfVerifycmdToVerifyAddAuthorityGroupWithDuplicateAuthDescripTest.contains(expectedResult));

    }

    @Test
    public void verifyAddAuthorityGroupWithAuthDescripTest() throws IOException {
        String[] cmdToverifyAddAuthorityGroupWithAuthDescripTest = {WINDOWS_COMMAND, "authgroup", "add", "-u", "admin", "-p", "admin123", "-an", "TestAG", "-ad", "", "-at", "DEVELOPER", "-at", "DEVELOPER","-url", URL};
        String resultOfcmdToverifyAddAuthorityGroupWithAuthDescripTest = UtilsCLITest.consoleCommandExecuter(cmdToverifyAddAuthorityGroupWithAuthDescripTest);
        String expectedResult = "Missing argument for option: ad. Process finished with exit code ";
        assertTrue(resultOfcmdToverifyAddAuthorityGroupWithAuthDescripTest.contains(expectedResult));

    }
}
