/*
    Xpress Insight User Admin Cli
    ============================
    ExtendedMultipleValueArg.java
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

public class ExtendedMultipleValueArg {

    /**
     * Indicates whether the resource is selected or not.
     */
    private boolean selected;

    /**
     * Name of the resource.
     */
    private String name;

    /**
     * Default constructor
     */
    public ExtendedMultipleValueArg() {
    }

    public ExtendedMultipleValueArg(String name) {
        this.name = name;
    }

    public ExtendedMultipleValueArg(boolean selected) {
        this.selected = selected;
    }

    public ExtendedMultipleValueArg(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    /**
     * Get whether selected.
     * @return true if selected.
     */
    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Get the name.
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
