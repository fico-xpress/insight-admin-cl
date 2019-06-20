/*
    Xpress Insight User Admin Cli
    ============================
    AuthorityGroupServiceImpl.java
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

import com.fico.xpress.operationresult.AddAuthorityGroupOperationResult;
import com.fico.xpress.operationresult.DescribeAuthorityGroupOperationResult;
import com.fico.xpress.operationresult.ListAuthorityGroupsOperationResult;
import com.fico.xpress.operationresult.OperationResult;
import com.fico.xpress.operationresult.UpdateAuthorityGroupOperationResult;
import com.fico.xpress.vos.AuthorityGroup;
import com.fico.xpress.vos.AuthorityGroupExtended;
import com.fico.xpress.vos.AuthorityListItem;
import com.fico.xpress.vos.Command;
import com.fico.xpress.vos.InsightApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.fico.xpress.utility.InsightAdminCliConstants.ADD_AUTHORITY_GROUP_AUTHORITIES;
import static com.fico.xpress.utility.InsightAdminCliConstants.ADD_AUTHORITY_GROUP_DESC;
import static com.fico.xpress.utility.InsightAdminCliConstants.ADD_AUTHORITY_GROUP_NAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.REMOVE_AUTHORITY_GROUP_AUTHORITIES;
import static com.fico.xpress.utility.InsightAdminUriConstants.ADD_AUTHGRP_URI;
import static com.fico.xpress.utility.InsightAdminUriConstants.DESC_OR_UPDATE_AUTHGRP_URI;
import static com.fico.xpress.utility.InsightAdminUriConstants.LIST_AUTHGRP_URI;

@Component
public class AuthorityGroupServiceImpl implements AuthorityGroupService {

    @Autowired
    private  RestService restService;

    /**
     * To get the list of all authority groups from an insight server.
     *
     * @param insightApplicationConfig
     */
    private ListAuthorityGroupsOperationResult getAuthorityGroupDetails(InsightApplicationConfig insightApplicationConfig) {
        String url = insightApplicationConfig.getEndPoint(LIST_AUTHGRP_URI);
        List<AuthorityGroup> resultList = restService.getListOfResource(insightApplicationConfig,
            url, new ParameterizedTypeReference<List<AuthorityGroup>>(){});
        ListAuthorityGroupsOperationResult listAuthorityGroupsOperationResult = new ListAuthorityGroupsOperationResult(resultList);
        return listAuthorityGroupsOperationResult;
    }

    /**
     * To process the operation present in the command line.
     *
     * @param command
     */
    @Override
    public OperationResult processAuthorityGroupCommand(Command command) {
        OperationResult operationResult = null;
        switch (command.getOperationType()) {
            case LIST:
                operationResult = getAuthorityGroupDetails(command.getInsightApplicationConfig());
                break;
            case ADD:
                operationResult = addAuthorityGroup(command);
                break;
            case UPDATE:
                operationResult = updateAuthorityGroup(command);
                break;
            case DESCRIBE:
                operationResult = getAuthorityGroupDescription(command.getInsightApplicationConfig(), command.getStringArgumentValue("an"));

        }
        return operationResult;
    }


    /**
     * To get the template user having the list of authorities.
     *
     * @param insightApplicationConfig
     */
    private AuthorityGroupExtended getTemplateAuthorityGroup(InsightApplicationConfig insightApplicationConfig) {
        AuthorityGroupExtended authorityGroupExtended;
        String url = insightApplicationConfig.getEndPoint(ADD_AUTHGRP_URI);
        authorityGroupExtended = restService.getResource(insightApplicationConfig,
            url, Collections.emptyMap(), new ParameterizedTypeReference<AuthorityGroupExtended>(){});
        return authorityGroupExtended;
    }

    /**
     * To add a new authority group to an insight server.
     *
     * @param command
     */
    private AddAuthorityGroupOperationResult addAuthorityGroup(Command command) {
        String url = command.getInsightApplicationConfig().getEndPoint(ADD_AUTHGRP_URI);
        AuthorityGroupExtended templateAuthorityGroup = getTemplateAuthorityGroup(command.getInsightApplicationConfig());
        AuthorityGroupExtended newAuthorityGroup = new AuthorityGroupExtended(command.getStringArgumentValue(ADD_AUTHORITY_GROUP_NAME),
            command.getStringArgumentValue(ADD_AUTHORITY_GROUP_DESC),
            populateAndValidateAuthorityList(command.getMultipleArgumentValue(ADD_AUTHORITY_GROUP_AUTHORITIES), templateAuthorityGroup.getAuthorities(), null));

        AuthorityGroupExtended authorityGroupExtendedResponse = restService.addResource(command.getInsightApplicationConfig(),
            url, newAuthorityGroup, new ParameterizedTypeReference<AuthorityGroupExtended>(){});
        AddAuthorityGroupOperationResult addAuthorityGroupOperationResult = new AddAuthorityGroupOperationResult(authorityGroupExtendedResponse);
        return addAuthorityGroupOperationResult;
    }


    /**
     * Utility Method to process authorities while adding or updating an authority group.
     *
     * @param addCommandAuthoritiesArgs
     * @param existingAuthorities
     * @param removeCommandAuthoritiesArgs
     */
    private Set<AuthorityListItem> populateAndValidateAuthorityList(List<String> addCommandAuthoritiesArgs,
                                                                    Collection<AuthorityListItem> existingAuthorities,
                                                                    List<String> removeCommandAuthoritiesArgs) {
        return MultipleValueArgValidationService.populateAndValidateMultipleValueArgs(addCommandAuthoritiesArgs,
                existingAuthorities, removeCommandAuthoritiesArgs, AuthorityListItem::new,
                AuthorityListItem::getDisplayName, "Authorities");
    }

    /**
     * To update an existing authority group to an insight server.
     *
     * @param command
     */
    private UpdateAuthorityGroupOperationResult updateAuthorityGroup(Command command) {
        String url = command.getInsightApplicationConfig().getEndPoint(DESC_OR_UPDATE_AUTHGRP_URI);

        AuthorityGroupExtended authorityGroupExtended = getAuthorityGroupDescription(command.getInsightApplicationConfig(),
                command.getStringArgumentValue(ADD_AUTHORITY_GROUP_NAME)).getAuthorityGroupExtended();
        populateExistingAuthorityGroup(command, authorityGroupExtended);
        Map<String, String> pathParam = new HashMap<>();
        pathParam.put("authgrpname", (String)command.getArguments().get(ADD_AUTHORITY_GROUP_NAME));
        AuthorityGroupExtended authorityGroupExtendedUpdated = restService.updateResource(command.getInsightApplicationConfig(),
                url, pathParam, authorityGroupExtended, new ParameterizedTypeReference<AuthorityGroupExtended>() {});
        UpdateAuthorityGroupOperationResult updateAuthorityGroupOperationResult = new UpdateAuthorityGroupOperationResult(authorityGroupExtendedUpdated);
        return updateAuthorityGroupOperationResult;
    }

    /**
     * Utility method to take new value or else populate with existing value of an authority group.
     *
     * @param command
     * @param authorityGroupExtended
     */
    private AuthorityGroupExtended populateExistingAuthorityGroup(Command command, AuthorityGroupExtended authorityGroupExtended) {
        authorityGroupExtended.setDescription((String)getUpdatedFieldValue(ADD_AUTHORITY_GROUP_DESC,
                command.getArguments(), authorityGroupExtended.getDescription()));
        if (command.getArguments().containsKey(ADD_AUTHORITY_GROUP_AUTHORITIES) ||
                command.getArguments().containsKey(REMOVE_AUTHORITY_GROUP_AUTHORITIES)) {
            authorityGroupExtended.setAuthorities(populateAndValidateAuthorityList(
                    command.getMultipleArgumentValue(ADD_AUTHORITY_GROUP_AUTHORITIES),
                    authorityGroupExtended.getAuthorities(), command.getMultipleArgumentValue(REMOVE_AUTHORITY_GROUP_AUTHORITIES)));
        }
        return authorityGroupExtended;
    }


    /**
     * Utility method to get a new value from command line or else return the old field value.
     *
     * @param option
     * @param arguments
     * @param fieldValue
     */
    private Object getUpdatedFieldValue(String option, Map<String, Object> arguments, Object fieldValue) {
        if (arguments.containsKey(option) && arguments.get(option) != null) {
            return arguments.get(option);
        }
        return fieldValue;
    }

    /**
     * To get description of a specific authority group from an insight server.
     *
     * @param insightApplicationConfig
     * @param targetAuthorityGroupName
     */
    private DescribeAuthorityGroupOperationResult getAuthorityGroupDescription(InsightApplicationConfig insightApplicationConfig, String targetAuthorityGroupName) {
        String url = insightApplicationConfig.getEndPoint(DESC_OR_UPDATE_AUTHGRP_URI);
        Map<String, String> pathParam = new HashMap<>();
        pathParam.put("authgrpname", targetAuthorityGroupName);
        AuthorityGroupExtended authorityGroupExtended = restService.getResource(insightApplicationConfig,
                url, pathParam, new ParameterizedTypeReference<AuthorityGroupExtended>() {
                });
        DescribeAuthorityGroupOperationResult describeAuthorityGroupOperationResult = new DescribeAuthorityGroupOperationResult(authorityGroupExtended);
        return describeAuthorityGroupOperationResult;
    }
}
