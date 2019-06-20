/*
    Xpress Insight User Admin Cli
    ============================
    UtilsCLITest.java
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
package com.fico.xpress.insight.clisystemtest;

import com.fico.xpress.exception.ApplicationException;
import com.fico.xpress.vos.InsightApplicationConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.WINDOWS_COMMAND;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


public class UtilsCLITest {

    private RestTemplate restTemplate = new RestTemplate();

    private String token;
    protected static String projectId;

    public static String URL = "http://localhost:8860/";
    private static UtilsCLITest utilscli = new UtilsCLITest();


    /* Static 'instance' method */
    public static UtilsCLITest getInstance() {
        return utilscli;
    }

    protected static String consoleCommandExecuter(String[] commands) throws IOException {
        // This code demonstrate using Java ProcessBuilder class to run a batch
        // file
        // Java ProcessBuilder and BufferedReader classes are used to implement
        // this.
        final StringBuffer sb = new StringBuffer();
        int processComplete = -1;
            if (System.getenv("os") == null || !System.getenv("os").contains("Windows")) {
                commands[0] = commands[0].replace(WINDOWS_COMMAND, "insightadmincli");
            }

        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(true);
        try {
            final Process process = pb.start();
            final InputStream is = process.getInputStream();
            // the background thread watches the output from the process
            new Thread(new Runnable() {
                public void run() {
                    try {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(is));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append('\n');
                        }
                    } catch (IOException e) {
                        System.out
                                .println("Java ProcessBuilder: IOException occured.");
                        e.printStackTrace();
                    } finally {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            // Wait to get exit value
            // the outer thread waits for the process to finish
            processComplete = process.waitFor();
            System.out.println("Java ProcessBuilder result:" + processComplete);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Java ProcessBuilder - return=: " + "\n" + sb.toString());
        return sb.toString();
    }

    public static Map getTableContainsDescribeUser(String userName) throws IOException {

        String[] commandsForDescribeUser = {"insightadmincli.bat", "user", "describe", "-u", "admin", "-p", "admin123", "-tu", userName,"-url",URL};
        //store the details of the User using describe
        String commandExecutorResultString = UtilsCLITest.consoleCommandExecuter(commandsForDescribeUser);
        List<String> resultsOfCommandsForDescribeUser = Arrays.asList(commandExecutorResultString.split("\\n"));
        Map<String, String> userTableDetails = new LinkedHashMap<>();
        //assigning all the table contains in key Value format like <UserName,TestAdmin> etc
        for (String keyValStr : resultsOfCommandsForDescribeUser) {
            String[] keyValArr = keyValStr.split(":");
            if (keyValArr != null) {
            /*    String key = keyValArr[0].trim();
                String value = keyValArr[1].trim();*/
                String key = keyValArr[0].trim();
                String value = "";
                if (keyValArr.length == 2)
                    value = keyValArr[1].trim();
                userTableDetails.put(key, value);
            }
        }
        System.out.println("userTableDetails map:   :" + userTableDetails);

        return userTableDetails;
    }
    public static Map getTableContainsDescribeAuthorityUser(String userName) throws IOException {

        String[] commandsForDescribeUser = {"insightadmincli.bat", "authgroup", "describe","-authoritygroupname",userName, "-u", "admin", "-p", "admin123", "-url", URL};
        //store the details of the Auth User using describe
        String commandExecutorResultString = UtilsCLITest.consoleCommandExecuter(commandsForDescribeUser);
        List<String> resultsOfCommandsForDescribeUser = Arrays.asList(commandExecutorResultString.split("\n"));
        Map<String, String> authUserTableDetails = new LinkedHashMap<>();
        //assigning all the table contains in key Value format like <AuthUser Titles,values> etc
        for (String keyValStr : resultsOfCommandsForDescribeUser) {
            String[] keyValArr = keyValStr.split(":");
            if (keyValArr != null) {
                String key = keyValArr[0].trim();
                String value = "";
                if (keyValArr.length == 2)
                    value = keyValArr[1].trim();
                authUserTableDetails.put(key, value);
            }
        }
        System.out.println("authUserTableDetails map:   :" + authUserTableDetails);

        return authUserTableDetails;
    }


    public String uploadResource(InsightApplicationConfig insightApplicationConfig) {
        token = performLoginToInsight(insightApplicationConfig);
        String url = InsightCmdLineTestConstants.INSIGHT_HOST + InsightCmdLineTestConstants.INSIGHT_UPLOAD_APP_URI;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.add("Cookie", token);
            headers.add("Referer", InsightCmdLineTestConstants.INSIGHT_HOST + "insight/");
            headers.add("X-Requested-With", "XMLHttpRequest");
            headers.add("Authorization", "Token " + token);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("project-file", getInsightApp());
            body.add("project-file-format", "INSIGHT");
            body.add("project-name", "");

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return response.getBody();
        } catch (HttpServerErrorException exp) {
            exp.printStackTrace();
            throw new ApplicationException((exp.getResponseBodyAsString()));
        } catch (Exception exp) {
            exp.printStackTrace();
            throw new ApplicationException("App Upload operation failed.");
        } finally {
            //authenticationService.invalidateToken(token);
        }
    }

    public ByteArrayResource getInsightApp() throws URISyntaxException, IOException {
        Path path = Paths.get(getClass().getClassLoader().getResource(InsightCmdLineTestConstants.CAMPAIGN_CONVERSION_PROJECT_FILE_NAME).toURI());
        byte[] project = Files.readAllBytes(path);
        ByteArrayResource contentsAsResource = new ByteArrayResource(project);
        return contentsAsResource;
    }

    public String deleteResource(InsightApplicationConfig insightApplicationConfig, String projectId) {
        if (token == null) {
            token = performLoginToInsight(insightApplicationConfig);
        }
        String url = InsightCmdLineTestConstants.INSIGHT_HOST + InsightCmdLineTestConstants.INSIGHT_DELETE_APP_URI;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", token);
            headers.add("Referer", InsightCmdLineTestConstants.INSIGHT_HOST + "insight/");
            headers.add("X-Requested-With", "XMLHttpRequest");
            headers.add("Authorization", "Token " + token);

            Map<String, String> pathParam = new HashMap<>();
            pathParam.put("id", projectId);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class, pathParam);
            return response.getBody();
        } catch (HttpServerErrorException exp) {
            exp.printStackTrace();
            throw new ApplicationException((exp.getResponseBodyAsString()));
        } catch (Exception exp) {
            exp.printStackTrace();
            throw new ApplicationException("App Delete operation failed.");
        }
    }


    /**
     * Log in the user.
     *
     * @param insightApplicationConfig of the user
     */
    public String performLoginToInsight(InsightApplicationConfig insightApplicationConfig) {
        String insightToken;
        String url = InsightCmdLineTestConstants.INSIGHT_HOST + InsightCmdLineTestConstants.INSIGHT_LOGIN_URI;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", insightApplicationConfig.getUserName());
        map.add("password", insightApplicationConfig.getPassword());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(url, request, String.class);
            insightToken = response.getBody();
        } catch (ResourceAccessException e) {
            throw new ApplicationException("Could not connect to server.");
        }
        return insightToken;
    }

    /**
     * Get the value from a JSON object with the given key as a JSON array.
     *
     * @param object JSON object
     * @param key    object key
     * @return found value.
     */
    public JSONArray getJSONArray(JSONObject object, String key) {
        Object objectValue = object.get(key);
        assertNotNull("Expected JSON object for key: " + key, objectValue);

        if (objectValue instanceof JSONArray) {
            return (JSONArray) objectValue;
        } else {
            fail("JSON object " + key + " is not a JSON array");
        }
        return null;
    }

    /**
     * Get the value from a JSON object with the given key as an inner JSON object.
     *
     * @param object JSON object
     * @param key    object key
     * @return found value.
     */
    public JSONObject getJSONObject(JSONObject object, String key) {
        Object objectValue = object.get(key);
        assertNotNull("Expected JSON object for key: " + key, objectValue);

        if (objectValue instanceof JSONObject) {
            return (JSONObject) objectValue;
        } else {
            fail("JSON object " + key + " is not a JSON object");
        }
        return null;
    }

}
