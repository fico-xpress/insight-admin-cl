/*
    Xpress Insight User Admin Cli
    ============================
    Project.java
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


import java.util.Objects;

/**
 * Represents a project.
 **/
public class Project extends ExtendedMultipleValueArg {
    /**
     * Project reference id.
     */
    private String ref;

    public Project(String ref, String name, boolean selected) {
        super(name, selected);
        this.ref = ref;
    }

    /**
     * Default Constructor.
     */
    public Project() {
    }

    public Project(String s) {
        super(s);
    }

    /**
     * Gets the project reference.
     * @return project reference.
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param o
     * @return true if the two projects have the same name.
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Project)) {
            return false;
        }
        Project project = (Project) o;
        if(getName() == null || project.getName() == null){
            return getName() == project.getName();
        }
        return getName().equals(project.getName());
    }

    /**
     * @return hash code using the name.
     */
    @Override
    public final int hashCode() {
        return Objects.hash(getName());
    }
}


