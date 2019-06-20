/*
    Xpress Insight User Admin Cli
    ============================
    AuthorityListItemTest.java
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

import org.junit.Assert;
import org.junit.Test;
import org.meanbean.test.BeanTester;

public class AuthorityListItemTest {

    @Test
    public void testGettersAndSetters(){
        BeanTester beanTester = new BeanTester();
        beanTester.testBean(Command.class);
    }

    @Test
    public void testEqualsAndHashCode(){
        UserInfoExtended userInfoExtended = new UserInfoExtended();
        AuthorityListItem authorityListItem = new AuthorityListItem("TestAuthority",true);

        AuthorityListItem authorityListItemComparator = new AuthorityListItem("TestAuthority",true);

        Assert.assertTrue(authorityListItem.equals(authorityListItemComparator));
        Assert.assertTrue(authorityListItem.equals(authorityListItem));
        Assert.assertEquals(authorityListItem.hashCode(), authorityListItemComparator.hashCode());
        Assert.assertFalse(authorityListItem.equals(userInfoExtended));
    }
}

