/*
    Xpress Insight User Admin Cli
    ============================
    AuthorityGroup.java
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
 * Represents an authority group.
 **/
public class AuthorityGroup extends ExtendedMultipleValueArg {

    /**
     * Description of the authority group.
     */
    protected String description;

    /**
     * Default constructor.
     */
    public AuthorityGroup() {
    }

    public AuthorityGroup(String name, boolean selected) {
        super(name, selected);
    }

    public AuthorityGroup(String name, String description, boolean selected) {
        super(name, selected);
        this.description = description;
    }

    public AuthorityGroup(String s) {
        super(s);
    }

    /**
     * @return Description of the authority group.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param o
     * @return true if the two authority groups have the same name.
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof AuthorityGroup)) {
            return false;
        }
        AuthorityGroup that = (AuthorityGroup) o;
        if(getName() == null || that.getName() == null){
            return getName() == that.getName();
        }
        return  getName().equals(that.getName());
    }

    /**
     * @return hash code using the name.
     */
    @Override
    public final int hashCode() {
        return Objects.hash(getName());
    }

    /**
     * Set the authority group description.
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
