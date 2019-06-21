/*
    Xpress Insight User Admin Cli
    ============================
    UpdateAuthorityGroupCmdLineTest.java
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
import java.util.Map;

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.WINDOWS_COMMAND;
import static com.fico.xpress.insight.clisystemtest.UtilsCLITest.URL;
import static junit.framework.TestCase.assertTrue;

public class UpdateAuthorityGroupCmdLineTest {

    @Test
    public void verifyUpdateAuthorityGroupCmdLineTest() throws IOException {
        Long newTime = new java.util.Date().getTime();
        String[] addAuthGroup = (WINDOWS_COMMAND+" authgroup add -an testAg1" + newTime + " -u admin -p admin123 -url " + URL).split(" ");
        String testUserName = (UtilsCLITest.consoleCommandExecuter(addAuthGroup).split("\n"))[1].split(" ")[0];
        String[] updateauthgroupCmd = (WINDOWS_COMMAND+" authgroup update -an " + testUserName + " -ad \"Testnewdescription\" -at DEVELOPER -u admin -p admin123 -url " + URL).split(" ");
        String resultsOFUpdateCmd = UtilsCLITest.consoleCommandExecuter(updateauthgroupCmd);
        assertTrue(resultsOFUpdateCmd.contains("Successfully updated"));
        Map authGroupDetails = UtilsCLITest.getTableContainsDescribeAuthorityUser(testUserName);
        assertTrue(authGroupDetails.get("Authority Group Description").toString().contains("Testnewdescription"));
        assertTrue(authGroupDetails.get("Authorities").toString().contains("DEVELOPER"));
    }

    @Test
    public void verifyUpdateAuthorityGroupWithRemovedAuthorities() throws IOException {
        String[] addCmdauthgroupAuthorityCmd = (WINDOWS_COMMAND+" authgroup add -an testAg -ad \"Test description\" -at DEVELOPER -u admin -p admin123 -url " + URL).split(" ");
        UtilsCLITest.consoleCommandExecuter(addCmdauthgroupAuthorityCmd);
        String[] addauthgroupAuthorityCmd = (WINDOWS_COMMAND+" authgroup update -an testAg -ad \"Test description\" -at DEVELOPER -u admin -p admin123 -url " + URL).split(" ");
        UtilsCLITest.consoleCommandExecuter(addauthgroupAuthorityCmd);
        String[] removeauthgroupAuthorityCmd = (WINDOWS_COMMAND+" authgroup update -an testAg -ad \"Test description\" -rt DEVELOPER -u admin -p admin123 -url " + URL).split(" ");
        String firstUpdateMsg = UtilsCLITest.consoleCommandExecuter(removeauthgroupAuthorityCmd);
        assertTrue(firstUpdateMsg.contains("testAg Successfully updated."));
        String secondUpdateMsg = UtilsCLITest.consoleCommandExecuter(removeauthgroupAuthorityCmd);
        assertTrue(secondUpdateMsg.contains("Removing a non selected Authorities [DEVELOPER]. Process finished with exit code 1"));

    }

    @Test
    public void verifyUpdateAuthorityHelpOption() throws IOException {
        String[] addauthgroupAuthorityhelpCmd = (WINDOWS_COMMAND+" authgroup update -an testAg -ad \"Test description\" -at DEVELOPER -u admin -p admin123 -h -url " + URL).split(" ");
        Object resultsofCmd = UtilsCLITest.consoleCommandExecuter(addauthgroupAuthorityhelpCmd);
        assertTrue(resultsofCmd.toString().contains("--help"));
    }

    @Test
    public void verifyUpdateAuthorityUpdateWrongOrderOptionValidation() throws IOException {
        String[] updateAuthorityUpdateWrongOrderOptionValidation = (WINDOWS_COMMAND+" update authgroup -at DEVELOPER -ad \"Test description\"  -an testAg -u admin -p admin123 -url " + URL).split(" ");
        Object resultsofCmd = UtilsCLITest.consoleCommandExecuter(updateAuthorityUpdateWrongOrderOptionValidation);
        String expectedResult = "Invalid parameter: found 'update', expected one of [authgroup|user]. Process finished with exit code 1";
        assertTrue(((String) resultsofCmd).contains(expectedResult));
    }

    @Test
    public void verifyUpdateAuthorityUpdateWithAuthNameValidation() throws IOException {
        String[] updateAuthorityUpdateWithAuthNameValidation = (WINDOWS_COMMAND+" authgroup update -at DEVELOPER -ad \"Test description\"  -authoritynam testAg -u admin -p admin123 -url " + URL).split(" ");
        Object resultsofCupdateAuthorityUpdateWithAuthNameValidation = UtilsCLITest.consoleCommandExecuter(updateAuthorityUpdateWithAuthNameValidation);
        String expectedResult = "Unrecognized option: -authoritynam. Process finished with exit code 1";
        assertTrue(((String) resultsofCupdateAuthorityUpdateWithAuthNameValidation).contains(expectedResult));
    }

    @Test
    public void verifyUpdateAuthorityUpdateWithAuthDescriptionValidation() throws IOException {
        String[] updateAuthorityUpdateWithAuthDescriptionValidation = (WINDOWS_COMMAND+" authgroup update -at DEVELOPER -ad   -authoritygroupname testAg -u admin -p admin123 -url " + URL).split(" ");
        Object resultsofCupdateAuthorityUpdateWithAuthDescriptionValidation = UtilsCLITest.consoleCommandExecuter(updateAuthorityUpdateWithAuthDescriptionValidation);
        String expectedResult = "Missing argument for option: ad. Process finished with exit code 1";
        assertTrue(((String) resultsofCupdateAuthorityUpdateWithAuthDescriptionValidation).contains(expectedResult));
    }

    @Test
    public void verifyUpdateAuthorityUpdateWithAuthAuthoritiesValidation() throws IOException {
        String[] updateAuthorityUpdateWithAuthAuthoritiesValidation = (WINDOWS_COMMAND+" authgroup update -at developer -ad  \"Test description\"  -authoritygroupname AdvancedUser -u admin -p admin123 -url " + URL).split(" ");
        String expectedresult = "Invalid Authorities [developer]. Process finished with exit code 1";
        Object resultsofCupdateAuthorityUpdateWithAuthAuthoritiesValidation = UtilsCLITest.consoleCommandExecuter(updateAuthorityUpdateWithAuthAuthoritiesValidation);
        assertTrue(((String) resultsofCupdateAuthorityUpdateWithAuthAuthoritiesValidation).contains(expectedresult));

    }
}
