/*
    Xpress Insight User Admin Cli
    ============================
    DescribeUserOperationResultTest.java
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
import com.fico.xpress.vos.Project;
import com.fico.xpress.vos.UserInfoExtended;
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
public class DescribeUserOperationResultTest {

    @InjectMocks
    private DescribeUserOperationResult describeUserOperationResult;

    @Test
    public void testGetAuthorityGroups() {
        Collection<AuthorityGroup> authorityGroups = new ArrayList<>();
        authorityGroups.add(new AuthorityGroup("Developer", true));
        authorityGroups.add(new AuthorityGroup("SystemAdministration", false));
        authorityGroups.add(new AuthorityGroup("BasicUser", true));
        String expected = "Developer, BasicUser";
        assertEquals(expected, describeUserOperationResult.getAuthorityGroups(authorityGroups));

    }

    @Test
    public void testGetProjects() {
        Collection<Project> projects = new ArrayList<>();
        projects.add(new Project(null, "QuickStart", true));
        projects.add(new Project(null, "QuickStart2", true));
        projects.add(new Project(null, "QuickStart3", false));
        String expected = "QuickStart, QuickStart2";
        assertEquals(expected, describeUserOperationResult.getProjects(projects));
    }

    @Test
    public void testPrintWithOrderCheck() {
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuf, true);
        Collection<AuthorityGroup> authorityGroups = new ArrayList<>();
        authorityGroups.add(new AuthorityGroup("Developer", true));
        authorityGroups.add(new AuthorityGroup("SystemAdministration", true));
        Collection<Project> projects = new ArrayList<>();
        projects.add(new Project(null, "Quick Start", true));
        projects.add(new Project(null, "Quick Start (2)", true));
        UserInfoExtended userInfoExtended = new UserInfoExtended("admin",
            "Administrator",
            "User",
            true,
            "admin",
            "abc@gmail.com",
            true,
            false,
            true,
            null,
            null,
            authorityGroups,
            projects);
        describeUserOperationResult.setUserInfoExtended(userInfoExtended);
        describeUserOperationResult.print(out);

        String[] output = new String(outBuf.toByteArray()).split("\r\\n|:");
        Arrays.stream(output).map(String::trim).toArray(unused -> output);
        String arr[] = {
            "User Name", "admin",
            "First Name", "Administrator",
            "Last Name", "User",
            "Email", "abc@gmail.com",
            "Tableau Enabled", "true",
            "Tableau Username", "admin",
            "Enabled", "true",
            "Locked", "false",
            "Local Account", "true",
            "Authority Groups", "Developer, SystemAdministration",
            "Apps", "Quick Start, Quick Start (2)"};
        assertArrayEquals(output, arr);
    }
}
