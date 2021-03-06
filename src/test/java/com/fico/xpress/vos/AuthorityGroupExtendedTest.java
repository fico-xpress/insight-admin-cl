/*
    Xpress Insight User Admin Cli
    ============================
    AuthorityGroupExtendedTest.java
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

import com.google.common.collect.Iterables;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class AuthorityGroupExtendedTest {

    @Test
    public void testGettersAndSetters(){
        BeanTester beanTester = new BeanTester();
        beanTester.testBean(AuthorityGroupExtended.class);
    }

    @Test
    public void testDefaultConstructor(){
        Collection<AuthorityListItem> authorities = new ArrayList<>(Arrays.asList(new AuthorityListItem("DEVELOPER", true)));

        AuthorityGroupExtended authorityGroupExtended = new AuthorityGroupExtended("test", "this is test", authorities);
        AuthorityListItem authorityListItem = Iterables.get(authorityGroupExtended.getAuthorities(),0);
        assertEquals("DEVELOPER", authorityListItem.getDisplayName());
        assertEquals(true, authorityListItem.getSelected());

    }
}
