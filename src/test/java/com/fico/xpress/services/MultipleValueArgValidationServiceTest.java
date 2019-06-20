/*
    Xpress Insight User Admin Cli
    ============================
    MultipleValueArgValidationServiceTest.java
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
package com.fico.xpress.services;

import com.fico.xpress.exception.ApplicationException;
import com.fico.xpress.vos.AuthorityGroup;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.fico.xpress.services.MultipleValueArgValidationService.populateAndValidateMultipleValueArgs;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertThat;

public class MultipleValueArgValidationServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testPopulateAndValidateMultipleValueArgs() {
        List<String> addArgumentValues = Arrays.asList("Developer", "AdvancedUser");
        List<String> removeArgumentValues = Arrays.asList("SystemAdministration");
        List<AuthorityGroup> existingAuthList = new ArrayList<>();
        existingAuthList.add(new AuthorityGroup("Developer", false));
        existingAuthList.add(new AuthorityGroup("AdvancedUser", false));
        existingAuthList.add(new AuthorityGroup("SystemAdministration", true));

        Set<AuthorityGroup> authList = populateAndValidateMultipleValueArgs(addArgumentValues, existingAuthList, removeArgumentValues,
            AuthorityGroup::new, AuthorityGroup::getName, "Authority Group");
        assertThat(authList, hasItem (hasProperty("name", is("AdvancedUser"))));
        assertThat(authList, hasItem (hasProperty("name", is("Developer"))));
        assertThat(authList, hasItem (hasProperty("selected", is(true))));
    }

    @Test
    public void testValidateAddArgs() {
        List<String> addArgumentValues = Arrays.asList("Developer1");
        List<AuthorityGroup> existingAuthList = new ArrayList<>();
        existingAuthList.add(new AuthorityGroup("Developer", false));
        existingAuthList.add(new AuthorityGroup("AdvancedUser", false));
        existingAuthList.add(new AuthorityGroup("SystemAdministration", true));

        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Invalid Authority Group [Developer1].");
        populateAndValidateMultipleValueArgs(addArgumentValues, existingAuthList, null,
            AuthorityGroup::new, AuthorityGroup::getName, "Authority Group");
    }

    @Test
    public void testValidateRemoveArgs() {
        List<String> removeArgumentValues = Arrays.asList("SystemAdministration1");
        List<AuthorityGroup> existingAuthList = new ArrayList<>();
        existingAuthList.add(new AuthorityGroup("Developer", false));
        existingAuthList.add(new AuthorityGroup("AdvancedUser", false));
        existingAuthList.add(new AuthorityGroup("SystemAdministration", true));

        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Invalid Authority Group [SystemAdministration1].");
        populateAndValidateMultipleValueArgs(null, existingAuthList, removeArgumentValues,
            AuthorityGroup::new, AuthorityGroup::getName, "Authority Group");
    }

    @Test
    public void testValidateAddAndRemoveArgs() {
        List<String> addArgumentValues = Arrays.asList("Developer");
        List<String> removeArgumentValues = Arrays.asList("Developer");
        List<AuthorityGroup> existingAuthList = new ArrayList<>();
        existingAuthList.add(new AuthorityGroup("Developer", false));
        existingAuthList.add(new AuthorityGroup("AdvancedUser", false));
        existingAuthList.add(new AuthorityGroup("SystemAdministration", true));

        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Duplicate Authority Group exists in add and remove Authority Group arguments.");
        populateAndValidateMultipleValueArgs(addArgumentValues, existingAuthList, removeArgumentValues,
            AuthorityGroup::new, AuthorityGroup::getName, "Authority Group");
    }

    @Test
    public void testPrepareUniqueMultipleValueArgToAdd() {
        List<String> removeArgumentValues = Arrays.asList(new String[]{"Developer", "AdvancedUser"});
        List<AuthorityGroup> existingAuthList = new ArrayList<>();
        existingAuthList.add(new AuthorityGroup("Developer", false));
        existingAuthList.add(new AuthorityGroup("AdvancedUser", false));
        existingAuthList.add(new AuthorityGroup("SystemAdministration", true));

        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Removing a non selected Authority Group [Developer, AdvancedUser].");
        populateAndValidateMultipleValueArgs(null, existingAuthList, removeArgumentValues,
            AuthorityGroup::new, AuthorityGroup::getName, "Authority Group");
    }
}
