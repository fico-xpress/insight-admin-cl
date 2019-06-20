/*
    Xpress Insight User Admin Cli
    ============================
    UserServiceImpl.java
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
import com.fico.xpress.operationresult.OperationResult;
import com.fico.xpress.operationresult.UpdateUserOperationResult;
import com.fico.xpress.utility.InsightAdminCliConstants;
import com.fico.xpress.vos.AuthorityGroup;
import com.fico.xpress.vos.Command;
import com.fico.xpress.vos.InsightApplicationConfig;
import com.fico.xpress.vos.Project;
import com.fico.xpress.vos.UserInfo;
import com.fico.xpress.vos.UserInfoExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fico.xpress.utility.InsightAdminCliConstants.ADD_APP;
import static com.fico.xpress.utility.InsightAdminCliConstants.ADD_AUTHORITY_GROUP;
import static com.fico.xpress.utility.InsightAdminCliConstants.DISABLED;
import static com.fico.xpress.utility.InsightAdminCliConstants.EMAIL;
import static com.fico.xpress.utility.InsightAdminCliConstants.ENABLED;
import static com.fico.xpress.utility.InsightAdminCliConstants.FIRST_NAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.LAST_NAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.LOCAL_ACCOUNT_DISABLE;
import static com.fico.xpress.utility.InsightAdminCliConstants.LOCAL_ACCOUNT_ENABLE;
import static com.fico.xpress.utility.InsightAdminCliConstants.LOCKED;
import static com.fico.xpress.utility.InsightAdminCliConstants.REMOVE_APP;
import static com.fico.xpress.utility.InsightAdminCliConstants.REMOVE_AUTHORITY_GROUP;
import static com.fico.xpress.utility.InsightAdminCliConstants.TABLEAU_DISABLED;
import static com.fico.xpress.utility.InsightAdminCliConstants.TABLEAU_ENABLED;
import static com.fico.xpress.utility.InsightAdminCliConstants.TABLEAU_USERNAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.TARGET_PASSWORD;
import static com.fico.xpress.utility.InsightAdminCliConstants.TARGET_USER_NAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.UNLOCKED;
import static com.fico.xpress.utility.InsightAdminUriConstants.ADD_USER_URI;
import static com.fico.xpress.utility.InsightAdminUriConstants.DESC_OR_UPDATE_USER_URI;
import static com.fico.xpress.utility.InsightAdminUriConstants.LIST_USER_URI;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private  RestService restService;

    /**
     * Utility method to get details of all users from an insight server.
     *
     * @param insightApplicationConfig
     */
    private List<UserInfo> getListOfUsers(InsightApplicationConfig insightApplicationConfig) {
        String url = insightApplicationConfig.getEndPoint(LIST_USER_URI);
        List<UserInfo> resultList = restService.getListOfResource(insightApplicationConfig,
            url, new ParameterizedTypeReference<List<UserInfo>>(){});
        return resultList;
    }

    /**
     * To get the list of all usernames from an insight server.
     *
     * @param insightApplicationConfig
     */
    private ListUsersOperationResult getUserNames(InsightApplicationConfig insightApplicationConfig) {
        List<UserInfo> userList = getListOfUsers(insightApplicationConfig);
        if (userList == null || userList.isEmpty()) {
            throw new ApplicationException("Empty list of users.");
        }
        List<String> userNames = userList.stream().map(UserInfo::getUsername).collect(Collectors.toList());
        ListUsersOperationResult listUsersOperationResult = new ListUsersOperationResult();
        listUsersOperationResult.setUserNames(userNames);
        return listUsersOperationResult;
    }

    /**
     * To get description of a specific user from an insight server.
     *
     * @param insightApplicationConfig
     */
    private DescribeUserOperationResult getUserDescription(InsightApplicationConfig insightApplicationConfig, String targetUserName) {
        UserInfoExtended userInfoExtended;
        String url = insightApplicationConfig.getEndPoint(DESC_OR_UPDATE_USER_URI);
        Map<String, String> pathParam = new HashMap<>();
        pathParam.put("username", targetUserName);
        userInfoExtended = restService.getResource(insightApplicationConfig,
            url, pathParam, new ParameterizedTypeReference<UserInfoExtended>(){});
        DescribeUserOperationResult describeUserOperationResult = new DescribeUserOperationResult();
        describeUserOperationResult.setUserInfoExtended(userInfoExtended);
        return describeUserOperationResult;
    }

    /**
     * To process the operation present in the command line.
     *
     * @param command
     */
    @Override
    public OperationResult processUserCommand(Command command) {
        OperationResult operationResult = null;
        switch (command.getOperationType()) {
            case LIST:
                operationResult = getUserNames(command.getInsightApplicationConfig());
                break;
            case DESCRIBE:
                operationResult = getUserDescription(command.getInsightApplicationConfig(), command.getStringArgumentValue(TARGET_USER_NAME));
                break;
            case ADD:
                operationResult = addUser(command);
                break;
            case UPDATE:
                operationResult = updateUser(command);
                break;
        }
        return operationResult;
    }

    /**
     * To add a new user to an insight server.
     *
     * @param command
     */
    private AddUserOperationResult addUser(Command command) {
        InsightApplicationConfig insightApplicationConfig = command.getInsightApplicationConfig();
        String url = insightApplicationConfig.getEndPoint(ADD_USER_URI);
        UserInfoExtended templateUser = getTemplateUser(insightApplicationConfig);
        UserInfoExtended newUser = new UserInfoExtended(command);
        newUser.setAuthorityGroups(populateAndValidateAuthorityGroups(command.getMultipleArgumentValue(ADD_AUTHORITY_GROUP),
                templateUser.getAuthorityGroups(), null));
        newUser.setProjects(populateAndValidateApps(command.getMultipleArgumentValue(ADD_APP), templateUser.getProjects(), null));

        UserInfoExtended userInfoExtendedResponse = restService.addResource(insightApplicationConfig,
                url, newUser, new ParameterizedTypeReference<UserInfoExtended>(){});
        AddUserOperationResult addUserOperationResult;
        addUserOperationResult = new AddUserOperationResult(userInfoExtendedResponse);
        return addUserOperationResult;
    }

    /**
     * Utility method to get a new value from command line or else return the old field value.
     *
     * @param option
     * @param command
     * @param fieldValue
     */
    private String getUpdatedStringFieldValue(String option, Command command, String fieldValue) {
        if (command.getArguments().containsKey(option) && command.getArguments().get(option) != null) {
            return command.getStringArgumentValue(option);
        }
        return fieldValue;
    }

    /**
     * Utility method to get a new Boolean field value from command line or else return old field value.
     *
     * @param trueOption
     * @param falseOption
     * @param command
     * @param fieldValue
     */
    private boolean getUpdatedBooleanFieldValue(String trueOption, String falseOption, Command command, boolean fieldValue) {
        if (command.getArguments().containsKey(trueOption) && command.getArguments().get(trueOption) != null) {
            return command.getBooleanArgumentValue(trueOption);
        } else if (command.getArguments().containsKey(falseOption) && command.getArguments().get(falseOption) != null) {
            return command.getBooleanArgumentValue(falseOption);
        }
        return fieldValue;
    }

    /**
     * To update a specific user to an insight server.
     *
     * @param command the command prepared from commandline
     */
    private UpdateUserOperationResult updateUser(Command command) {
        UpdateUserOperationResult updateUserOperationResult;
        InsightApplicationConfig insightApplicationConfig = command.getInsightApplicationConfig();
        String url = insightApplicationConfig.getEndPoint(DESC_OR_UPDATE_USER_URI);
        DescribeUserOperationResult describeUserOperationResult = getUserDescription(insightApplicationConfig, command.getStringArgumentValue(InsightAdminCliConstants.TARGET_USER_NAME));
        UserInfoExtended existingUserInfo = describeUserOperationResult.getUserInfoExtended();
        populateUserInfoExtended(command, existingUserInfo);
        Map<String, String> pathParam = new HashMap<>();
        pathParam.put("username", command.getStringArgumentValue(TARGET_USER_NAME));
        UserInfoExtended userInfoExtendedResponse = restService.updateResource(insightApplicationConfig,
            url, pathParam, existingUserInfo, new ParameterizedTypeReference<UserInfoExtended>(){});
        updateUserOperationResult = new UpdateUserOperationResult(userInfoExtendedResponse);
        return updateUserOperationResult;
    }

    /**
     * Utility method used while updating a specific user.
     *
     * @param command
     * @param existingUserInfo
     */
    private void populateUserInfoExtended(Command command, UserInfoExtended existingUserInfo) {
        existingUserInfo.setFirstName(getUpdatedStringFieldValue(FIRST_NAME, command, existingUserInfo.getFirstName()));
        existingUserInfo.setLastName(getUpdatedStringFieldValue(LAST_NAME, command, existingUserInfo.getLastName()));
        existingUserInfo.setTableauEnabled(getUpdatedBooleanFieldValue(TABLEAU_ENABLED, TABLEAU_DISABLED, command, existingUserInfo.isTableauEnabled()));
        existingUserInfo.setTableauUsername(getUpdatedStringFieldValue(TABLEAU_USERNAME, command, existingUserInfo.getTableauUsername()));
        existingUserInfo.setEmail(getUpdatedStringFieldValue(EMAIL, command, existingUserInfo.getEmail()));
        existingUserInfo.setEnabled(getUpdatedBooleanFieldValue(ENABLED, DISABLED, command, existingUserInfo.isEnabled()));
        existingUserInfo.setLocked(getUpdatedBooleanFieldValue(LOCKED, UNLOCKED, command, existingUserInfo.isLocked()));
        existingUserInfo.setLocalAccount(getUpdatedBooleanFieldValue(LOCAL_ACCOUNT_ENABLE, LOCAL_ACCOUNT_DISABLE, command, existingUserInfo.isLocalAccount()));
        existingUserInfo.setPassword(getUpdatedStringFieldValue(TARGET_PASSWORD, command, existingUserInfo.getPassword()));
        existingUserInfo.setId(existingUserInfo.getId());
        //if ag is present in command line then the existing ag of the user will be replaced with the ag present in command line.
        if (command.getArguments().containsKey(ADD_AUTHORITY_GROUP) || command.getArguments().containsKey(REMOVE_AUTHORITY_GROUP)) {
            existingUserInfo.setAuthorityGroups(populateAndValidateAuthorityGroups(command.getMultipleArgumentValue(ADD_AUTHORITY_GROUP), existingUserInfo.getAuthorityGroups(), command.getMultipleArgumentValue(REMOVE_AUTHORITY_GROUP)));
        }
        //if app is present in command line then the existing app of the user will be replaced with the app present in command line.
        if (command.getArguments().containsKey(ADD_APP) || command.getArguments().containsKey(REMOVE_APP)) {
            existingUserInfo.setProjects(populateAndValidateApps(command.getMultipleArgumentValue(ADD_APP), existingUserInfo.getProjects(), command.getMultipleArgumentValue(REMOVE_APP)));
        }
    }

    /**
     * Gets a blank user object but with the authority groups and apps that are available.
     *
     * @param insightApplicationConfig
     * @return blank user object.
     */
    private UserInfoExtended getTemplateUser(InsightApplicationConfig insightApplicationConfig) {
        UserInfoExtended userInfoExtended;
        String url = insightApplicationConfig.getEndPoint(ADD_USER_URI);
        userInfoExtended = restService.getResource(insightApplicationConfig,
            url, Collections.emptyMap(), new ParameterizedTypeReference<UserInfoExtended>(){});
        return userInfoExtended;
    }


    /**
     * Utility Method to process authority groups while adding or updating user.
     *
     * @param addArgumentValues
     * @param existingCollection
     * @param removeArgumentValues
     */
    private Set<AuthorityGroup> populateAndValidateAuthorityGroups
    (List<String> addArgumentValues,
     Collection<AuthorityGroup> existingCollection,
     List<String> removeArgumentValues) {
        return MultipleValueArgValidationService.populateAndValidateMultipleValueArgs(addArgumentValues, existingCollection,
                removeArgumentValues, AuthorityGroup::new, AuthorityGroup::getName, "Authority Group");

    }

    /**
     *To change the app name (having the ref value already set from getTemplateUser)
     * to true if app is present in argumentValues
     * @param addArgumentValues arguments from command line to be added.
     * @param existingCollection present in insight for apps/authority group.
     * @param removeArgumentValues arguments from command line to be removed.
     */
    private Set<Project> populateAndValidateApps
    (List<String> addArgumentValues,
     Collection<Project> existingCollection,
     List<String> removeArgumentValues) {
        return MultipleValueArgValidationService.populateAndValidateMultipleValueArgs(addArgumentValues,
            existingCollection, removeArgumentValues, Project::new, Project::getName, "App");
    }

}
