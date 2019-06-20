/*
    Xpress Insight User Admin Cli
    ============================
    AuthorityGroupExtended.java
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

import java.util.ArrayList;
import java.util.Collection;


public class AuthorityGroupExtended extends AuthorityGroup {

    /**
     * Collection of authorities.
     */
    protected Collection<AuthorityListItem> authorities;

    /**
     * Get the list of authorities for a specific authority group.
     * @return
     */
    public Collection<AuthorityListItem> getAuthorities() {
        return authorities;
    }

    /**
     * Set the authorities of a specific authority group.
     * @param authorities
     */
    public void setAuthorities(Collection<AuthorityListItem> authorities) {
        this.authorities = authorities;
    }

    /**
     * Default constructor.
     */
    public AuthorityGroupExtended() {
        super();
        this.authorities = new ArrayList<>();
    }

    public AuthorityGroupExtended(String name, String description, Collection<AuthorityListItem> authorities) {
        super(name);
        this.description = description;
        this.authorities = authorities;
    }

    /**
     * @return Utility method to print the authority details.
     */
    @Override
    public String toString() {
        return "{AuthorityGroup name:" + super.getName() + "}";
    }
}
