/*
    Xpress Insight User Admin Cli
    ============================
    AuthorityGroupServiceImplTest.java
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
import com.fico.xpress.operationresult.AddAuthorityGroupOperationResult;
import com.fico.xpress.operationresult.DescribeAuthorityGroupOperationResult;
import com.fico.xpress.operationresult.ListAuthorityGroupsOperationResult;
import com.fico.xpress.operationresult.UpdateAuthorityGroupOperationResult;
import com.fico.xpress.utility.InsightAdminCliConstants;
import com.fico.xpress.vos.AuthorityGroup;
import com.fico.xpress.vos.AuthorityGroupExtended;
import com.fico.xpress.vos.AuthorityListItem;
import com.fico.xpress.vos.Command;
import com.fico.xpress.vos.InsightApplicationConfig;
import com.fico.xpress.vos.OperationType;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.INSIGHT_HOST;
import static com.fico.xpress.utility.InsightAdminCliConstants.ADD_AUTHORITY_GROUP_AUTHORITIES;
import static com.fico.xpress.utility.InsightAdminCliConstants.ADD_AUTHORITY_GROUP_DESC;
import static com.fico.xpress.utility.InsightAdminCliConstants.REMOVE_AUTHORITY_GROUP_AUTHORITIES;
import static com.fico.xpress.utility.InsightAdminUriConstants.ADD_AUTHGRP_URI;
import static com.fico.xpress.utility.InsightAdminUriConstants.DESC_OR_UPDATE_AUTHGRP_URI;
import static com.fico.xpress.utility.InsightAdminUriConstants.LIST_AUTHGRP_URI;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorityGroupServiceImplTest {

    @InjectMocks
    private AuthorityGroupServiceImpl authorityGroupServiceImpl;

    @Mock
    private RestService restService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testProcessAuthorityGroupCommandForList() {
        Command command = new Command();
        command.setOperationType(OperationType.LIST);
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        command.setInsightApplicationConfig(insightApplicationConfig);
        insightApplicationConfig.setHostUrl("");
        List<AuthorityGroup> authorityGroupList = new ArrayList<>();
        authorityGroupList.add(new AuthorityGroup("BasicUser",true));
        authorityGroupList.add(new AuthorityGroup("AdvancedUser",true));

        String url = insightApplicationConfig.getEndPoint(LIST_AUTHGRP_URI);
        doReturn(authorityGroupList).when(restService).getListOfResource(Mockito.any(), eq(url)
            , Mockito.any());
        ListAuthorityGroupsOperationResult listAuthorityGroupsOperationResult = (ListAuthorityGroupsOperationResult) authorityGroupServiceImpl.processAuthorityGroupCommand(command);
        List<AuthorityGroup> authorityGroupListResult = listAuthorityGroupsOperationResult.getAuthorityGroups();
        assertTrue(authorityGroupListResult.get(0).getName().equalsIgnoreCase("BasicUser"));
    }

    @Test
    public void testProcessAuthorityGroupCommandForDescribe() {
        Command command = new Command();
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        command.setInsightApplicationConfig(insightApplicationConfig);
        command.setOperationType(OperationType.DESCRIBE);
        command.getArguments();
        AuthorityGroupExtended authorityGroupExtended = new AuthorityGroupExtended();
        List<AuthorityListItem> authorities = new ArrayList<>();
        authorities.add(new AuthorityListItem("DEVELOPER", true));
        authorities.add(new AuthorityListItem("APP_ALL", false));
        authorityGroupExtended.setAuthorities(authorities);
        authorityGroupExtended.setName("AdvancedUser");
        authorityGroupExtended.setDescription("Authority set for advanced users");
        String url = insightApplicationConfig.getEndPoint(DESC_OR_UPDATE_AUTHGRP_URI);
        doReturn(authorityGroupExtended).when(restService).getResource(Mockito.any(), eq(url)
            , Mockito.any(), Mockito.any());
        DescribeAuthorityGroupOperationResult result = (DescribeAuthorityGroupOperationResult) authorityGroupServiceImpl.processAuthorityGroupCommand(command);
        AuthorityGroupExtended authorityGroupExtendedResult = result.getAuthorityGroupExtended();
        assertTrue("AdvancedUser".equals(authorityGroupExtendedResult.getName()));
        assertTrue("Authority set for advanced users".equals(authorityGroupExtendedResult.getDescription()));
        assertTrue("DEVELOPER".equalsIgnoreCase(((List<AuthorityListItem>) authorityGroupExtendedResult.getAuthorities()).get(0).getDisplayName()));
    }

    @Test
    public void testProcessAuthorityGroupCommandForUpdate() {
        Command command = new Command();
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        command.setInsightApplicationConfig(insightApplicationConfig);
        command.setOperationType(OperationType.UPDATE);
        command.getArguments().put(ADD_AUTHORITY_GROUP_DESC, "new description.");
        command.getArguments().put(InsightAdminCliConstants.ADD_AUTHORITY_GROUP_AUTHORITIES, Arrays.asList("APP_ALL"));
        command.getArguments().put(InsightAdminCliConstants.REMOVE_AUTHORITY_GROUP_AUTHORITIES, Arrays.asList("DEVELOPER"));

        AuthorityGroupExtended authorityGroupExtended = new AuthorityGroupExtended("Basic Authority Group",
            "This is test Authority Group.",
            new ArrayList<>(Arrays.asList(new AuthorityListItem("DEVELOPER", true), new AuthorityListItem(("APP_ALL"), false))));

        String url = insightApplicationConfig.getEndPoint(DESC_OR_UPDATE_AUTHGRP_URI);
        doReturn(authorityGroupExtended).when(restService).getResource(Mockito.any(),
            eq(url)
            , Mockito.any(), Mockito.any());
        when(restService.updateResource(Mockito.any(), eq(url), Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(authorityGroupExtended);
        UpdateAuthorityGroupOperationResult updateAuthorityGroupsOperationResult = (UpdateAuthorityGroupOperationResult) authorityGroupServiceImpl.processAuthorityGroupCommand(command);
        assertEquals("Basic Authority Group", updateAuthorityGroupsOperationResult.getAuthorityGroupExtended().getName());
        assertEquals("new description.", updateAuthorityGroupsOperationResult.getAuthorityGroupExtended().getDescription());
        Set<AuthorityListItem> authoritySet = (Set) updateAuthorityGroupsOperationResult.getAuthorityGroupExtended().getAuthorities();
        assertThat(authoritySet, hasItems(
            new AuthorityListItem("APP_ALL", true)
        ));
        assertEquals(1, authoritySet.size());
    }

    @Test
    public void testAddAuthorityGroup() {
        Command command = new Command();
        command.setOperationType(OperationType.ADD);
        command.getArguments();
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        command.setInsightApplicationConfig(insightApplicationConfig);
        AuthorityGroupExtended newAuthorityGroup = new AuthorityGroupExtended("Basic Authority Group",
                "This is test Authority Group.",
                new ArrayList<>(Arrays.asList(new AuthorityListItem("DEVELOPER",true))));
        String url = insightApplicationConfig.getEndPoint(ADD_AUTHGRP_URI);

        doReturn(newAuthorityGroup).when(restService).getResource(Mockito.any(), eq(url)
            , Mockito.any(), Mockito.any());

        when(restService.addResource(Mockito.any()
                , Mockito.anyString()
                , Mockito.any(), Mockito.any()))
                .thenReturn(newAuthorityGroup);
        AddAuthorityGroupOperationResult addAuthorityGroupOperationResult = (AddAuthorityGroupOperationResult) authorityGroupServiceImpl.processAuthorityGroupCommand(command);
        AuthorityGroupExtended authorityGroupExtended = addAuthorityGroupOperationResult.getAuthorityGroupExtended();
        assertTrue("Basic Authority Group".equalsIgnoreCase(authorityGroupExtended.getName()));
        assertTrue("This is test Authority Group.".equals(authorityGroupExtended.getDescription()));
        List<AuthorityListItem> authorityListItems = (List) authorityGroupExtended.getAuthorities();
        assertTrue("DEVELOPER".equals(authorityListItems.get(0).getDisplayName()));
    }

    public void testPopulateAndValidateAuthorityListForAddOperation() {
        Command command = new Command();
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        command.setInsightApplicationConfig(insightApplicationConfig);
        List<String> addArgValues = Arrays.asList("DEVELOPER");
        command.setOperationType(OperationType.ADD);
        command.getArguments().put(ADD_AUTHORITY_GROUP_AUTHORITIES, addArgValues);
        AuthorityGroupExtended newAuthorityGroup = new AuthorityGroupExtended("Basic Authority Group",
            "This is test Authority Group.",
            new ArrayList<>(Arrays.asList(new AuthorityListItem("DEVELOPER", false))));
        String url = insightApplicationConfig.getEndPoint(ADD_AUTHGRP_URI);
        doReturn(newAuthorityGroup).when(restService).getResource(Mockito.any(), eq(url)
            , Mockito.any(), Mockito.any());

        when(restService.addResource(Mockito.any()
            , Mockito.anyString()
            , Mockito.any(), Mockito.any()))
            .thenReturn(newAuthorityGroup);


        AddAuthorityGroupOperationResult addAuthorityGroupOperationResult = (AddAuthorityGroupOperationResult) authorityGroupServiceImpl.processAuthorityGroupCommand(command);
        Collection<AuthorityListItem> authList = addAuthorityGroupOperationResult.getAuthorityGroupExtended().getAuthorities();

        assertThat(authList, hasItem(hasProperty("displayName", is("DEVELOPER"))));
        assertThat(authList, hasItem (hasProperty("selected", is(true))));
    }

    @Test
    public void testPopulateAndValidateAuthorityListForUpdateOperation() {
        Command command = new Command();
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        command.setInsightApplicationConfig(insightApplicationConfig);
        command.setOperationType(OperationType.UPDATE);
        List<String> addArgumentValues = Arrays.asList("DEVELOPER", "APP_ALL");
        List<String> removeArgumentValues = Arrays.asList("APP_EDIT");
        List<AuthorityListItem> existingAuthList = new ArrayList<>();

        existingAuthList.add(new AuthorityListItem("DEVELOPER", false));
        existingAuthList.add(new AuthorityListItem("APP_ALL", false));
        existingAuthList.add(new AuthorityListItem("APP_EDIT", true));

        command.getArguments().put(ADD_AUTHORITY_GROUP_AUTHORITIES, addArgumentValues);
        command.getArguments().put(REMOVE_AUTHORITY_GROUP_AUTHORITIES, removeArgumentValues);

        AuthorityGroupExtended authorityGroupExtended = new AuthorityGroupExtended("Basic Authority Group",
            "This is test Authority Group.",
            existingAuthList);
        String url = insightApplicationConfig.getEndPoint(DESC_OR_UPDATE_AUTHGRP_URI);
        doReturn(authorityGroupExtended).when(restService).getResource(Mockito.any(),
            eq(url)
            , Mockito.any(), Mockito.any());
        when(restService.updateResource(Mockito.any(), eq(url), Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(authorityGroupExtended);
        UpdateAuthorityGroupOperationResult updateAuthorityGroupsOperationResult = (UpdateAuthorityGroupOperationResult) authorityGroupServiceImpl.processAuthorityGroupCommand(command);

        Collection<AuthorityListItem> authList = updateAuthorityGroupsOperationResult.getAuthorityGroupExtended().getAuthorities();
        assertThat(authList, hasItem (hasProperty("displayName", is("DEVELOPER"))));
        assertThat(authList, hasItem (hasProperty("displayName", is("APP_ALL"))));
        assertThat(authList, hasItems(
            new AuthorityListItem("DEVELOPER",true),
            new AuthorityListItem("APP_ALL", true)
        ));
    }

    @Test
    public void testPopulateAndValidateAuthorityListForInvalidAgrumentListForAdd() {
        Command command = new Command();
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        command.setInsightApplicationConfig(insightApplicationConfig);
        List<String> addArgValues = Arrays.asList("DEVELOPER1");
        List<AuthorityListItem> existingAuthList = new ArrayList<>();
        existingAuthList.add(new AuthorityListItem("DEVELOPER", false));
        command.setOperationType(OperationType.ADD);
        command.getArguments().put(ADD_AUTHORITY_GROUP_AUTHORITIES, addArgValues);
        AuthorityGroupExtended newAuthorityGroup = new AuthorityGroupExtended("Basic Authority Group",
            "This is test Authority Group.",
            new ArrayList<>(Arrays.asList(new AuthorityListItem("DEVELOPER", false))));
        String url = insightApplicationConfig.getEndPoint(ADD_AUTHGRP_URI);
        doReturn(newAuthorityGroup).when(restService).getResource(Mockito.any(), eq(url)
            , Mockito.any(), Mockito.any());

        when(restService.addResource(Mockito.any()
            , Mockito.anyString()
            , Mockito.any(), Mockito.any()))
            .thenReturn(newAuthorityGroup);
        expectedException.expectMessage("Invalid Authorities [DEVELOPER1].");
        expectedException.expect(ApplicationException.class);
        authorityGroupServiceImpl.processAuthorityGroupCommand(command);
    }

    @Test
    public void testPopulateAndValidateAuthorityListWhenExistingAuthorityListIsEmptyForAdd() {
        Command command = new Command();
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        command.setInsightApplicationConfig(insightApplicationConfig);
        List<String> addArgValues = Arrays.asList("DEVELOPER");
        List<AuthorityListItem> existingAuthList = new ArrayList<>();
        command.setOperationType(OperationType.ADD);
        command.getArguments().put(ADD_AUTHORITY_GROUP_AUTHORITIES, addArgValues);
        AuthorityGroupExtended newAuthorityGroup = new AuthorityGroupExtended("Basic Authority Group",
            "This is test Authority Group.",
            existingAuthList);
        String url = insightApplicationConfig.getEndPoint(ADD_AUTHGRP_URI);
        doReturn(newAuthorityGroup).when(restService).getResource(Mockito.any(), eq(url)
            , Mockito.any(), Mockito.any());

        when(restService.addResource(Mockito.any()
            , Mockito.anyString()
            , Mockito.any(), Mockito.any()))
            .thenReturn(newAuthorityGroup);

        expectedException.expectMessage("Currently no valid Authorities exists, Please try Add/Update without any Authorities.");
        expectedException.expect(ApplicationException.class);
        authorityGroupServiceImpl.processAuthorityGroupCommand(command);
    }

    @Test
    public void testValidateAddAndRemoveArgsForAuthoritiesForUpdate() {
        List<String> addArguments = Arrays.asList("DEVELOPER");
        List<String> removeArguments = Arrays.asList("DEVELOPER");
        List<AuthorityListItem> existingAuthList = new ArrayList<>();
        existingAuthList.add(new AuthorityListItem("DEVELOPER", true));

        Command command = new Command();
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        command.setInsightApplicationConfig(insightApplicationConfig);
        command.setOperationType(OperationType.UPDATE);
        command.getArguments().put(ADD_AUTHORITY_GROUP_DESC, "new description.");
        command.getArguments().put(InsightAdminCliConstants.ADD_AUTHORITY_GROUP_AUTHORITIES, addArguments);
        command.getArguments().put(InsightAdminCliConstants.REMOVE_AUTHORITY_GROUP_AUTHORITIES, removeArguments);

        AuthorityGroupExtended authorityGroupExtended = new AuthorityGroupExtended("Basic Authority Group",
            "This is test Authority Group.",
            existingAuthList);


        String url = insightApplicationConfig.getEndPoint(DESC_OR_UPDATE_AUTHGRP_URI);
        doReturn(authorityGroupExtended).when(restService).getResource(Mockito.any(),
            eq(url)
            , Mockito.any(), Mockito.any());
        when(restService.updateResource(Mockito.any(), eq(url), Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(authorityGroupExtended);

        expectedException.expectMessage("Duplicate Authorities exists in add and remove Authorities arguments.");
        expectedException.expect(ApplicationException.class);
        authorityGroupServiceImpl.processAuthorityGroupCommand(command);
    }

    @Test
    public void testValidateRemoveAuthorityArgsForUpdate() {
        List<String> removeArguments = new ArrayList<>();
        removeArguments.add("DEVELOPER1");
        Set<AuthorityListItem> existingAuthorities = new HashSet<>();
        existingAuthorities.add(new AuthorityListItem("DEVELOPER",true));

        Command command = new Command();
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        command.setInsightApplicationConfig(insightApplicationConfig);
        command.setOperationType(OperationType.UPDATE);
        command.getArguments().put(InsightAdminCliConstants.REMOVE_AUTHORITY_GROUP_AUTHORITIES, removeArguments);

        AuthorityGroupExtended authorityGroupExtended = new AuthorityGroupExtended("Basic Authority Group",
            "This is test Authority Group.",
            existingAuthorities);


        String url = insightApplicationConfig.getEndPoint(DESC_OR_UPDATE_AUTHGRP_URI);
        doReturn(authorityGroupExtended).when(restService).getResource(Mockito.any(),
            eq(url)
            , Mockito.any(), Mockito.any());
        when(restService.updateResource(Mockito.any(), eq(url), Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(authorityGroupExtended);

        expectedException.expectMessage("Invalid Authorities [DEVELOPER1].");
        expectedException.expect(ApplicationException.class);
        authorityGroupServiceImpl.processAuthorityGroupCommand(command);
    }
}
