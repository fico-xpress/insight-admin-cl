/*
    Xpress Insight User Admin Cli
    ============================
    RestServiceTest.java
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
import com.fico.xpress.utility.InsightAdminUriConstants;
import com.fico.xpress.vos.Alert;
import com.fico.xpress.vos.AuthorityGroup;
import com.fico.xpress.vos.InsightApplicationConfig;
import com.fico.xpress.vos.UserInfo;
import com.fico.xpress.vos.UserInfoExtended;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AuthenticationServiceImpl authenticationService;

    @InjectMocks
    private RestService restService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private HttpServerErrorException httpServerErrorException;

    @Test
    public void testGetResource(){
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        ParameterizedTypeReference<UserInfoExtended> responseType = new ParameterizedTypeReference<UserInfoExtended>(){};
        Map<String,String> pathParam = new LinkedHashMap<>();
        UserInfoExtended userInfoExtended = new UserInfoExtended();
        List<AuthorityGroup> authorityGroups = new ArrayList<>();
        authorityGroups.add(new AuthorityGroup("AdvancedUser", false));
        authorityGroups.add(new AuthorityGroup("BasicUser", false));

        userInfoExtended.setAuthorityGroups(authorityGroups);
        userInfoExtended.setUsername("admin");
        userInfoExtended.setFirstName("Administrator");
        userInfoExtended.setLastName("User");

        ResponseEntity<UserInfoExtended> response = new ResponseEntity<>(userInfoExtended, HttpStatus.OK);

        String url = InsightAdminUriConstants.INSIGHTADMIN_URI + InsightAdminUriConstants.DESC_OR_UPDATE_USER_URI;
        doReturn(response).when(restTemplate).exchange(eq(url),
                eq(HttpMethod.GET),
                Matchers.<HttpEntity<String>>any(),
                Matchers.<ParameterizedTypeReference<UserInfoExtended>>any(),
                Matchers.<Map<String,String>>any()
        );

        UserInfoExtended userInfoExtended1 = restService.getResource(insightApplicationConfig, url, pathParam, responseType);
        assertTrue("admin".equalsIgnoreCase(userInfoExtended1.getUsername()));
        assertTrue("AdvancedUser".equalsIgnoreCase(((List<AuthorityGroup>)userInfoExtended1.getAuthorityGroups()).get(0).getName()));
    }

    @Test
    public void testGetResourceThrowsHttpServerErrorException(){
        when(httpServerErrorException
            .getResponseBodyAsString()).thenReturn("{\"type\":\"alert-danger\",\"title\":\"User\",\"message\":\"Unable to retrieve the user information.\"}");
        when(restTemplate.exchange(Matchers.anyString(),
            eq(HttpMethod.GET),
            Matchers.<HttpEntity<String>>any(),
            Matchers.<ParameterizedTypeReference<UserInfoExtended>>any()))
            .thenThrow(httpServerErrorException);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Unable to retrieve the user information.");
        restService.getResource(null,"" , null, null);
    }

    @Test
    public void testGetListOfResource(){
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        ParameterizedTypeReference<List<UserInfo>> responseType = new ParameterizedTypeReference<List<UserInfo>>(){};
        List<UserInfo> userList = new ArrayList<>();
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setUsername("admin");
        userList.add(userInfo1);
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setUsername("admin2");
        userList.add(userInfo2);
        ResponseEntity<List<UserInfo>> response = new ResponseEntity<>(userList, HttpStatus.OK);
        String url = InsightAdminUriConstants.INSIGHTADMIN_URI + InsightAdminUriConstants.LIST_USER_URI;
        doReturn(response).when(restTemplate).exchange(eq(url),
                eq(HttpMethod.GET),
                Matchers.<HttpEntity<String>>any(),
                Matchers.<ParameterizedTypeReference<List<UserInfo>>>any()
                );

        assertEquals(userList, restService.getListOfResource(insightApplicationConfig, url, responseType));
    }

    @Test
    public void testGetListOfResourceThrowsHttpServerErrorException(){
        when(httpServerErrorException
            .getResponseBodyAsString()).thenReturn("{\"type\":\"alert-danger\",\"title\":\"User\",\"message\":\"Unable to retrieve the user List.\"}");
        when(restTemplate.exchange(Matchers.anyString(),
            eq(HttpMethod.GET),
            Matchers.<HttpEntity<String>>any(),
            Matchers.<ParameterizedTypeReference<List<UserInfo>>>any()
        )).thenThrow(httpServerErrorException);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Unable to retrieve the user List.");
        restService.getListOfResource(null,"" , null);
    }
    @Test
    public void test_getListOfResource_calls_invalidateToken_WhenExceptionIsThrown(){
        when(restTemplate.exchange(Matchers.anyString(),
            eq(HttpMethod.GET),
            Matchers.<HttpEntity<String>>any(),
            Matchers.<ParameterizedTypeReference<List<UserInfo>>>any()))
            .thenThrow(new RestClientException("Invalid Login"));
       try {
           restService.getListOfResource(null, "" , null);
       } catch (RestClientException exp) {
       }
        verify(authenticationService).invalidateToken(Mockito.any(), Mockito.any());
    }

    @Test
    public void test_getResource_calls_invalidateToken_WhenExceptionIsThrown(){
        when(restTemplate.exchange(Matchers.anyString(),
            eq(HttpMethod.GET),
            Matchers.<HttpEntity<String>>any(),
            Matchers.<ParameterizedTypeReference<UserInfoExtended>>any()))
            .thenThrow(new RestClientException("Invalid Login"));
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Invalid Login");
        restService.getResource(null,"", null, null);
    }

    @Test
    public void testAddResourceForNewUser(){
        ParameterizedTypeReference<UserInfoExtended> responseType = new ParameterizedTypeReference<UserInfoExtended>(){};
        UserInfoExtended newUser = new UserInfoExtended("admin",
                "Administrator",
                "User",
                true,
                "admin",
                "abc@gmail.com",
                true,
                false,
                true,
                null,
                null,
                null,
                null);
        ResponseEntity<UserInfoExtended> response = new ResponseEntity<>(newUser, HttpStatus.OK);
        String url = InsightAdminUriConstants.INSIGHTADMIN_URI + InsightAdminUriConstants.ADD_USER_URI;
        when(restTemplate.exchange(eq(url),
            eq(HttpMethod.POST),
            Matchers.<HttpEntity<UserInfoExtended>>any(),
            Matchers.<ParameterizedTypeReference<UserInfoExtended>>any()))
            .thenReturn(response);

        UserInfoExtended userInfoExtended = restService.addResource(null, url, null, responseType);
        assertTrue("admin".equalsIgnoreCase(userInfoExtended.getUsername()));
        assertTrue("Administrator".equalsIgnoreCase(userInfoExtended.getFirstName()));
        assertTrue("User".equalsIgnoreCase(userInfoExtended.getLastName()));
        assertTrue(userInfoExtended.getTableauEnabled());
        assertTrue("admin".equalsIgnoreCase(userInfoExtended.getTableauUsername()));
        assertTrue(userInfoExtended.isEnabled());
        assertFalse(userInfoExtended.isLocked());
        assertTrue(userInfoExtended.isLocalAccount());
        assertNull(userInfoExtended.getPassword());
        assertNull(userInfoExtended.getId());
        assertNull(userInfoExtended.getAuthorityGroups());
        assertNull(userInfoExtended.getProjects());
    }

    @Test
    public void testAddResourceThrowsHttpServerErrorException(){
        when(httpServerErrorException
            .getResponseBodyAsString()).thenReturn("{\"type\":\"alert-danger\",\"title\":\"User\",\"message\":\"The user already exists.\"}");
        when(restTemplate.exchange(Matchers.anyString(),
            eq(HttpMethod.POST),
            Matchers.<HttpEntity<UserInfoExtended>>any(),
            Matchers.<ParameterizedTypeReference<UserInfoExtended>>any()))
            .thenThrow(httpServerErrorException);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("The user already exists.");
        restService.addResource(null,"", null, null);
    }

    @Test
    public void testAddResourceThrowsException(){
       when(restTemplate.exchange(Matchers.anyString(),
            eq(HttpMethod.POST),
            Matchers.<HttpEntity<UserInfoExtended>>any(),
            Matchers.<ParameterizedTypeReference<UserInfoExtended>>any(),
           Matchers.<Map<String,String>>any()))
            .thenThrow(new RestClientException("Failure"));
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Add operation failed.");
        restService.addResource(null,"", null, null);
    }

    @Test
    public void testConvertJsonToAlertObject() {
        String json = "{\"type\":\"alert-danger\",\"title\":\"User\",\"message\":\"The user already exists.\"}";
        Alert alert = restService.convertJsonToAlertObject(json);
        assertTrue("The user already exists.".equalsIgnoreCase(alert.getMessage()));
    }

    @Test
    public void testConvertJsonToAlertObjectThrowsIOException() {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Error in json request format.");
        restService.convertJsonToAlertObject("Invalid format of json");
    }

    @Test
    public void testUpdateResourceForNewUser() {
        ParameterizedTypeReference<UserInfoExtended> responseType = new ParameterizedTypeReference<UserInfoExtended>(){};
        UserInfoExtended newUser = new UserInfoExtended("admin",
                "Administrator",
                "User",
                true,
                "admin",
                "abc@gmail.com",
                true,
                false,
                true,
                null,
                null,
                null,
                null);
        ResponseEntity<UserInfoExtended> response = new ResponseEntity<>(newUser, HttpStatus.OK);
        String url = InsightAdminUriConstants.INSIGHTADMIN_URI + InsightAdminUriConstants.DESC_OR_UPDATE_USER_URI;
        ArgumentCaptor<HttpEntity> requestCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        when(restTemplate.exchange(eq(url),
                eq(HttpMethod.PUT),
                requestCaptor.capture(),
            Matchers.<ParameterizedTypeReference<UserInfoExtended>>any()))
                .thenReturn(response);
        UserInfoExtended userInfoExtended = restService.updateResource(null, url, null, newUser, responseType);
        HttpEntity<UserInfoExtended> createdRequest = requestCaptor.getValue();
        assertTrue(createdRequest.getBody().getUsername().equals("admin"));
        assertTrue("admin".equalsIgnoreCase(userInfoExtended.getUsername()));
        assertTrue("Administrator".equalsIgnoreCase(userInfoExtended.getFirstName()));
        assertTrue("User".equalsIgnoreCase(userInfoExtended.getLastName()));
        assertTrue(userInfoExtended.getTableauEnabled());
        assertTrue("admin".equalsIgnoreCase(userInfoExtended.getTableauUsername()));
        assertTrue(userInfoExtended.isEnabled());
        assertFalse(userInfoExtended.isLocked());
        assertTrue(userInfoExtended.isLocalAccount());
        assertNull(userInfoExtended.getPassword());
        assertNull(userInfoExtended.getId());
        assertNull(userInfoExtended.getAuthorityGroups());
        assertNull(userInfoExtended.getProjects());
    }

    @Test
    public void testUpdateResourceThrowsHttpServerErrorException(){
        Map<String, String> pathParam = new HashMap<>();
        pathParam.put("authgrpname", " ");
        when(httpServerErrorException
                .getResponseBodyAsString()).thenReturn("{\"type\":\"alert-danger\",\"title\":\"User\",\"message\":\"A username must be provided, be less than 100 characters and contain no invalid characters.\"}");
        when(restTemplate.exchange(Matchers.anyString(),
                eq(HttpMethod.PUT),
                Matchers.<HttpEntity<UserInfoExtended>>any(),
                Matchers.<ParameterizedTypeReference<UserInfoExtended>>any(),
                Matchers.<Map<String,String>>any()))
                .thenThrow(httpServerErrorException);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("A username must be provided, be less than 100 characters and contain no invalid characters.");
        restService.updateResource(null, "", pathParam, null, null);
    }

    @Test
    public void testUpdateResourceThrowsException(){
        when(restTemplate.exchange(Matchers.anyString(),
                eq(HttpMethod.PUT),
                Matchers.<HttpEntity<UserInfoExtended>>any(),
                Matchers.<ParameterizedTypeReference<UserInfoExtended>>any(),
                Matchers.<Map<String,String>>any())).thenThrow(new RestClientException("Failure"));
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Update operation failed.");
        restService.updateResource(null,"", null, null, null);
    }
}
