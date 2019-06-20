/*
    Xpress Insight User Admin Cli
    ============================
    InsightAdminCliTest.java
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

import com.fico.xpress.operationresult.ListAuthorityGroupsOperationResult;
import com.fico.xpress.operationresult.ListUsersOperationResult;
import com.fico.xpress.services.AuthorityGroupServiceImpl;
import com.fico.xpress.services.UserServiceImpl;
import com.fico.xpress.vos.AuthorityGroup;
import com.fico.xpress.vos.Command;
import com.fico.xpress.vos.OperationType;
import com.fico.xpress.vos.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.INSIGHT_HOST;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class InsightAdminCliTest {

    @InjectMocks
    private InsightAdminCli insightAdminCli;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private AuthorityGroupServiceImpl authorityGroupService;

    ByteArrayOutputStream outBuf = new ByteArrayOutputStream();

    PrintStream out = new PrintStream(outBuf, true);

    ByteArrayOutputStream errBuf = new ByteArrayOutputStream();

    PrintStream err = new PrintStream(errBuf, true);

    @Before
    public void setup() {
        System.setOut(out);
        System.setErr(err);
    }

    @Test
    public void testMainForParseException() {
        insightAdminCli.main(new String[] {"user", "list", "-u", "-p", "admin123"});
        assertThat(new String(errBuf.toByteArray()), is(allOf(
                containsString("Missing argument for option: u. Process finished with exit code 1")
        )));
    }

    @Test
    public void testMainForApplicationException() {
        insightAdminCli.main(new String[] {"user", "list", "-p", "admin123"});
        assertThat(new String(errBuf.toByteArray()), is(allOf(
                containsString("User Name is mandatory. Process finished with exit code 1")
        )));
    }

    @Test
    public void testProcessCommand() {
        List<String> userNames = new ArrayList<>();
        userNames.add("admin");
        userNames.add("admin2");
        ListUsersOperationResult listUsersOperationResult = new ListUsersOperationResult();
        listUsersOperationResult.setUserNames(userNames);
        InsightAdminCli insightAdminCliSpy = spy(insightAdminCli);
        ArgumentCaptor<Command> commandCaptor = ArgumentCaptor.forClass(Command.class);
        doReturn(listUsersOperationResult).when(userService).processUserCommand(commandCaptor.capture());
        insightAdminCliSpy.processCommand(new String[]{"user", "list", "-u", "admin", "-p", "admin123", "-url", INSIGHT_HOST});
        assertThat(new String(outBuf.toByteArray()), is(allOf(
                containsString("admin")
        )));
        Command createdCommand = commandCaptor.getValue();
        assertTrue(createdCommand.getResource() == Resource.USER);
        assertTrue(createdCommand.getOperationType() == OperationType.LIST);
        assertTrue(createdCommand.getInsightApplicationConfig().getUserName().equals("admin"));
        assertTrue(createdCommand.getInsightApplicationConfig().getPassword().equals("admin123"));
        assertTrue(createdCommand.getInsightApplicationConfig().getHostUrl().equals(INSIGHT_HOST));
    }

    @Test
    public void testPrintHelp() {
        insightAdminCli.main(new String[] {"-h"});
        assertThat(new String(outBuf.toByteArray()), is(allOf(
                containsString(" FICO Xpress Insight administration tool")
        )));
    }
    @Test
    public void testProcessResourceForUser() {
        Command command = new Command();
        command.setResource(Resource.USER);
        List<String> userNames = new ArrayList<>();
        userNames.add("admin");
        userNames.add("admin2");
        ListUsersOperationResult listUsersOperationResult = new ListUsersOperationResult();
        listUsersOperationResult.setUserNames(userNames);
        doReturn(listUsersOperationResult).when(userService).processUserCommand(command);
        insightAdminCli.processResource(command);
        assertThat(new String(outBuf.toByteArray()), is(allOf(
                containsString("admin")
        )));
    }

    @Test
    public void testProcessResourceForAuthorityGroup() {
        Command command = new Command();
        command.setResource(Resource.AUTHGROUP);
        List<AuthorityGroup> authorityGroups = new ArrayList<>();
        authorityGroups.add(new AuthorityGroup("AdvancedUser", false));
        authorityGroups.add(new AuthorityGroup("BasicUser", false));
        ListAuthorityGroupsOperationResult listUsersOperationResult = new ListAuthorityGroupsOperationResult(authorityGroups);
        doReturn(listUsersOperationResult).when(authorityGroupService).processAuthorityGroupCommand(command);
        insightAdminCli.processResource(command);
        assertThat(new String(outBuf.toByteArray()), is(allOf(
            containsString("AdvancedUser"),
            containsString("BasicUser")
        )));
    }
}
