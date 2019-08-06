/*
    Xpress Insight User Admin Cli
    ============================
    AuthorityGroupTest.java
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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.meanbean.test.BeanTester;

public class AuthorityGroupTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testGettersAndSetters(){
        BeanTester beanTester = new BeanTester();
        beanTester.testBean(AuthorityGroup.class);
    }

    @Test
    public void testConstructorWithTwoArgs(){
        AuthorityGroup authorityGroup = new AuthorityGroup("test",true);
        Assert.assertEquals("test",authorityGroup.getName());
        Assert.assertTrue(authorityGroup.getSelected());
    }

    @Test
    public void testConstructorWithThreeArgs(){
        AuthorityGroup authorityGroup = new AuthorityGroup("test","Test Authority",true);
        Assert.assertEquals("test",authorityGroup.getName());
        Assert.assertEquals("Test Authority",authorityGroup.getDescription());
        Assert.assertTrue(authorityGroup.getSelected());
    }


}
