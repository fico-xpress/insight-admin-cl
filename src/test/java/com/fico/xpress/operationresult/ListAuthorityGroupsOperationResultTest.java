/*
    Xpress Insight User Admin Cli
    ============================
    ListAuthorityGroupsOperationResultTest.java
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

package com.fico.xpress.operationresult;

import com.fico.xpress.vos.AuthorityGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ListAuthorityGroupsOperationResultTest {
    @InjectMocks
    private ListAuthorityGroupsOperationResult listAuthorityGroupsOperationResult;

    @Test
    public void testPrint() {
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuf, true);
        List<AuthorityGroup> authorityGroupList = new ArrayList<>(Arrays.asList(new AuthorityGroup("AdvancedUser",true),
            new AuthorityGroup("BasicUser",true)));
        listAuthorityGroupsOperationResult.setAuthorityGroups(authorityGroupList);
        listAuthorityGroupsOperationResult.print(out);
        assertThat(new String(outBuf.toByteArray()), is(allOf(
            containsString("AdvancedUser"),
            containsString("BasicUser")
        )));
    }
}
