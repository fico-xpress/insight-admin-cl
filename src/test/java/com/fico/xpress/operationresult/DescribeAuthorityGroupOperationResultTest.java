/*
    Xpress Insight User Admin Cli
    ============================
    DescribeAuthorityGroupOperationResultTest.java
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

import com.fico.xpress.vos.AuthorityGroupExtended;
import com.fico.xpress.vos.AuthorityListItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DescribeAuthorityGroupOperationResultTest {

    @InjectMocks
    private DescribeAuthorityGroupOperationResult describeAuthorityGroupOperationResult;

    @Test
    public void testGetAuthorities() {
        Collection<AuthorityListItem> authorityListItems = new ArrayList<>();
        authorityListItems.add(new AuthorityListItem("DEVELOPER", true));
        authorityListItems.add(new AuthorityListItem("APP_NEW", false));
        authorityListItems.add(new AuthorityListItem("APP_ALL", true));
        String expected = "DEVELOPER, APP_ALL";
        assertEquals(expected, describeAuthorityGroupOperationResult.getAuthorities(authorityListItems));
    }

    @Test
    public void testPrintWithOrderCheck() {
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuf, true);
        Collection<AuthorityListItem> authorities = new ArrayList<>();
        authorities.add(new AuthorityListItem("DEVELOPER", true));
        authorities.add(new AuthorityListItem("APP_ALL", true));
        AuthorityGroupExtended authorityGroupExtended = new AuthorityGroupExtended("AdvancedUser",
                "Authority set for advanced users",
                authorities);
        DescribeAuthorityGroupOperationResult describeAuthorityGroupOperationResult = new DescribeAuthorityGroupOperationResult(authorityGroupExtended);
        describeAuthorityGroupOperationResult.print(out);

        String[] output = new String(outBuf.toByteArray()).split("\r\\n|:");
        Arrays.stream(output).map(String::trim).toArray(unused -> output);
        String arr[] = {"Authority Group Name", "AdvancedUser", "Authority Group Description", "Authority set for advanced users",
            "Authorities", "DEVELOPER, APP_ALL"};
        assertArrayEquals(output, arr);
    }
}
