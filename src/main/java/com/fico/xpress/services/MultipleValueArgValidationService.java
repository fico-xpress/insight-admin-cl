/*
    Xpress Insight User Admin Cli
    ============================
    MultipleValueArgValidationService.java
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
import com.fico.xpress.vos.ExtendedMultipleValueArg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.lang.String.format;

public class MultipleValueArgValidationService {

    /**
     * To populate and validate multiple argument values.
     *
     * @param addArgumentValues
     * @param existingCollection
     * @param removeArgumentValues
     * @param constructor
     * @param propertyName
     * @param messageType
     * @param <T>
     */
    public static <T extends ExtendedMultipleValueArg> Set<T> populateAndValidateMultipleValueArgs(List<String> addArgumentValues,
                                                                                                   Collection<T> existingCollection,
                                                                                                   List<String> removeArgumentValues,
                                                                                                   Function<String, T> constructor,
                                                                                                   Function<T, String> propertyName,
                                                                                                   String messageType) {
        Set<T> uniqueResourceResult = null;
        Set<T> uniqueExistingCollection;
        if (existingCollection != null && !existingCollection.isEmpty()) {
            Set<T> uniqueResourceForAdd = new HashSet<>();
            Set<String> addUniqueArgumentValues = null;
            Set<String> removeUniqueArgumentValues = null;
            uniqueExistingCollection = new HashSet<>(existingCollection);
            List<String> invalidArgumentList = new ArrayList<>();
            if (addArgumentValues != null) {
                addUniqueArgumentValues = new HashSet<>(addArgumentValues);
                validateAddArgs(addUniqueArgumentValues, uniqueExistingCollection, invalidArgumentList, uniqueResourceForAdd, constructor);
            }
            if (removeArgumentValues != null) {
                removeUniqueArgumentValues = new HashSet<>(removeArgumentValues);
                validateRemoveArgs(removeUniqueArgumentValues, uniqueExistingCollection, invalidArgumentList, constructor);
            }
            validateAddAndRemoveArgs(addUniqueArgumentValues, removeUniqueArgumentValues, messageType);

            if (!invalidArgumentList.isEmpty()) {
                throw new ApplicationException(format("Invalid %1$s %2$s.", messageType, invalidArgumentList));
            }

            uniqueResourceResult = prepareUniqueMultipleValueArgToAdd(removeUniqueArgumentValues,
                    uniqueResourceForAdd, uniqueExistingCollection, propertyName, messageType);
        } else if (addArgumentValues != null || removeArgumentValues != null) {
            throw new ApplicationException(format("Currently no valid %1$s exists, Please try Add/Update without any %1$s.", messageType));
        }
        return uniqueResourceResult;
    }

    /**
     * Utility method to validate arguments while adding from command line.
     *
     * @param addUniqueArgumentValues
     * @param uniqueExistingCollection
     * @param invalidArgumentList
     * @param uniqueResourceForAdd
     * @param constructor
     * @param <T>
     */
    private static <T> void validateAddArgs(Set<String> addUniqueArgumentValues, Set<T> uniqueExistingCollection, List<String> invalidArgumentList, Set<T> uniqueResourceForAdd, Function<String, T> constructor) {
        T commandResourceObj;
        for (String argument : addUniqueArgumentValues) {
            commandResourceObj = constructor.apply(argument);
            if (!uniqueExistingCollection.contains(commandResourceObj)) {
                invalidArgumentList.add(argument);
            } else if (invalidArgumentList.isEmpty()) {
                uniqueResourceForAdd.add(commandResourceObj);
            }
        }
    }

    /**
     * To validate arguments while removing from command line.
     *
     * @param removeUniqueArgumentValues
     * @param uniqueExistingCollection
     * @param invalidArgumentList
     * @param constructor
     * @param <T>
     */
    private static <T> void validateRemoveArgs(Set<String> removeUniqueArgumentValues, Set<T> uniqueExistingCollection, List<String> invalidArgumentList, Function<String, T> constructor) {
        T commandResourceObj;
        for (String argument : removeUniqueArgumentValues) {
            commandResourceObj = constructor.apply(argument);
            if (!uniqueExistingCollection.contains(commandResourceObj)) {
                invalidArgumentList.add(argument);
            }
        }
    }

    /**
     * To validate duplicate arguments values present while adding and removing from command line.
     *
     * @param addUniqueArgumentValues
     * @param removeUniqueArgumentValues
     * @param errorMessage
     */
    private static void validateAddAndRemoveArgs(Set<String> addUniqueArgumentValues, Set<String> removeUniqueArgumentValues, String errorMessage) {
        if (removeUniqueArgumentValues != null) {
            if (addUniqueArgumentValues != null && addUniqueArgumentValues.removeAll(removeUniqueArgumentValues)) {
                throw new ApplicationException(format("Duplicate %1$s exists in add and remove %1$s arguments.", errorMessage));
            }
        }
    }

    /**
     * To prepare a list of resource while adding or updating multiple value arguments.
     *
     * @param removeUniqueArgumentValues
     * @param uniqueResourceToAdd
     * @param uniqueExistingCollection
     * @param propertyName
     * @param messageType
     * @param <T>
     */
    private static <T extends ExtendedMultipleValueArg> Set<T> prepareUniqueMultipleValueArgToAdd(Set<String> removeUniqueArgumentValues,
                                                                                                  Set<T> uniqueResourceToAdd,
                                                                                                  Set<T> uniqueExistingCollection,
                                                                                                  Function<T, String> propertyName, String messageType) {
        List<String> nonSelectedResource = new ArrayList<>();
        Set<T> uniqueResourceResult = new HashSet<>();
        for (T existingObj : uniqueExistingCollection) {
            if (removeUniqueArgumentValues != null) {
                if (!removeUniqueArgumentValues.contains(propertyName.apply(existingObj)) && (existingObj.getSelected() || uniqueResourceToAdd.contains(existingObj))) {
                    existingObj.setSelected(true);
                    uniqueResourceResult.add(existingObj);
                } else if (removeUniqueArgumentValues.contains(propertyName.apply(existingObj)) && !existingObj.getSelected()) {
                    nonSelectedResource.add(propertyName.apply(existingObj));
                }
            } else {
                if (existingObj.getSelected() || uniqueResourceToAdd.contains(existingObj)) {
                    existingObj.setSelected(true);
                    uniqueResourceResult.add(existingObj);
                }
            }
        }
        if (!nonSelectedResource.isEmpty()) {
            throw new ApplicationException(format("Removing a non selected %1$s %2$s.", messageType, nonSelectedResource));
        }
        return uniqueResourceResult;
    }
}
