/*
    Xpress Insight User Admin Cli
    ============================
    UserServiceImplTest.java
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
package com.fico.xpress.services;

import com.fico.xpress.exception.ApplicationException;
import com.fico.xpress.operationresult.AddUserOperationResult;
import com.fico.xpress.operationresult.DescribeUserOperationResult;
import com.fico.xpress.operationresult.ListUsersOperationResult;
import com.fico.xpress.operationresult.UpdateUserOperationResult;
import com.fico.xpress.utility.InsightAdminCliConstants;
import com.fico.xpress.vos.AuthorityGroup;
import com.fico.xpress.vos.Command;
import com.fico.xpress.vos.InsightApplicationConfig;
import com.fico.xpress.vos.OperationType;
import com.fico.xpress.vos.Project;
import com.fico.xpress.vos.UserInfo;
import com.fico.xpress.vos.UserInfoExtended;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.fico.xpress.utility.InsightAdminUriConstants.ADD_USER_URI;
import static com.fico.xpress.utility.InsightAdminUriConstants.DESC_OR_UPDATE_USER_URI;
import static com.fico.xpress.utility.InsightAdminUriConstants.LIST_USER_URI;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private RestService restService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testGetUserNamesFailure() {
        Command command = new Command();
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl("http://localhost:8860");
        command.setOperationType(OperationType.LIST);
        command.setInsightApplicationConfig(insightApplicationConfig);
        String url = insightApplicationConfig.getEndPoint(LIST_USER_URI);
        doReturn(null).when(restService).getListOfResource(Mockito.any(), eq(url)
            , Mockito.any());
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Empty list of users.");
        userServiceImpl.processUserCommand(command);
    }

    @Test
    public void testProcessUserCommandForList() {
        Command command = new Command();
        command.setOperationType(OperationType.LIST);
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl("http://localhost:8860");
        command.setInsightApplicationConfig(insightApplicationConfig);
        List<UserInfo> userList = new ArrayList<>();
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setUsername("admin");
        userList.add(userInfo1);
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setUsername("admin2");
        userList.add(userInfo2);
        String url = insightApplicationConfig.getEndPoint(LIST_USER_URI);
        doReturn(userList).when(restService).getListOfResource(Mockito.any(), eq(url)
            , Mockito.any());
        ListUsersOperationResult result = (ListUsersOperationResult) userServiceImpl.processUserCommand(command);
        assertThat(result.getUserNames(), is(Arrays.asList("admin", "admin2")));
    }

    @Test
    public void testProcessUserCommandForDescribe() {
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl("http://localhost:8860");
        Command command = new Command();
        command.setOperationType(OperationType.DESCRIBE);
        command.getArguments().put(InsightAdminCliConstants.TARGET_USER_NAME, "admin");
        command.setInsightApplicationConfig(insightApplicationConfig);
        UserInfoExtended userInfoExtended = new UserInfoExtended();
        List<AuthorityGroup> authorityGroups = new ArrayList<>();
        authorityGroups.add(new AuthorityGroup("AdvancedUser", false));
        authorityGroups.add(new AuthorityGroup("BasicUser", false));
        userInfoExtended.setAuthorityGroups(authorityGroups);
        userInfoExtended.setUsername("admin");
        userInfoExtended.setFirstName("Administrator");
        userInfoExtended.setLastName("User");

        String url = insightApplicationConfig.getEndPoint(DESC_OR_UPDATE_USER_URI);
        doReturn(userInfoExtended).when(restService).getResource(Mockito.any(), eq(url)
            , Mockito.any(), Mockito.any());
        DescribeUserOperationResult result = (DescribeUserOperationResult) userServiceImpl.processUserCommand(command);
        assertTrue("admin".equals(result.getUserInfoExtended().getUsername()));
        assertTrue("Administrator".equals(result.getUserInfoExtended().getFirstName()));
        assertTrue("User".equals(result.getUserInfoExtended().getLastName()));
        assertTrue("AdvancedUser".equalsIgnoreCase(((List<AuthorityGroup>) result.getUserInfoExtended().getAuthorityGroups()).get(0).getName()));
    }

    @Test
    public void testProcessUserCommandForAdd() {
        Command command = new Command();
        command.getArguments();
        command.setOperationType(OperationType.ADD);
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl("http://localhost:8860");
        command.setInsightApplicationConfig(insightApplicationConfig);
        UserInfoExtended userInfoExtended = new UserInfoExtended();
        userInfoExtended.setUsername("admin");
        userInfoExtended.setFirstName("Administrator");
        userInfoExtended.setLastName("User");

        String url = insightApplicationConfig.getEndPoint(ADD_USER_URI);
        doReturn(userInfoExtended).when(restService).getResource(Mockito.any(), eq(url)
            , Mockito.any(), Mockito.any());
        when(restService.addResource(Mockito.any()
            , Mockito.anyString()
            , Mockito.any(), Mockito.any()))
            .thenReturn(userInfoExtended);
        AddUserOperationResult result = (AddUserOperationResult) userServiceImpl.processUserCommand(command);
        assertTrue("admin".equals(result.getUserInfoExtended().getUsername()));
        assertTrue("Administrator".equals(result.getUserInfoExtended().getFirstName()));
        assertTrue("User".equals(result.getUserInfoExtended().getLastName()));
    }

    @Test
    public void testProcessUserCommandForUpdate() {
        Command command = new Command();
        command.setOperationType(OperationType.UPDATE);
        command.getArguments().put(InsightAdminCliConstants.EMAIL, "newUpdatedmail@fico.com");
        command.getArguments().put(InsightAdminCliConstants.TABLEAU_ENABLED, true);
        command.getArguments().put(InsightAdminCliConstants.LOCAL_ACCOUNT_DISABLE, false);
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl("http://localhost:8860");
        command.setInsightApplicationConfig(insightApplicationConfig);
        List<String> addAuthArgumentValues = Arrays.asList("Developer", "AdvancedUser");
        List<String> removeAuthArgumentValues = Arrays.asList("SystemAdministration");
        command.getArguments().put(InsightAdminCliConstants.ADD_AUTHORITY_GROUP, addAuthArgumentValues);
        command.getArguments().put(InsightAdminCliConstants.REMOVE_AUTHORITY_GROUP, removeAuthArgumentValues);

        List<String> addAppArgumentVlaues = Arrays.asList("Quick Start");
        List<String> removeAppArgumentVlaues = Arrays.asList("Conversion");
        command.getArguments().put(InsightAdminCliConstants.ADD_APP, addAppArgumentVlaues);
        command.getArguments().put(InsightAdminCliConstants.REMOVE_APP, removeAppArgumentVlaues);
        List<Project> templateProjects = new ArrayList<>();
        templateProjects.add(new Project(null, "Quick Start", false));
        templateProjects.add(new Project(null, "Conversion", true));

        List<AuthorityGroup> authorityGroupList = new ArrayList<>();
        authorityGroupList.add(new AuthorityGroup("Developer",true));
        authorityGroupList.add(new AuthorityGroup("AdvancedUser", false));
        authorityGroupList.add(new AuthorityGroup("SystemAdministration", true));

        UserInfoExtended existingUserInfoExtended = new UserInfoExtended("admin",
            "adminFirstName",
            "newAdminLastName",
            false,
            null,
            "abc@gmail.com",
            true,
            false,
            true,
            "password",
            null,
            authorityGroupList,
            templateProjects);

        String url = insightApplicationConfig.getEndPoint(DESC_OR_UPDATE_USER_URI);
        doReturn(existingUserInfoExtended).when(restService).getResource(Mockito.any(), eq(url)
            , Mockito.any(), Mockito.any());
        doReturn(existingUserInfoExtended).when(restService).updateResource(Mockito.any(), eq(url), Mockito.any(), Mockito.any(), Mockito.any());

        UserInfoExtended userInfoExtendedResponse = ((UpdateUserOperationResult) userServiceImpl.processUserCommand(command)).getUserInfoExtended();
        assertTrue("admin".equals(userInfoExtendedResponse.getUsername()));
        assertTrue("adminFirstName".equals(userInfoExtendedResponse.getFirstName()));
        assertTrue("newAdminLastName".equals(userInfoExtendedResponse.getLastName()));

        assertNull(userInfoExtendedResponse.getTableauUsername());
        assertTrue("newUpdatedmail@fico.com".equals(userInfoExtendedResponse.getEmail()));
        assertTrue(userInfoExtendedResponse.isEnabled());
        assertFalse(userInfoExtendedResponse.isLocked());


        assertTrue("password".equals(userInfoExtendedResponse.getPassword()));
        assertNull(userInfoExtendedResponse.getId());

        Collection<AuthorityGroup> authGroupList = userInfoExtendedResponse.getAuthorityGroups();
        assertThat(authGroupList, hasItem (hasProperty("name", is("Developer"))));
        assertThat(authGroupList, hasItem(hasProperty("name", is("AdvancedUser"))));
        assertThat(authGroupList, hasItem (hasProperty("selected", is(true))));

        Collection<Project> appResult = userInfoExtendedResponse.getProjects();
        assertThat(appResult, hasItem(hasProperty("name", is("Quick Start"))));
        assertThat(appResult, hasItem(hasProperty("selected", is(true))));

        assertTrue(userInfoExtendedResponse.getTableauEnabled());
        assertFalse(userInfoExtendedResponse.isLocalAccount());
    }
}
