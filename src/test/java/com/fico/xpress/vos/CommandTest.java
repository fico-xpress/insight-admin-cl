/*
    Xpress Insight User Admin Cli
    ============================
    CommandTest.java
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

import com.fico.xpress.utility.InsightAdminCliConstants;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommandTest {

    @Test
    public void testGetArgumentsReturnEmptyListWhenArgumentsNull(){
        Command command = new Command();
        assertEquals(command.getArguments(), Collections.emptyMap());
    }

    @Test
    public void testGetStringArgumentValue(){
        Command command = new Command();
        command.getArguments().put(InsightAdminCliConstants.FIRST_NAME,"fname");
        assertEquals(command.getStringArgumentValue(InsightAdminCliConstants.FIRST_NAME),"fname");
    }

    @Test
    public void testGetBooleanArgumentValue(){
        Command command = new Command();
        command.getArguments().put(InsightAdminCliConstants.ENABLED, true);
        assertEquals(command.getBooleanArgumentValue(InsightAdminCliConstants.ENABLED),true);
    }

    @Test
    public void testGettersAndSetters(){
        BeanTester beanTester = new BeanTester();
        beanTester.testBean(Command.class);
    }

    @Test
    public void testGetMultipleArgumentValue() {
        Command command = new Command();
        String str[] = new String[]{"test","test1"};
        List<String> list = Arrays.asList(str);
        command.getArguments().put(InsightAdminCliConstants.ADD_AUTHORITY_GROUP, list);
        assertEquals(list, command.getMultipleArgumentValue(InsightAdminCliConstants.ADD_AUTHORITY_GROUP));
    }
}
