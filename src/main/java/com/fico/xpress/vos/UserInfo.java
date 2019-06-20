/*
    Xpress Insight User Admin Cli
    ============================
    UserInfo.java
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

public class UserInfo {

    /**
     * Username of the user.
     */
    protected String username;

    /**
     * First name of the user.
     */
    protected String firstName;

    /**
     * Last name of the user.
     */
    protected String lastName;

    /**
     * Tableau username of the user.
     */
    protected boolean tableauEnabled;

    /**
     * Tableau username of the user.
     */
    protected String tableauUsername;

    /**
     * Email address of the user.
     */
    protected String email;

    /**
     * Whether the account is enabled.
     */
    protected boolean enabled;

    /**
     * Whether the account is locked.
     */
    protected boolean locked;

    /**
     * Whether this account is authenticated locally only.
     */
    protected boolean localAccount;

    /**
     * Password of the user.
     */
    protected String password;


    public UserInfo(String username,
                    String firstName,
                    String lastName,
                    boolean tableauEnabled,
                    String tableauUsername,
                    String email,
                    boolean enabled,
                    boolean locked,
                    boolean localAccount,
                    String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tableauEnabled = tableauEnabled;
        this.tableauUsername = tableauUsername;
        this.email = email;
        this.enabled = enabled;
        this.locked = locked;
        this.localAccount = localAccount;
        this.password = password;
    }

    /**
     * Default constructor
     */
    public UserInfo() {
        this.username = "";
        this.firstName = "";
        this.lastName = "";
        this.tableauEnabled = true;
        this.tableauUsername = "";
        this.email = "";
        this.locked = false;
        this.enabled = false;
        this.localAccount = false;
    }

    /**
     * Get the user name.
     * @return user name.
     */
    public String getUsername() {
        return username;
    }

    /**
     * User account is enabled.
     * @return true if the account is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * User account is locked.
     * @return true if the account is locked.
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Get the first name of the user.
     * @return first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the last name of the user.
     * @return last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get whether tableau is enabled.
     * @return tableau enabled.
     */
    public boolean getTableauEnabled() {
        return tableauEnabled;
    }

    /**
     * Get the tableau username of the user.
     * @return tableau username.
     */
    public String getTableauUsername() {
        return tableauUsername;
    }

    /**
     * Get the email address of the user.
     * @return email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Is the user account authenticated locally only.
     */
    public boolean isLocalAccount() {
        return localAccount;
    }

    /**
     * Set whether the user account should be authenticated locally only.
     */
    public void setLocalAccount(boolean localAccount) {
        this.localAccount = localAccount;
    }

    /**
     * Set the username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set the firstName
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set the lastName
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Tableau is enabled.
     * @return true if the Tableau is enabled.
     */
    public boolean isTableauEnabled() {
        return tableauEnabled;
    }

    /**
     * Set the tableauEnabled
     * @param tableauEnabled
     */
    public void setTableauEnabled(boolean tableauEnabled) {
        this.tableauEnabled = tableauEnabled;
    }

    /**
     * Sets the tableauUsername
     * @param tableauUsername
     */
    public void setTableauUsername(String tableauUsername) {
        this.tableauUsername = tableauUsername;
    }

    /**
     * Set the email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the enabled
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Set the locked
     * @param locked
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Get the password of the user.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Utility method to print the user details.
     * @return user details.
     */
    @Override
    public String toString() {
        return "{UserInfo username:"+username+",firstName:"+firstName+",lastName:"+lastName+
                ",tableauEnabled:"+tableauEnabled+",tableauUsername:"+ tableauUsername +",email:"+email+
                ",enabled:"+enabled+",locked:"+locked+",localAccount:"+localAccount+"}";
    }
}
