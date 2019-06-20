/*
    Xpress Insight User Admin Cli
    ============================
    RestService.java
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fico.xpress.exception.ApplicationException;
import com.fico.xpress.vos.Alert;
import com.fico.xpress.vos.InsightApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class RestService
{

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * To get a resource from an insight server.
     *
     * @param insightApplicationConfig
     * @param url
     * @param pathParam
     * @param responseType
     * @param <T>
     */
    public <T> T getResource(InsightApplicationConfig insightApplicationConfig, String url, Map<String, String> pathParam, ParameterizedTypeReference<T> responseType) {
        String msg = "List operation failed.";
        T responseBody = callInsightService(insightApplicationConfig, url, pathParam, null, responseType, HttpMethod.GET, msg);
        return responseBody;
    }

    /**
     * Utility method to get an exception message.
     *
     * @param httpMethod
     * @param msg
     * @param exceptionMsg
     */
    private String getMessage(HttpMethod httpMethod, String msg, String exceptionMsg){
        return (httpMethod == HttpMethod.PUT || httpMethod == HttpMethod.POST) ? msg : exceptionMsg;
    }

    /**
     * To get list of all resources from an insight server.
     *
     * @param insightApplicationConfig
     * @param url
     * @param responseType
     * @param <T>
     */
    public <T> List<T> getListOfResource(InsightApplicationConfig insightApplicationConfig, String url, ParameterizedTypeReference<List<T>> responseType) {
        String token = authenticationService.getToken(insightApplicationConfig);
        List<T> responseBody;
        try {
            HttpHeaders headers = setHeaders(token);
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity <List<T>> response = restTemplate.exchange(url, HttpMethod.GET, request, responseType);
            responseBody = response.getBody();
        } catch (HttpServerErrorException exp) {
            throw new ApplicationException(convertJsonToAlertObject(exp.getResponseBodyAsString()).getMessage());
        } finally {
            authenticationService.invalidateToken(insightApplicationConfig, token);
        }
        return responseBody;
    }

    /**
     * To add a resource to an insight server.
     *
     * @param insightApplicationConfig
     * @param url
     * @param object
     * @param responseType
     * @param <T>
     */
    public <T> T addResource(InsightApplicationConfig insightApplicationConfig, String url, T object, ParameterizedTypeReference<T> responseType) {
        String msg = "Add operation failed.";
        T responseBody = callInsightService(insightApplicationConfig, url, null, object, responseType, HttpMethod.POST, msg);
        return responseBody;
    }

    /**
     * To set the request headers.
     *
     * @param token
     */
    protected HttpHeaders setHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", token);
        headers.add("Cache-Control","no-cache");
        headers.add("Referer","required");
        headers.add("X-Requested-With","XMLHttpRequest");
        return headers;
    }

    /**
     * To convert exception json string from an insight server to an alert object.
     *
     * @param json
     */
    protected Alert convertJsonToAlertObject(String json) {
        Alert alert;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            alert = objectMapper.readValue(json, Alert.class);
        } catch (IOException e) {
            throw new ApplicationException("Error in json request format.");
        }
        return alert;
    }

    /**
     * To update an existing resource.
     *
     * @param insightApplicationConfig
     * @param url
     * @param pathParam
     * @param object
     * @param responseType
     * @param <T>
     */
    public <T> T updateResource(InsightApplicationConfig insightApplicationConfig, String url, Map<String, String> pathParam, T object, ParameterizedTypeReference<T> responseType) {
        String msg = "Update operation failed.";
        T responseBody = callInsightService(insightApplicationConfig, url, pathParam, object, responseType, HttpMethod.PUT, msg);
        return responseBody;
    }

    /**
     * Utility method for making rest calls.
     *
     * @param insightApplicationConfig
     * @param url
     * @param pathParam
     * @param object
     * @param responseType
     * @param httpMethod
     * @param msg
     * @param <T>
     */
    private <T> T callInsightService(InsightApplicationConfig insightApplicationConfig, String url, Map<String, String> pathParam,
                                     T object, ParameterizedTypeReference<T> responseType, HttpMethod httpMethod, String msg) {
        String token = authenticationService.getToken(insightApplicationConfig);
        T responseBody;
        try {
            HttpHeaders headers = setHeaders(token);
            HttpEntity<T> request = new HttpEntity<>(object, headers);
            ResponseEntity<T> response;
            if (pathParam != null) {
                response = restTemplate.exchange(url, httpMethod, request, responseType, pathParam);
            } else {
                response = restTemplate.exchange(url, httpMethod, request, responseType);
            }
            responseBody = response.getBody();
        } catch (HttpServerErrorException exp) {
            throw new ApplicationException(convertJsonToAlertObject(exp.getResponseBodyAsString()).getMessage());
        } catch (Exception exp) {
            throw new ApplicationException(getMessage(httpMethod, msg, exp.getMessage()));
        } finally {
            authenticationService.invalidateToken(insightApplicationConfig, token);
        }
        return responseBody;
    }

}
