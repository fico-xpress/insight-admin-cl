/*
    Xpress Insight User Admin Cli
    ============================
    AuthenticationServiceImpl.java
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static com.fico.xpress.utility.InsightAdminUriConstants.LOGIN_URI;
import static com.fico.xpress.utility.InsightAdminUriConstants.LOGOUT_URI;
import static java.lang.String.format;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestService restService;

    /**
     * Method used to log in to an insight server and to fetch the token.
     * @param insightApplicationConfig
     */
    @Override
    public String getToken(InsightApplicationConfig insightApplicationConfig) {
        String token;
        String url = insightApplicationConfig.getEndPoint(LOGIN_URI);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("j_username", insightApplicationConfig.getUserName());
        map.add("j_password", insightApplicationConfig.getPassword());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(url, request, String.class);
            token = response.getHeaders().get("Set-Cookie").get(0).split(";")[0];
            if (response.getHeaders().get("Location").get(0).contains("error")) {
                throw new ApplicationException("Invalid credentials.");
            }
        } catch (ResourceAccessException | IllegalArgumentException e) {
            throw new ApplicationException(format("Could not connect to server. Reason: %1$s.", e.getMessage()));
        }
        return token;
    }

    /**
     * Method used to log out from an insight server by passing back the token.
     * @param insightApplicationConfig
     * @param token
     */
    @Override
    public HttpStatus invalidateToken(InsightApplicationConfig insightApplicationConfig, String token) {
        String url = insightApplicationConfig.getEndPoint(LOGOUT_URI);
        HttpHeaders headers = restService.setHeaders(token);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url
                , HttpMethod.GET
                , request
                , new ParameterizedTypeReference<String>(){});
        return response.getStatusCode();
    }
}
