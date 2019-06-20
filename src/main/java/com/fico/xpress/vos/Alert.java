/*
    Xpress Insight User Admin Cli
    ============================
    Alert.java
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

/**
 * Represents an alert.
 **/
public class Alert {
    /**
     * Type of alert.
     */
    private String type;

    /**
     * Title of the alert.
     */
    private String title;

    /**
     * content of the alert.
     */
    private String message;

    /**
     * Constructor.
     */
    public Alert() {
        this.type = "";
        this.title = "";
        this.message = "";
    }


    /**
     * Gets the alert type.
     * @return alert type.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the alert title.
     * @return alert title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the alert message.
     * @return alert message.
     */
    public String getMessage() {
        return message;
    }


    /**
     * Sets the alert type.
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the alert title.
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the alert message.
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}


