/*
    Xpress Insight User Admin Cli
    ============================
    Command.java
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
package com.fico.xpress.vos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Command {

    /**
     * Resource on which operation is to be performed.
     */
    private Resource resource;

    /**
     *Operation to be performed.
     */
    private OperationType operationType;

    /**
     * arguments having arguments in the form of a map.
     */
    private Map<String, Object> arguments;

    /**
     * contains the username, password and host url.
     */
    private InsightApplicationConfig insightApplicationConfig;

    /**
     * Represents that help is present in the command or not.
     */
    private boolean showHelp;

    /**
     * Get the resource.
     * @return resource.
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Set the resource.
     * @param resource
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * Get the operation Type.
     * @return operation Type.
     */
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     * Set the operation Type.
     * @param operationType
     */
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    /**
     * Get the argument map.
     * @return  argument map.
     */
    public Map<String, Object> getArguments() {
        if (arguments == null) {
            arguments = new HashMap<>();
        }
        return arguments;
    }

    /**
     * Is help present.
     * @return true if there is help argument present.
     */
    public boolean isShowHelp() {
        return showHelp;
    }

    /**
     * Set the showHelp.
     * @param showHelp
     */
    public void setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
    }

    /**
     * Get insightApplicationConfig
     * @return insightApplicationConfig
     */
    public InsightApplicationConfig getInsightApplicationConfig() {
        return insightApplicationConfig;
    }

    /**
     * Set the insightApplicationConfig
     * @param insightApplicationConfig
     */
    public void setInsightApplicationConfig(InsightApplicationConfig insightApplicationConfig) {
        this.insightApplicationConfig = insightApplicationConfig;
    }

    /**
     * Get the string argument value.
     * @param argName
     * @return string argument value.
     */
    public String getStringArgumentValue(String argName) {
        return (String) arguments.getOrDefault(argName, "");
    }

    /**
     * Get the boolean argument value.
     * @param argName
     * @return boolean argument value.
     */
    public boolean getBooleanArgumentValue(String argName) {
        return arguments.containsKey(argName) && (Boolean) arguments.get(argName);
    }

    /**
     * Get the multiple argument value.
     * @param argName
     * @return multiple argument value.
     */
    public List<String> getMultipleArgumentValue(String argName) {
        return (List<String>) arguments.get(argName);
    }
}
