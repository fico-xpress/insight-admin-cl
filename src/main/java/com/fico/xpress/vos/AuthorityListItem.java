/*
    Xpress Insight User Admin Cli
    ============================
    AuthorityListItem.java
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

public class AuthorityListItem extends ExtendedMultipleValueArg {

    /**
     * display name of the authority.
     */
    private String displayName = "";


    /**
     * Default Constructor
     */
    public AuthorityListItem(){

    }
    /**
     * Constructor.
     * @param displayName authority display name.
     * @param selected selected.
     */
    public AuthorityListItem(String displayName, boolean selected) {
        super(selected);
        this.displayName = displayName;
    }

    public AuthorityListItem(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get the display name.
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Utility method to print the authority details.
     * @return authority details.
     */
    @Override
    public String toString() {
        return "{Authority name: " + displayName + " : " + getSelected() + "}";
    }


    /**
     * @param o
     * @return true if both the authorities have the same name.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorityListItem)) {
            return false;
        }
        AuthorityListItem that = (AuthorityListItem) o;
        return getDisplayName().equals(that.getDisplayName());
    }

    /**
     * @return hash code using the name.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDisplayName());
    }
}
