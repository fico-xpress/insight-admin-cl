/*
    Xpress Insight User Admin Cli
    ============================
    InsightAdminCliParserTest.java
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
package com.fico.xpress.commandline;

import com.fico.xpress.exception.ApplicationException;
import com.fico.xpress.utility.InsightAdminCliConstants;
import com.fico.xpress.utility.ReadConsolePassword;
import com.fico.xpress.vos.Command;
import com.fico.xpress.vos.InsightApplicationConfig;
import com.fico.xpress.vos.OperationType;
import com.fico.xpress.vos.Resource;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.INSIGHT_HOST;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class InsightAdminCliParserTest {


    InsightAdminCliParser insightAdminCliParser;

    ByteArrayOutputStream outBuf = new ByteArrayOutputStream();

    PrintStream out = new PrintStream(outBuf, true);

    @Mock
    ReadConsolePassword readConsolePassword;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        // to be able to read what was logged
        insightAdminCliParser.setOut(out);
        insightAdminCliParser.setReadConsolePassword(readConsolePassword);
    }

    @Test
    public void testParseCommandArgs() throws ParseException {
        Command command = new Command();
        command.setResource(Resource.USER);
        command.setOperationType(OperationType.LIST);
        command.setInsightApplicationConfig(new InsightApplicationConfig());
        command.getInsightApplicationConfig().setHostUrl(INSIGHT_HOST);

        Command expectedCommand = insightAdminCliParser.parseCommandArgs(new String[]{"user", "list", "-u", "admin",
                "-p", "admin123", "-url", INSIGHT_HOST});
        assertEquals(expectedCommand.getResource(), command.getResource());
        assertEquals(expectedCommand.getOperationType(), command.getOperationType());
        assertEquals(expectedCommand.getInsightApplicationConfig().getHostUrl(), command.getInsightApplicationConfig().getHostUrl());
    }

    @Test
    public void testParseCommandArgsHelp() throws  ParseException{
        Command expectedCommand = insightAdminCliParser.parseCommandArgs(new String[]{"-h"});
        assertEquals(true, expectedCommand.isShowHelp());
    }

    @Test
    public void testParseCommandArgsUserFailure() throws  ParseException{
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("User Name is mandatory.");
        insightAdminCliParser.parseCommandArgs(new String[]{"user", "list", "-p", "admin123"});
    }

    @Test
    public void testParseCommandArgsPasswordFailure() throws  ParseException{
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Password is mandatory.");
        insightAdminCliParser.parseCommandArgs(new String[]{"user", "list", "-u", "admin"});
    }
    @Test
    public void testNextFailure() throws  ParseException{
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Insufficient command-line parameters, expected: [add|describe|list|update]");
        insightAdminCliParser.parseCommandArgs(new String[]{"user", "-u", "admin"});
    }
    @Test
    public void testReadFailure() throws  ParseException{
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Invalid parameter: found 'lis', expected one of [add|describe|list|update]");
        insightAdminCliParser.parseCommandArgs(new String[]{"user", "lis","-u", "admin"});
    }

    @Test
    public void testParseArgumentsForMandatoryFields() throws ParseException{
        String[] args = new String[]{"user", "add", "-u", "admin", "-p", "admin123",
                "-tu", "newadmin", "-fn", "firstname", "-ln", "lastname", "-tp", "admin123", "-url", INSIGHT_HOST};
        Command command = insightAdminCliParser.parseCommandArgs(args);
        assertEquals("admin123", command.getInsightApplicationConfig().getPassword());
        assertEquals("newadmin", command.getArguments().get("tu"));
        assertEquals("firstname", command.getArguments().get("fn"));
        assertEquals("lastname", command.getArguments().get("ln"));
        assertEquals("admin123", command.getArguments().get("tp"));
        assertEquals(INSIGHT_HOST, command.getInsightApplicationConfig().getHostUrl());
    }

    @Test
    public void testParseArgumentsForOptionalFields() throws ParseException{
        String[] args = new String[]{"user", "add", "-u", "admin", "-p", "admin123",
            "-tu", "newadmin", "-fn", "firstnamr", "-ln", "lastname", "-tp", "admin123",
            "-em", "abc@fico.com", "-ag", "Developer", "-ag", "BasicUser", "-aa", "Quick Start", "-lae", "-en",
            "-tbd", "-tbu", "tabname", "-rg", "AdvancedUser", "-rg", "BasicUser", "-ra", "Conversion",
                "-url", INSIGHT_HOST};
        Command command = insightAdminCliParser.parseCommandArgs(args);
        assertEquals("abc@fico.com", command.getStringArgumentValue("em"));
        assertEquals(true, command.getBooleanArgumentValue("lae"));
        assertEquals(true, command.getBooleanArgumentValue("en"));
        assertEquals(false, command.getBooleanArgumentValue("lk"));
        assertEquals(false, command.getBooleanArgumentValue("tbe"));
        assertEquals("tabname", command.getStringArgumentValue("tbu"));
        assertEquals("Developer", command.getMultipleArgumentValue("ag").get(0));
        assertEquals("BasicUser", command.getMultipleArgumentValue("ag").get(1));
        assertEquals("Quick Start", command.getMultipleArgumentValue("aa").get(0));
        assertEquals("AdvancedUser", command.getMultipleArgumentValue("rg").get(0));
        assertEquals("BasicUser", command.getMultipleArgumentValue("rg").get(1));
        assertEquals("Conversion", command.getMultipleArgumentValue("ra").get(0));
        assertEquals(INSIGHT_HOST, command.getInsightApplicationConfig().getHostUrl());
    }

    @Test
    public void testParseArgumentsForTargetUsernameThrowsException() throws ParseException{
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Target new User Name is mandatory.");
        insightAdminCliParser.parseCommandArgs(new String[]{"user", "add", "-u", "admin", "-p", "admin123",
                "-url", INSIGHT_HOST});
    }

    @Test
    public void testParseArgumentsForFirstNameThrowsException() throws ParseException{
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("First Name is mandatory.");
        insightAdminCliParser.parseCommandArgs(new String[]{"user", "add", "-u", "admin", "-p", "admin123",
                "-tu", "admin2", "-url", INSIGHT_HOST});
    }

    @Test
    public void testParseArgumentsForLastNameThrowsException() throws ParseException{
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Last Name is mandatory.");
        insightAdminCliParser.parseCommandArgs(new String[]{"user", "add", "-u", "admin", "-p", "admin123",
                "-tu", "admin2", "-fn", "fname", "-url", INSIGHT_HOST});
    }

    @Test
    public void testParseArgumentsForTargetPasswordThrowsException() throws ParseException{
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Target new User Password is mandatory.");
        insightAdminCliParser.parseCommandArgs(new String[]{"user", "add", "-u", "admin", "-p", "admin123",
                "-tu", "admin2", "-fn", "fname", "-ln", "lname", "-url", INSIGHT_HOST});
    }

    @Test
    public void testPrintHelp() {
        insightAdminCliParser.printHelp();
        assertThat(new String(outBuf.toByteArray()), is(allOf(
            containsString("usage: insightadmincli "),
            containsString("[authgroup|user]"),
            containsString("[add|describe|list|update]"),
            containsString("[-p"),
            containsString("<password>]"),
            containsString("-h,--help "),
            containsString("-u,--username <username>"),
            containsString(" -p,--password <password>"),
            containsString("-tu,--targetuser <targetuser>"),
            containsString("-ln,--lastname <lastname>"),
            containsString("-tp,--targetpassword <targetpassword>"),
            containsString("-lae,--localaccountenable"),
            containsString("-lad,--localaccountdisable"),
            containsString("-em,--email <email>"),
            containsString("-en,--enabled"),
            containsString("-ds,--disabled"),
            containsString("-lk,--locked"),
            containsString("-ulk,--unlocked"),
            containsString("-tbe,--tableauenabled"),
            containsString("-tbd,--tableaudisabled"),
            containsString("-ag,--addauthoritygroup <addauthoritygroup>"),
            containsString("-aa,--addapp <addapp>"),
            containsString("-ra,--removeapp <removeapp>"),
            containsString("-tbu,--tableauusername <tableauusername>"),
            containsString("-rg,--removeauthoritygroup <removeauthoritygroup>")
        )));
    }

    @Test
    public void testProcessUserCommandForDescribeError() throws ParseException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Target User Name is mandatory.");
        insightAdminCliParser.parseCommandArgs(new String[]{"user", "describe", "-u", "admin", "-p", "admin123",
                "-url", INSIGHT_HOST});
    }

    @Test
    public void testParseArgumentsForUpdateOperation() throws  ParseException{
        String[] args = new String[]{"user", "update", "-u", "admin", "-p", "admin123",
                "-tu", "newadmin", "-fn", "updatedfirstname", "-url", INSIGHT_HOST};
        Command command = insightAdminCliParser.parseCommandArgs(args);
        assertEquals("updatedfirstname", command.getStringArgumentValue("fn"));
    }

    @Test
    public void testParseArgumentsForDescribeOperation() throws  ParseException{
        String[] args = new String[]{"authgroup", "describe", "-u", "admin", "-p", "admin123",
                "-an", "newadmin", "-url", INSIGHT_HOST};
        Command command = insightAdminCliParser.parseCommandArgs(args);
        assertEquals("newadmin", command.getStringArgumentValue(InsightAdminCliConstants.ADD_AUTHORITY_GROUP_NAME));
    }

    @Test
    public void testParseArgumentsForBooleanFieldThrowsException() throws ParseException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Tableau enable and disable option cannot be present simultaneously.");
        insightAdminCliParser.parseCommandArgs(new String[] {"user", "update", "-u", "admin", "-p", "admin123", "-tu", "test", "-tbe", "-tbd",
                "-url", INSIGHT_HOST});
    }

    @Test
    public void testParseArgumentsForAuthorityGroupAddUpdateOperation() throws ParseException {
        String[] args = new String[]{"authgroup", "add", "-u", "admin", "-p", "admin123",
            "-an", "newauthority", "-ad", "description", "-at", "DEVELOPER", "-at", "APP_ALL", "-rt", "APP_PROJECT",
                "-url", INSIGHT_HOST};
        Command command = insightAdminCliParser.parseCommandArgs(args);
        List<String> authorities = command.getMultipleArgumentValue("at");
        assertEquals("newauthority", command.getStringArgumentValue("an"));
        assertEquals("description", command.getStringArgumentValue("ad"));
        assertEquals("DEVELOPER", authorities.get(0));
        assertEquals("APP_ALL", authorities.get(1));
        assertEquals("APP_PROJECT", command.getMultipleArgumentValue("rt").get(0));
    }

    @Test
    public void testParseArgumentsForAuthorityGroupAddOperationThrowsApplicationException() throws ParseException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Authority Group Name is mandatory.");
        insightAdminCliParser.parseCommandArgs(new String[]{"authgroup", "add", "-u", "admin", "-p", "admin123",
                "-url", INSIGHT_HOST});
    }

    @Test
    public void testParseCommandArgsPasswordInputFromConsole() throws ParseException {
        doReturn("admin123").when(readConsolePassword).readPassword();
        Command command = insightAdminCliParser.parseCommandArgs(new String[] {"user", "list", "-u", "admin"});
        assertEquals("admin123", command.getInsightApplicationConfig().getPassword());
    }
}
