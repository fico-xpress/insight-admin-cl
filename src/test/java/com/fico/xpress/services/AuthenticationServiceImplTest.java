/*
    Xpress Insight User Admin Cli
    ============================
    AuthenticationServiceImplTest.java
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
import com.fico.xpress.vos.InsightApplicationConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.INSIGHT_HOST;
import static com.fico.xpress.utility.InsightAdminUriConstants.LOGIN_URI;
import static com.fico.xpress.utility.InsightAdminUriConstants.LOGOUT_URI;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestService restService;

    @InjectMocks
    private AuthenticationServiceImpl authService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testGetTokenSuccess() {
        String token = "OMADMINSID=th8v1WoBXcHeAZ5zBx8G73oDEVHyAqO7vhyFIIBe.d6pjkmq2";
        String cookie = "OMADMINSID=th8v1WoBXcHeAZ5zBx8G73oDEVHyAqO7vhyFIIBe.d6pjkmq2; path=/insightadmin; HttpOnly";
        String location = "http://localhost:8860/insightadmin/pages/admin.jsp#userlist";
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig("admin", "admin123");
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("Set-Cookie", cookie);
        map.add("Location", location);
        ResponseEntity<String> response = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(eq(insightApplicationConfig.getEndPoint(LOGIN_URI)),
            Matchers.anyString(),
            Matchers.eq(String.class))).thenReturn(response);
        assertEquals(token, authService.getToken(insightApplicationConfig));
    }

    @Test
    public void testGetTokenFailure() {
        String cookie = "OMADMINSID=th8v1WoBXcHeAZ5zBx8G73oDEVHyAqO7vhyFIIBe.d6pjkmq2; path=/insightadmin; HttpOnly";
        String location = "http://localhost:8860/insightadmin/logon.jsp?error=true";
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig("admin", "admin123");
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("Set-Cookie", cookie);
        map.add("Location", location);
        ResponseEntity<String> response = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(eq(insightApplicationConfig.getEndPoint(LOGIN_URI)),
            anyString(),
            eq(String.class))).thenReturn(response);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Invalid credentials.");
        authService.getToken(insightApplicationConfig);
    }

    @Test
    public void testInvalidateSession() {
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        Mockito.when(restTemplate.exchange(eq(insightApplicationConfig.getEndPoint(LOGOUT_URI)),
                eq(HttpMethod.GET),
                Matchers.<HttpEntity<String>>any(),
                Matchers.<ParameterizedTypeReference<String>>any())
        ).thenReturn(response);
        assertEquals(HttpStatus.OK, authService.invalidateToken(insightApplicationConfig, ""));
    }

    @Test
    public void testConnectionError() {
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setHostUrl(INSIGHT_HOST);
        Mockito.when(restTemplate.postForEntity(eq(insightApplicationConfig.getEndPoint(LOGIN_URI)),
            anyString(),
            eq(String.class))).thenThrow(new ResourceAccessException("Cannot connect to server."));
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Could not connect to server.");
        authService.getToken(insightApplicationConfig);
    }
}
