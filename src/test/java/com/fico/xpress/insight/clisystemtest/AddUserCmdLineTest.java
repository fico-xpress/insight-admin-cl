/*
    Xpress Insight User Admin Cli
    ============================
    AddUserCmdLineTest.java
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

import com.fico.xpress.vos.InsightApplicationConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.WINDOWS_COMMAND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AddUserCmdLineTest extends UtilsCLITest {
    @BeforeClass
    public static void onlyOnce() {

    }
    @Before
    public void testUploadApp() throws ParseException {
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig("admin", "admin123");
        String result = uploadResource(insightApplicationConfig);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(result);
        JSONArray jsonArray = getJSONArray(getJSONObject(json, "projects"), "items");
        //projectId to be populated
        projectId = (String) ((JSONObject) jsonArray.get(0)).get("id");
        System.out.println(System.getProperty("java.runtime.version"));
    }


    @After
    public void testDeleteApp() {
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig("admin", "admin123");
        deleteResource(insightApplicationConfig, projectId);
    }

    @Test
    public void testAddedNewUserInListValidation() throws IOException {
        String[] testAddedNewUserInListValidation = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin", "-fn", "Test", "-ln", "User", "-tp", "admin123","-url", URL};
        String[] addedListUserDetails = {WINDOWS_COMMAND, "user", "list", "-u", "admin", "-p", "admin123","-url", URL};
        List resultsAddedNewUserInList = Arrays.asList(UtilsCLITest.consoleCommandExecuter(testAddedNewUserInListValidation).split("\\n"));
        List resultsAddedListUserDetails = Arrays.asList(UtilsCLITest.consoleCommandExecuter(addedListUserDetails).split("\\n"));
        int initialSizeList = resultsAddedListUserDetails.size();
        //check if new user is already added in the list
        ListIterator list_user = resultsAddedListUserDetails.listIterator();
        String expectedResultforAddedUser = "The user already exists. Process finished with exit code 1";
        String expectedResultforNewlyAddedUser = testAddedNewUserInListValidation[8] + "Successfully added.";

        while (list_user.hasNext()) {
            if (!list_user.next().toString().contains(testAddedNewUserInListValidation[8])) {
                UtilsCLITest.consoleCommandExecuter(testAddedNewUserInListValidation);
                int Updated_Size = resultsAddedListUserDetails.size();
                if (Updated_Size > initialSizeList) {
                    try {
                        assertTrue(resultsAddedNewUserInList.get(resultsAddedNewUserInList.size() - 1).toString().contains(expectedResultforAddedUser));
                    } catch (Exception e) {
                        assertTrue(resultsAddedNewUserInList.get(resultsAddedNewUserInList.size() - 1).toString().contains(expectedResultforNewlyAddedUser));
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    @Test
    public void verifyAUserInputsCommandInOrderValidation() throws IOException {
        String[] testAddCommandsNotInUserInputs = {WINDOWS_COMMAND, "user", "-u", "admin", "-p", "admin123", "-tu", "newadmin", "-fn", "adarsh", "-ln", "jais", "-tp", "admin123", "add","-url", URL};
        String expectedResult = "The user already exists. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(testAddCommandsNotInUserInputs).contains(expectedResult));
        String[] testAddCommandsNotInUserInputs1 = {WINDOWS_COMMAND, "user", "-u", "admin", "-p", "admin123", "add", "-tu", "newadmin", "-fn", "adarsh", "-ln", "jais", "-tp", "admin123", "add", "add","-url", URL};
        assertTrue(UtilsCLITest.consoleCommandExecuter(testAddCommandsNotInUserInputs1).contains(expectedResult));
    }

    @Test
    public void verifyAuthorityGroupValidationForAddUser() throws IOException {
        String[] verifyAuthorityGroupValidationForAddUser = {WINDOWS_COMMAND, "user", "ADD", "-u", "admin", "-p", "admin123", "-tu", "abcde", "-fn", "adarsh", "-ln", "jais", "-tp", "admin123", "-ag", "Advance User","-url", URL};
        String expectedResultWhenAddCommandInCaps = "Invalid parameter: found 'ADD', expected one of [add|describe|list|update]. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyAuthorityGroupValidationForAddUser).contains(expectedResultWhenAddCommandInCaps));
    }

    @Test
    public void verifyInvalidAutorityGroupOption() throws IOException {
        String[] verifyInvalidAutorityGroupOption = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "-tu", "Admin", "-fn", "adarsh", "-ln", "jais", "-tp", "admin123", "-a", "Advance User","-url", URL};
        String expectedResultToVerifyInvalidAutorityGroupOption = "Unrecognized option: -a. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyInvalidAutorityGroupOption).contains(expectedResultToVerifyInvalidAutorityGroupOption));
    }

    @Test
    public void verifyAuthorityGroupWithInvalidArguments() throws IOException {
        String[] verifyAuthorityGroupWithInvalidArguments = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "-tu", "Admin", "-fn", "adarsh", "-ln", "jais", "-tp", "admin123", "-ag", "-aa", "Advance User","-url", URL};
        String expectedResultToVerifyAuthorityGroupWithInvalidArguments = "Missing argument for option: ag. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyAuthorityGroupWithInvalidArguments).contains(expectedResultToVerifyAuthorityGroupWithInvalidArguments));
    }

    @Test
    public void verifyAppsValidationWithoutArgumentValue() throws IOException {
        String[] verifyAppsValidationWithoutArgumentValue = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "-tu", "Admin", "-fn", "adarsh", "-ln", "jais", "-tp", "admin123", "-aa", "-ag", "Advance User","-url", URL};
        String expectedResultToVerifyAppsValidationWithoutArgumentValue = "Missing argument for option: aa. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyAppsValidationWithoutArgumentValue).contains(expectedResultToVerifyAppsValidationWithoutArgumentValue));
    }

    @Test
    public void verifyAuthorityGroupWrongArguments() throws IOException {

        String[] verifyAuthorityGroupWrongArguments = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "-tu", "newadmin8", "-fn", "adarsh", "-ln", "jais", "-tp", "admin123", "-ag", "Advance User","-url", URL};
        String expectedResultVerifyAuthorityGroupWrongArguments = "Invalid Authority Group [Advance User]. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyAuthorityGroupWrongArguments).contains(expectedResultVerifyAuthorityGroupWrongArguments));

    }

    @Test
    public void verifyAuthorityUserIsGettingAddedInList() throws IOException {
        String[] verifyAuthorityUserIsGettingAddedInList = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin1", "-fn", "adarsh", "-ln", "jais", "-tp", "admin123", "-ag", "AdvancedUser","-url", URL};
        //to check whether Authority Group Added or not in the list
        String[] resultsOfDescribeUser = {WINDOWS_COMMAND, "user", "describe", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin1","-url", URL};
        List resultsVerifyDescribeUser = Arrays.asList(UtilsCLITest.consoleCommandExecuter(resultsOfDescribeUser).split("\\n"));
        List check_AuthorityGroup_Value = Arrays.asList(resultsVerifyDescribeUser.get(resultsVerifyDescribeUser.size() - 2).toString().split(":"));
        if (check_AuthorityGroup_Value.get(check_AuthorityGroup_Value.size() - 1) != null) {
            UtilsCLITest.consoleCommandExecuter(verifyAuthorityUserIsGettingAddedInList);
            List resultsToVerifyDescribeUserAfterAGAdded = Arrays.asList(UtilsCLITest.consoleCommandExecuter(resultsOfDescribeUser).split("\\n"));
            List check_UpdatedAuthorityGroup_Value = Arrays.asList(resultsToVerifyDescribeUserAfterAGAdded.get(resultsToVerifyDescribeUserAfterAGAdded.size() - 2).toString().split(":"));
            assertEquals(check_AuthorityGroup_Value.get(check_AuthorityGroup_Value.size() - 1), check_UpdatedAuthorityGroup_Value.get(check_UpdatedAuthorityGroup_Value.size() - 1));
        }


    }

    @Test
    public void verifyMultipleAuthorityUserIsGettingAddedInList() throws IOException {
        String[] verifyMultipleAuthorityUserIsGettingAddedInList = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin2", "-fn", "adarsh", "-ln", "jais", "-tp", "admin123", "-ag", "AdvancedUser", "-ag", "Developer","-url", URL};
        //to check whether Authority Group Added or not in the list
        String[] resultsOfDescribeUser = {WINDOWS_COMMAND, "user", "describe", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin2","-url", URL};
        List resultsVerifyDescribeUser = Arrays.asList(UtilsCLITest.consoleCommandExecuter(resultsOfDescribeUser).split("\\n"));
        List check_AuthorityGroup_Value = Arrays.asList(resultsVerifyDescribeUser.get(resultsVerifyDescribeUser.size() - 2).toString().split(":"));
        if (check_AuthorityGroup_Value.get(check_AuthorityGroup_Value.size() - 1) != null) {
            UtilsCLITest.consoleCommandExecuter(verifyMultipleAuthorityUserIsGettingAddedInList);
            List resultsToVerifyDescribeUserAfterAGAdded = Arrays.asList(UtilsCLITest.consoleCommandExecuter(resultsOfDescribeUser).split("\\n"));
            List check_UpdatedAuthorityGroup_Value = Arrays.asList(resultsToVerifyDescribeUserAfterAGAdded.get(resultsToVerifyDescribeUserAfterAGAdded.size() - 2).toString().split(":"));
            List second_AG_Value = Arrays.asList(check_UpdatedAuthorityGroup_Value.get(check_UpdatedAuthorityGroup_Value.size() - 1).toString().split(","));
            String expectedResult = "Developer";
            //validate the second Authority User 
            assertEquals(second_AG_Value.get(second_AG_Value.size() - 1).toString().trim(), expectedResult);
        }

    }

    @Test
    public void verifyTargetUserNameForAddUser() throws IOException {
        //verify target Username /first name/Last Name/Target Password
        //verify target Username "-tu"
        String[] verifyTargetUserNameForAddUser = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "", "TestAdmin2", "-fn", "adarsh", "-ln", "jais", "-tp", "admin123", "-ag", "AdvancedUser", "-ag", "Developer","-url", URL};
        String expectedResultToVerifyTargetUserNameForAddUser = "Target new User Name is mandatory. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyTargetUserNameForAddUser).contains(expectedResultToVerifyTargetUserNameForAddUser));

    }

    @Test
    public void verifyTargetUserNameWithMissingArgumentsForAddUser() throws IOException {
        //verify target Username "-tu" missing arguments
        String[] verifyTargetUserNameWithMissingArgumentsForAddUser = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "-tu", "", "-fn", "adarsh", "-ln", "jais", "-tp", "admin123", "-ag", "AdvancedUser", "-ag", "Developer","-url", URL};
        String expectedResultToVerifyTargetUserNameWithMissingArgumentsForAddUser = "Missing argument for option: tu. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyTargetUserNameWithMissingArgumentsForAddUser).contains(expectedResultToVerifyTargetUserNameWithMissingArgumentsForAddUser));

    }

    @Test
    public void verifyFirstNameForAddUser() throws IOException {
        //verify first name  "-fn"
        String[] verifyFirstNameForAddUser = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin2", "", "adarsh", "-ln", "jais", "-tp", "admin123", "-ag", "AdvancedUser", "-ag", "Developer","-url", URL};
        String expectedResultToVerifyFirstNameForAddUser = "First Name is mandatory. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyFirstNameForAddUser).contains(expectedResultToVerifyFirstNameForAddUser));

    }

    @Test
    public void verifyLastNameForAddUser() throws IOException {
        //verify Last Name "-ln"
        String[] verifyLastNameForAddUser = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin2", "-fn", "adarsh", "", "jais", "-tp", "admin123", "-ag", "AdvancedUser", "-ag", "Developer","-url", URL};
        String expectedResultToVerifyLastNameForAddUser = "Last Name is mandatory. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyLastNameForAddUser).contains(expectedResultToVerifyLastNameForAddUser));

    }

    @Test
    public void verifyTargetPasswordForAddUser() throws IOException {
        //verify target password "-tp"
        String[] verifyTargetPasswordForAddUser = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin2", "-fn", "adarsh", "-ln", "jais", "", "admin123", "-ag", "AdvancedUser", "-ag", "Developer","-url", URL};
        String expectedResultToVerifyTargetPasswordForAddUser = "Target new User Password is mandatory. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyTargetPasswordForAddUser).contains(expectedResultToVerifyTargetPasswordForAddUser));
    }

    @Test
    public void verifyAddAppWithAddAppCmdforAddUser() throws IOException {
        Long newtime = new java.util.Date().getTime();
        String[] verifyAddAppWithAddAppCmdforAddUser = {WINDOWS_COMMAND, "user", "add", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin" + newtime, "-tp", "admin123", "-fn", "user", "-ln", "lsname", "-aa", "Conversion","-url", URL};
        String resultAddUserCmd = UtilsCLITest.consoleCommandExecuter(verifyAddAppWithAddAppCmdforAddUser);
        assertTrue(resultAddUserCmd.contains("TestAdmin" + newtime + " Successfully added."));
        Map describeUserCmd = UtilsCLITest.getTableContainsDescribeUser("TestAdmin" + newtime);
        assertTrue(describeUserCmd.get("Apps").toString().contains("Conversion"));
    }

}
