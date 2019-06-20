/*
    Xpress Insight User Admin Cli
    ============================
    UserInfoExtendedTest.java
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

import org.junit.Test;
import org.meanbean.test.BeanTester;

import static org.junit.Assert.assertEquals;

public class UserInfoExtendedTest {

    @Test
    public void testAddAuthorityGroup(){
        UserInfoExtended userInfoExtended = new UserInfoExtended();
        AuthorityGroup authorityGroup = new AuthorityGroup();
        userInfoExtended.addAuthorityGroup(authorityGroup);
    }

    @Test
    public void testGettersAndSetters(){
        BeanTester beanTester = new BeanTester();
        beanTester.testBean(UserInfoExtended.class);
    }

    @Test
    public void testConstructor(){
        UserInfoExtended userInfoExtended = new UserInfoExtended("uname",
                "fname",
                "lname",
                false,
                "tuname",
                "test@gmail.com",
                true,
                false,
                false,
                "pass",
                "12",
                null,
                null);

        assertEquals("uname", userInfoExtended.getUsername());
        assertEquals("fname", userInfoExtended.getFirstName());
        assertEquals("lname", userInfoExtended.getLastName());
        assertEquals(false, userInfoExtended.isTableauEnabled());
        assertEquals("test@gmail.com", userInfoExtended.getEmail());
        assertEquals(true, userInfoExtended.isEnabled());
        assertEquals(false, userInfoExtended.isLocked());
        assertEquals(false, userInfoExtended.isLocalAccount());
        assertEquals("pass", userInfoExtended.getPassword());
        assertEquals("12", userInfoExtended.getId());
        assertEquals(null, userInfoExtended.getAuthorityGroups());
        assertEquals(null, userInfoExtended.getProjects());
    }
}
