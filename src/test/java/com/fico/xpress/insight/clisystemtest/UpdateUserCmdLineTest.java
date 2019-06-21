/*
    Xpress Insight User Admin Cli
    ============================
    UpdateUserCmdLineTest.java
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
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.WINDOWS_COMMAND;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class UpdateUserCmdLineTest extends UtilsCLITest {


    @Before
    public void setUp() throws ParseException {
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig("admin", "admin123");
        String result = uploadResource(insightApplicationConfig);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(result);
        JSONArray jsonArray = getJSONArray(getJSONObject(json, "projects"), "items");
        //projectId to be populated
        projectId = (String) ((JSONObject) jsonArray.get(0)).get("id");
    }

    @After
    public void tearDown() {
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig("admin", "admin123");
        deleteResource(insightApplicationConfig, projectId);
    }

    @Test
    public void verifyUpdateCommandWithUpdatedNameInDescribeUser() throws IOException {
        String[] userUpdateCommandInput = {WINDOWS_COMMAND, "user", "update", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin", "-fn", "NewTestAdmin","-url",URL};
        String[] commandsForDescribeUser = {WINDOWS_COMMAND, "user", "describe", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin","-url",URL};
        //store the details of the User using describe
        List<String> resultsOfCommandsForDescribeUser = asList(UtilsCLITest.consoleCommandExecuter(commandsForDescribeUser).split("\\n"));
        //store the initial attribute value which is going to update
        List<String> resultOfUpdatedAttributeFirstName = asList(resultsOfCommandsForDescribeUser.subList(4, 5).toString().split(":"));
        //use the update command and store the results
        try {
            List<String> resultsOfUserUpdateCommandInput = asList(UtilsCLITest.consoleCommandExecuter(userUpdateCommandInput).split("\\n"));
            //check the successful update message
            assertTrue(resultsOfUserUpdateCommandInput.get(resultsOfUserUpdateCommandInput.size() - 2).contains(" Successfully updated."));
        } catch (Exception e) {
            assertTrue(resultOfUpdatedAttributeFirstName.get(resultOfUpdatedAttributeFirstName.size() - 2).trim().contains(userUpdateCommandInput[userUpdateCommandInput.length - 1].trim()));
            e.printStackTrace();
        }
    }

    @Test
    public void verifyUpdateCommandWithSameValueOFAdd_RemoveApps() throws IOException {
        String[] verifyUpdateCommandWithSameValueOFAdd_RemoveApps = {WINDOWS_COMMAND, "user", "update", "-u", "admin", "-p", "admin123", "-tu", "newadmin", "-fn", "updagain", "-aa", "AdvancedUser", "-ra", "AdvancedUser","-url",URL};
        String expectedResultForVerifyUpdateCommandWithSameValueOFAA_RA = "Duplicate App exists in add and remove App arguments. Process finished with exit code 1";
        assertTrue(Arrays.asList(UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithSameValueOFAdd_RemoveApps).split("\\n")).contains(expectedResultForVerifyUpdateCommandWithSameValueOFAA_RA));
        String[] verifyUpdateCommandWithSameValueOFRemove_AddApps = {WINDOWS_COMMAND, "user", "update", "-u", "admin", "-p", "admin123", "-tu", "newadmin", "-fn", "updagain", "-ra", "FirstStart", "-aa", "FirstStart","-url",URL};
        assertTrue(Arrays.asList(UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithSameValueOFRemove_AddApps).split("\\n")).contains(expectedResultForVerifyUpdateCommandWithSameValueOFAA_RA));
    }

    @Test
    public void verifyUserUpdateCommandWithMultipleAddArguments() throws IOException {
        String[] userUpdateCommandWithMultipleArguments = {WINDOWS_COMMAND, "user", "update", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin1", "-la", "true", "-fn",
                "himan", "-ln", "dugar", "-p", "admin123", "-em", "test@gmail.com", "-en", "true", "-lk", "true", "-tabe", "false", "-ag", "Developer", "-aa", "Basic","-url",URL};
        String[] commandsForDescribeUser = {WINDOWS_COMMAND, "user", "describe", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin1","-url",URL};
        //store the details of the User using describe
        List<String> resultsOfCommandsForDescribeUser = asList(UtilsCLITest.consoleCommandExecuter(commandsForDescribeUser).split("\\n"));
        Map userTableDetails = UtilsCLITest.getTableContainsDescribeUser("TestAdmin1");
        //run the update command
        List<String> resultsUserUpdateCommandWithMultipleArguments = asList(UtilsCLITest.consoleCommandExecuter(userUpdateCommandWithMultipleArguments).split("\\n"));
        String expectedResult = "TestAdmin Successfully updated.";
        if (resultsUserUpdateCommandWithMultipleArguments.get(resultsUserUpdateCommandWithMultipleArguments.size() - 1).contains(expectedResult)) {
            try {
                UtilsCLITest.consoleCommandExecuter(userUpdateCommandWithMultipleArguments);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //verify each attributed is getting updated w.r.t LA,FN<LN<EML<ENBLE<LOC<TBLENB<AG<APPS
            //validate local Account
            assertEquals(userUpdateCommandWithMultipleArguments[10], userTableDetails.get("Local Account"));
            //validate First name
            assertEquals(userUpdateCommandWithMultipleArguments[12], userTableDetails.get("First Name"));
            //validate last name
            assertEquals(userUpdateCommandWithMultipleArguments[14], userTableDetails.get("Last Name"));
            //validate Email
            assertEquals(userUpdateCommandWithMultipleArguments[18], userTableDetails.get("Email"));
            //validate Enabled
            assertEquals(userUpdateCommandWithMultipleArguments[20], userTableDetails.get("Enabled"));
            //Validate Locked
            assertEquals(userUpdateCommandWithMultipleArguments[22], userTableDetails.get("Locked"));
            //validate Tableau Enabled
            assertEquals(userUpdateCommandWithMultipleArguments[24], userTableDetails.get("Tableau Enabled"));
            //validate Authority Groups
            assertEquals(userUpdateCommandWithMultipleArguments[26], userTableDetails.get("Authority Groups"));

        }

    }

    @Test
    public void verifyUpdateCommandValidationWithRemoveArguments() throws IOException {
        String[] userUpdateCommandWithMultipleArguments = {WINDOWS_COMMAND, "user", "update", "-u", "admin", "-p", "admin123", "-tu", "newadmin8", "-fn", "UpdatedwithRG", "-rg", "Developer","-url",URL};
        //use the update command to remove Authority Groups Arguments
        List resultsOfUpdateCommandWithMultipleArguments = asList(UtilsCLITest.consoleCommandExecuter(userUpdateCommandWithMultipleArguments).split("\\n"));
        if (resultsOfUpdateCommandWithMultipleArguments.toString().contains(userUpdateCommandWithMultipleArguments[userUpdateCommandWithMultipleArguments.length - 5] + " Successfully updated.")) {
            //user should be able to get details of user after getting updated
            Map describeUserValue = UtilsCLITest.getTableContainsDescribeUser(userUpdateCommandWithMultipleArguments[userUpdateCommandWithMultipleArguments.length - 5]);
            //use assertion and make sure that user should not see removed authority
            assertNotEquals(String.valueOf(describeUserValue.get("Authority Groups")), userUpdateCommandWithMultipleArguments[userUpdateCommandWithMultipleArguments.length - 1]);
        }
    }

    @Test
    public void verifyUpdateCommandValidationWithAddAppsArguments() throws IOException {
        Map describeUser = UtilsCLITest.getTableContainsDescribeUser("TestAdmin");
        String[] verifyUpdateCommandValidationWithAddAppsArguments = {WINDOWS_COMMAND, "user", "update", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin", "-fn", "UpdatedwithRG", "-aa", "Quick Start","-url",URL};
        if (describeUser.containsValue("Apps")) {
            try {
                UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandValidationWithAddAppsArguments);
                assertEquals(describeUser.get("Apps"), verifyUpdateCommandValidationWithAddAppsArguments[verifyUpdateCommandValidationWithAddAppsArguments.length - 1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void verifyUpdateCommandValidationWithRemoveAppsArguments() throws IOException {
        Map describeUser = UtilsCLITest.getTableContainsDescribeUser("TestAdmin1");
        String[] verifyUpdateCommandValidationWithRemoveAppsArguments = {WINDOWS_COMMAND, "user", "update", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin1", "-fn", "UpdatedwithRG", "-ra", "Quick Start","-url",URL};
        String[] verifyUpdateCommandValidationWithAddAppsArguments = {WINDOWS_COMMAND, "user", "update", "-u", "admin", "-p", "admin123", "-tu", "TestAdmin1", "-fn", "UpdatedwithRG", "-aa", "Quick Start","-url",URL};

        if (describeUser.containsKey("Apps")) {
            try {
                UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandValidationWithRemoveAppsArguments).split("\\n");
                assertNotEquals(describeUser.get("Apps"), verifyUpdateCommandValidationWithRemoveAppsArguments[verifyUpdateCommandValidationWithRemoveAppsArguments.length - 1]);
            } catch (Exception e) {
                assertEquals(describeUser.get("Apps"), verifyUpdateCommandValidationWithRemoveAppsArguments[verifyUpdateCommandValidationWithRemoveAppsArguments.length - 1]);
                e.printStackTrace();
            }
        } else {
            try {
                UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandValidationWithAddAppsArguments).split("\\n");
                assertEquals(describeUser.get("Apps"), verifyUpdateCommandValidationWithAddAppsArguments[verifyUpdateCommandValidationWithAddAppsArguments.length - 1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void verifyUpdateCommandWithFullOptionName() throws IOException {
        //passed optionValue as "-targetUser" instead of "-tu"
        String[] verifyForUpdateCommandWithFullOptionName = {WINDOWS_COMMAND, "user", "update", "-u", "admin", "-p", "admin123", "-targetuser", "TestAdmin", "-fn", "himan27", "-ln", "dugar", "-p", "admin123", "-em", "test@gmail.com", "-tbu", "test2", "-ag", "Developer", "-ag", "BasicUser","-url",URL};
        String expectedResultForVerifyForUpdateCommandWithFullOptionName = "TestAdmin Successfully updated.";
        assertTrue(Arrays.asList(UtilsCLITest.consoleCommandExecuter(verifyForUpdateCommandWithFullOptionName).split("\\n")).contains(expectedResultForVerifyForUpdateCommandWithFullOptionName));
    }

    @Test
    public void verifyForUpdateCommandWithNonExitsAppsUser() throws IOException {
        String[] verifyForUpdateCommandWithNonExitsAppsUser = {WINDOWS_COMMAND, "user", "update", "-u", "admin", "-p", "admin123", "-targetuser", "TestAdmin", "-fn", "himan27", "-ln", "dugar", "-p", "admin123", "-em", "test@gmail.com", "-tbu", "test2", "-ag", "Developer", "-ag", "BasicUser", "-aa", "First Start","-url",URL};
        String expectedResultForVerifyForUpdateCommandWithNonExitsAppsUser = "Invalid App [First Start]. Process finished with exit code 1";
        assertTrue(Arrays.asList(UtilsCLITest.consoleCommandExecuter(verifyForUpdateCommandWithNonExitsAppsUser).split("\\n")).contains(expectedResultForVerifyForUpdateCommandWithNonExitsAppsUser));
    }

    @Test
    public void verifyForUpdateCommandWithWrongAuthorityGroup() throws IOException {
        String[] verifyForUpdateCommandWithWrongAuthorityGroup = {WINDOWS_COMMAND, "user", "update", "-u", "admin", "-p", "admin123", "-targetuser", "TestAdmin", "-fn", "himan27", "-ln", "dugar", "-p", "admin123", "-em", "test@gmail.com", "-tbu", "test2", "-ag", "Developer1", "-ag", "BasicUser", "-aa", "First Start","-url",URL};
        String expectedResultForVerifyForUpdateCommandWithWrongAuthorityGroup = "Invalid Authority Group [Developer1]. Process finished with exit code 1";
        assertTrue(Arrays.asList(UtilsCLITest.consoleCommandExecuter(verifyForUpdateCommandWithWrongAuthorityGroup).split("\\n")).contains(expectedResultForVerifyForUpdateCommandWithWrongAuthorityGroup));

    }

    @Test
    public void verifyUpdateCommandWithSameValueOFAdd_RemoveGroup() throws IOException {
        String[] verifyUpdateCommandWithSameValueOFAdd_RemoveGroup = {WINDOWS_COMMAND, "user", "update", "-u", "admin", "-p", "admin123", "-tu", "newadmin", "-fn", "updagain", "-ag", "AdvancedUser", "-rg", "AdvancedUser","-url",URL};
        String expectedResultOfVerifyUpdateCommandWithSameValueOFAdd_RemoveGroup = "Duplicate Authority Group exists in add and remove Authority Group arguments. Process finished with exit code 1";
        assertTrue(Arrays.asList(UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithSameValueOFAdd_RemoveGroup).split("\\n")).contains(expectedResultOfVerifyUpdateCommandWithSameValueOFAdd_RemoveGroup));
    }

    @Test
    public void verifyUpdateCommandWithableauEnabledArgs() throws IOException {
        String[] verifyUpdateCommandWithableauDisabledArgs = (WINDOWS_COMMAND+" user update -u admin -p admin123 -tu TestAdmin -fn updatedagain -tbd -url " +URL).split(" ");
        UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithableauDisabledArgs);
        Map describeUser = UtilsCLITest.getTableContainsDescribeUser("TestAdmin");
        assertEquals(describeUser.get("Tableau Enabled"), "false");
        String[] verifyUpdateCommandWithableauEnabledArgs = (WINDOWS_COMMAND+" user update -u admin -p admin123 -tu TestAdmin -fn updatedagain -tbe -url " +URL).split(" ");
        UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithableauEnabledArgs);
        Map describeUserUpdated = UtilsCLITest.getTableContainsDescribeUser("TestAdmin");
        assertEquals(describeUserUpdated.get("Tableau Enabled"), "true");

    }

    @Test
    public void verifyUpdateCommandWithlocalAccountArgs() throws IOException {
        String[] verifyUpdateCommandWithlocalAccountEnableArgs = (WINDOWS_COMMAND+" user update -u admin -p admin123 -tu TestAdmin -fn updatedagain -lae -url " +URL).split(" ");
        UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithlocalAccountEnableArgs);
        Map describeUser = UtilsCLITest.getTableContainsDescribeUser("TestAdmin");
        assertEquals(describeUser.get("Local Account"), "true");
        String[] verifyUpdateCommandWithlocalAccountDisableArgs = (WINDOWS_COMMAND+" user update -u admin -p admin123 -tu TestAdmin -fn updatedagain -lad -url " +URL).split(" ");
        UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithlocalAccountDisableArgs);
        Map describeUserUpdated = UtilsCLITest.getTableContainsDescribeUser("TestAdmin");
        assertEquals(describeUserUpdated.get("Local Account"), "false");

    }

    @Test
    public void verifyUpdateCommandWithlockedArgs() throws IOException {
        String[] verifyUpdateCommandWithlockedEnableArgs = (WINDOWS_COMMAND+" user update -u admin -p admin123 -tu TestAdmin -fn updatedagain -lk -url " +URL).split(" ");
        UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithlockedEnableArgs);
        Map describeUser = UtilsCLITest.getTableContainsDescribeUser("TestAdmin");
        assertEquals(describeUser.get("Locked"), "true");
        String[] verifyUpdateCommandWithLockedDisableArgs = (WINDOWS_COMMAND+" user update -u admin -p admin123 -tu TestAdmin -fn updatedagain -ulk -url " +URL).split(" ");
        UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithLockedDisableArgs);
        Map describeUserUpdated = UtilsCLITest.getTableContainsDescribeUser("TestAdmin");
        assertEquals(describeUserUpdated.get("Locked"), "false");

    }

    @Test
    public void verifyUpdateCommandWithEnabledArgs() throws IOException {
        String[] verifyUpdateCommandWithEnabledTrueArgs = (WINDOWS_COMMAND+" user update -u admin -p admin123 -tu TestAdmin -fn updatedagain -en -url " +URL).split(" ");
        UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithEnabledTrueArgs);
        Map describeUser = UtilsCLITest.getTableContainsDescribeUser("TestAdmin");
        assertEquals(describeUser.get("Enabled"), "true");
        String[] verifyUpdateCommandWithEnabledFalseArgs = (WINDOWS_COMMAND+" user update -u admin -p admin123 -tu TestAdmin -fn updatedagain -ds -url " +URL).split(" ");
        UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithEnabledFalseArgs);
        Map describeUserUpdated = UtilsCLITest.getTableContainsDescribeUser("TestAdmin");
        assertEquals(describeUserUpdated.get("Enabled"), "false");

    }

    @Test
    public void verifyUpdateCommandWithMultipleBooleanArgs() throws IOException {
        String[] verifyUpdateCommandWithLocked_EnabledTrueArgs =(WINDOWS_COMMAND+" user update -u admin -p admin123 -tu TestAdmin -fn updatedagain -en -ulk -url " +URL).split(" ");
        UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithLocked_EnabledTrueArgs);
        Map describeUser = UtilsCLITest.getTableContainsDescribeUser("TestAdmin");
        assertEquals(describeUser.get("Enabled"), "true");
        assertEquals(describeUser.get("Locked"), "false");
        String[] verifyUpdateCommandWithTBEnable_LocalAcntFalseArgs = (WINDOWS_COMMAND+" user update -u admin -p admin123 -tu TestAdmin -fn updatedagain -lad -tbe -url " +URL).split(" ");
        UtilsCLITest.consoleCommandExecuter(verifyUpdateCommandWithTBEnable_LocalAcntFalseArgs);
        Map describeUserUpdated = UtilsCLITest.getTableContainsDescribeUser("TestAdmin");
        assertEquals(describeUserUpdated.get("Local Account"), "false");
        assertEquals(describeUserUpdated.get("Tableau Enabled"), "true");

    }

}
