/*
    Xpress Insight User Admin Cli
    ============================
    InsightAdminCliParser.java
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
package com.fico.xpress.commandline;

import com.fico.xpress.exception.ApplicationException;
import com.fico.xpress.utility.ReadConsolePassword;
import com.fico.xpress.vos.Command;
import com.fico.xpress.vos.InsightApplicationConfig;
import com.fico.xpress.vos.OperationType;
import com.fico.xpress.vos.Resource;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.fico.xpress.utility.InsightAdminCliConstants.ADD_APP;
import static com.fico.xpress.utility.InsightAdminCliConstants.ADD_AUTHORITY_GROUP;
import static com.fico.xpress.utility.InsightAdminCliConstants.ADD_AUTHORITY_GROUP_AUTHORITIES;
import static com.fico.xpress.utility.InsightAdminCliConstants.ADD_AUTHORITY_GROUP_DESC;
import static com.fico.xpress.utility.InsightAdminCliConstants.ADD_AUTHORITY_GROUP_NAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.DISABLED;
import static com.fico.xpress.utility.InsightAdminCliConstants.EMAIL;
import static com.fico.xpress.utility.InsightAdminCliConstants.ENABLED;
import static com.fico.xpress.utility.InsightAdminCliConstants.FIRST_NAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.HELP;
import static com.fico.xpress.utility.InsightAdminCliConstants.HOST_URL;
import static com.fico.xpress.utility.InsightAdminCliConstants.LAST_NAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.LOCAL_ACCOUNT_DISABLE;
import static com.fico.xpress.utility.InsightAdminCliConstants.LOCAL_ACCOUNT_ENABLE;
import static com.fico.xpress.utility.InsightAdminCliConstants.LOCKED;
import static com.fico.xpress.utility.InsightAdminCliConstants.MANDATORY_AUTHORITY_GROUP_NAME_ADD_MSG;
import static com.fico.xpress.utility.InsightAdminCliConstants.MANDATORY_FIRST_NAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.MANDATORY_LAST_NAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.MANDATORY_PASSWORD_MSG;
import static com.fico.xpress.utility.InsightAdminCliConstants.MANDATORY_TARGET_PASSWORD_ADD_MSG;
import static com.fico.xpress.utility.InsightAdminCliConstants.MANDATORY_TARGET_USERNAME_ADD_MSG;
import static com.fico.xpress.utility.InsightAdminCliConstants.MANDATORY_TARGET_USERNAME_MSG;
import static com.fico.xpress.utility.InsightAdminCliConstants.MANDATORY_USERNAME_MSG;
import static com.fico.xpress.utility.InsightAdminCliConstants.PASSWORD;
import static com.fico.xpress.utility.InsightAdminCliConstants.PRINT_HELP_LEFT_PADDING;
import static com.fico.xpress.utility.InsightAdminCliConstants.PRINT_HELP_WIDTH;
import static com.fico.xpress.utility.InsightAdminCliConstants.REMOVE_APP;
import static com.fico.xpress.utility.InsightAdminCliConstants.REMOVE_AUTHORITY_GROUP;
import static com.fico.xpress.utility.InsightAdminCliConstants.REMOVE_AUTHORITY_GROUP_AUTHORITIES;
import static com.fico.xpress.utility.InsightAdminCliConstants.TABLEAU_DISABLED;
import static com.fico.xpress.utility.InsightAdminCliConstants.TABLEAU_ENABLED;
import static com.fico.xpress.utility.InsightAdminCliConstants.TABLEAU_USERNAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.TARGET_PASSWORD;
import static com.fico.xpress.utility.InsightAdminCliConstants.TARGET_USER_NAME;
import static com.fico.xpress.utility.InsightAdminCliConstants.UNLOCKED;
import static com.fico.xpress.utility.InsightAdminCliConstants.USER_NAME;
import static com.google.common.collect.Maps.uniqueIndex;
import static com.google.common.collect.Sets.newTreeSet;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;

public class InsightAdminCliParser {

    private static final Option OPT_HELP = Option.builder(HELP)
        .longOpt("help")
        .desc("Help.")
        .build();
    private static final Option OPT_USER_NAME = Option.builder(USER_NAME)
        .longOpt("username")
        .desc("Username for user admin account (1-100 chars)")
        .hasArg()
        .argName("username")
        .build();
    private static final Option OPT_PASSWORD = Option.builder(PASSWORD)
        .longOpt("password")
        .desc("Password for user admin account (8-100 chars)")
        .optionalArg(true)
        .numberOfArgs(1)
        .argName("password")
        .build();
    private static final Option OPT_TARGET_USER = Option.builder(TARGET_USER_NAME)
        .longOpt("targetuser")
        .desc("Username for target account (1-100 chars)")
        .hasArg()
        .argName("targetuser")
        .build();
    private static final Option OPT_FIRST_NAME = Option.builder(FIRST_NAME)
        .longOpt("firstname")
        .desc("First name for target account (1-200 chars)")
        .hasArg()
        .argName("firstname")
        .build();
    private static final Option OPT_LAST_NAME = Option.builder(LAST_NAME)
        .longOpt("lastname")
        .desc("Last name for target account (1-200 chars)")
        .hasArg()
        .argName("lastname")
        .build();
    private static final Option OPT_NEW_USER_TARGET_PASSWORD = Option.builder(TARGET_PASSWORD)
        .longOpt("targetpassword")
        .desc("Password for target account (8-100 chars)")
        .hasArg()
        .argName("targetpassword")
        .build();
    private static final Option OPT_EMAIL = Option.builder(EMAIL)
        .longOpt("email")
        .desc("Email address for target account (1-200 chars)")
        .hasArg()
        .argName("email")
        .build();
    private static final Option OPT_USER_ADD_AUTHORITY_GROUP = Option.builder(ADD_AUTHORITY_GROUP)
        .longOpt("addauthoritygroup")
        .desc("Add authority group to target account, group must already exist")
        .hasArg()
        .argName("addauthoritygroup")
        .build();
    private static final Option OPT_ADD_APP = Option.builder(ADD_APP)
        .longOpt("addapp")
        .desc("Add app membership for target account, app must already exist")
        .hasArg()
        .argName("addapp")
        .build();
    private static final Option OPT_REMOVE_APP = Option.builder(REMOVE_APP)
        .longOpt("removeapp")
        .desc("Remove app membership for target account")
        .hasArg()
        .argName("removeapp")
        .build();

    private static final Option OPT_TABLEAU_USERNAME = Option.builder(TABLEAU_USERNAME)
        .longOpt("tableauusername")
        .desc("Set Tableau username for target account")
        .hasArg()
        .argName("tableauusername")
        .build();

    private static final Option OPT_USER_REMOVE_AUTHORITY_GROUP = Option.builder(REMOVE_AUTHORITY_GROUP)
        .longOpt("removeauthoritygroup")
        .desc("Remove authority group from target account")
        .hasArg()
        .argName("removeauthoritygroup")
        .build();

    private static final Option OPT_LOCAL_ACCOUNT_ENABLE = Option.builder(LOCAL_ACCOUNT_ENABLE)
        .longOpt("localaccountenable")
        .desc("Set the target account to be a local account")
        .hasArg(false)
        .build();
    private static final Option OPT_LOCAL_ACCOUNT_DISABLE = Option.builder(LOCAL_ACCOUNT_DISABLE)
        .longOpt("localaccountdisable")
        .desc("Set the target account to be a normal account")
        .hasArg(false)
        .build();
    private static final Option OPT_ENABLED = Option.builder(ENABLED)
        .longOpt("enabled")
        .desc("Enable target account")
        .hasArg(false)
        .build();
    private static final Option OPT_DISABLE = Option.builder(DISABLED)
        .longOpt("disabled")
        .desc("Disable target account")
        .hasArg(false)
        .build();
    private static final Option OPT_LOCKED = Option.builder(LOCKED)
        .longOpt("locked")
        .desc("Lock target account")
        .hasArg(false)
        .build();
    private static final Option OPT_UNLOCKED = Option.builder(UNLOCKED)
        .longOpt("unlocked")
        .desc("Unlock target account")
        .hasArg(false)
        .build();
    private static final Option OPT_TABLEAU_ENABLED = Option.builder(TABLEAU_ENABLED)
        .longOpt("tableauenabled")
        .desc("Enable target account for Tableau access")
        .hasArg(false)
        .build();
    private static final Option OPT_TABLEAU_DISABLED = Option.builder(TABLEAU_DISABLED)
        .longOpt("tableaudisabled")
        .desc("Disable target account for Tableau access")
        .hasArg(false)
        .build();

    private static final Option OPT_AUTHORITY_GROUP_NAME = Option.builder(ADD_AUTHORITY_GROUP_NAME)
        .longOpt("authoritygroupname")
        .desc("Authority Group Name")
        .hasArg()
        .argName("authoritygroupname")
        .build();

    private static final Option OPT_AUTHORITY_GROUP_DESCRIPTION = Option.builder(ADD_AUTHORITY_GROUP_DESC)
        .longOpt("authoritygroupdescription")
        .desc("Authority Group Description")
        .hasArg()
        .argName("authoritygroupdescription")
        .build();

    private static final Option OPT_AUTHORITY_GROUP_ADD_AUTHORITIES = Option.builder(ADD_AUTHORITY_GROUP_AUTHORITIES)
        .longOpt("addauthorities")
        .desc("Add Authority Group Authorities")
        .hasArg()
        .argName("addauthorities")
        .build();

    private static final Option OPT_AUTHORITY_GROUP_REMOVE_AUTHORITIES = Option.builder(REMOVE_AUTHORITY_GROUP_AUTHORITIES)
        .longOpt("removeauthorities")
        .desc("Remove Authority Group Authorities")
        .hasArg()
        .argName("removeauthorities")
        .build();

    private static final Option OPT_HOST_URL = Option.builder(HOST_URL)
        .longOpt("hosturl")
        .desc("Insight server to connect")
        .hasArg()
        .argName("hosturl")
        .build();

    private static final Options OPTS_HELP = new Options()
        .addOption(OPT_HELP);

    protected static final Options OPTS = new Options()
        .addOption(OPT_HELP)
        .addOption(OPT_USER_NAME)
        .addOption(OPT_PASSWORD)
        .addOption(OPT_TARGET_USER)
        .addOption(OPT_FIRST_NAME)
        .addOption(OPT_LAST_NAME)
        .addOption(OPT_NEW_USER_TARGET_PASSWORD)
        .addOption(OPT_LOCAL_ACCOUNT_ENABLE)
        .addOption(OPT_LOCAL_ACCOUNT_DISABLE)
        .addOption(OPT_EMAIL)
        .addOption(OPT_ENABLED)
        .addOption(OPT_DISABLE)
        .addOption(OPT_LOCKED)
        .addOption(OPT_UNLOCKED)
        .addOption(OPT_TABLEAU_ENABLED)
        .addOption(OPT_TABLEAU_DISABLED)
        .addOption(OPT_USER_ADD_AUTHORITY_GROUP)
        .addOption(OPT_ADD_APP)
        .addOption(OPT_REMOVE_APP)
        .addOption(OPT_TABLEAU_USERNAME)
        .addOption(OPT_USER_REMOVE_AUTHORITY_GROUP)
        .addOption(OPT_AUTHORITY_GROUP_NAME)
        .addOption(OPT_AUTHORITY_GROUP_DESCRIPTION)
        .addOption(OPT_AUTHORITY_GROUP_ADD_AUTHORITIES)
        .addOption(OPT_AUTHORITY_GROUP_REMOVE_AUTHORITIES)
        .addOption(OPT_HOST_URL);

    private static PrintStream out = System.out;
    private static ReadConsolePassword readConsolePassword = new ReadConsolePassword();

    /**
     * Method to create the command object from the string arguments entered.
     * @param args
     * @return command
     * @throws ParseException
     */
    public static Command parseCommandArgs(String... args) throws ParseException {
        Command command = new Command();
        CommandLineParser parser = new DefaultParser();
        command.setShowHelp(checkForHelpArg(args));
        if (command.isShowHelp()) {
            return command;
        }
        CommandLine cmdLine = parser.parse(OPTS, args );
        Iterator<String> itArgs = cmdLine.getArgList().iterator();
        command.setResource(next(Resource.class, itArgs));
        command.setOperationType(next(OperationType.class, itArgs));
        parseLoginCredentials(cmdLine, command);
        parseArguments(cmdLine, command);
        return command;
    }

    /**
     * Method to parse user credentials for login functionality.
     */
    public static void parseLoginCredentials(CommandLine cmdLine, Command command) {
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig();
        insightApplicationConfig.setUserName(validateOptionValue(USER_NAME, MANDATORY_USERNAME_MSG, cmdLine));
        insightApplicationConfig.setPassword(getPasswordFromConsole(cmdLine));
        insightApplicationConfig.setHostUrl(cmdLine.getOptionValue(HOST_URL));
        command.setInsightApplicationConfig(insightApplicationConfig);
    }

    /**
     * Method that prepares the command object based on resource and operation .
     * @param cmdLine
     * @param command
     */
    public static void parseArguments(CommandLine cmdLine, Command command) {
        if (Resource.USER == command.getResource()) {
            if (OperationType.DESCRIBE == command.getOperationType() || OperationType.UPDATE == command.getOperationType()) {
                command.getArguments().put(TARGET_USER_NAME, validateOptionValue(TARGET_USER_NAME,
                    MANDATORY_TARGET_USERNAME_MSG, cmdLine));
            }
            if (OperationType.ADD == command.getOperationType()) {
                parseArgumentsForUserAddOperation(cmdLine, command);
                checkForMultipleValueArgAddUser(command, cmdLine);
            } else if (OperationType.UPDATE == command.getOperationType()) {
                parseArgumentsForUpdateOperation(cmdLine, command);
                checkForMultipleValueArgAddUser(command, cmdLine);
            }
        }
        else{
            if (OperationType.ADD == command.getOperationType() || OperationType.UPDATE == command.getOperationType()) {
                parseArgumentsForAuthorityGroupAddOperation(cmdLine, command);
                checkForMultipleValueArgUpdateAuthorityGroup(command, cmdLine);
            } else if (OperationType.DESCRIBE == command.getOperationType()) {
                command.getArguments().put(ADD_AUTHORITY_GROUP_NAME, validateOptionValue(ADD_AUTHORITY_GROUP_NAME,
                    MANDATORY_AUTHORITY_GROUP_NAME_ADD_MSG, cmdLine));
            }
        }
        //all other arguments can be checked and put to the command arguments map
    }

    /**
     * Utility method that checks if help argument is present or not.
     * @param args
     * @return true if help is present in the command.
     * @throws ParseException
     */
    private static boolean checkForHelpArg(String... args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        while (args.length > 0) {
            CommandLine cli = parser.parse(OPTS_HELP, args, true);
            if (cli.hasOption(OPT_HELP.getLongOpt())) {
                return true;
            }
            // otherwise, first argument is unrecognized, skip it and keep going
            args = Stream.of(cli.getArgs()).skip(1).toArray(String[]::new);
        }
        return false;
    }

    /**
     * Utility method to prepare multiple value command arguments for ag/rg (authority groups), aa (apps).
     * @param command object.
     * @param cmdLine .
     */
    private static void checkForMultipleValueArgAddUser(Command command, CommandLine cmdLine) {
        if (cmdLine.hasOption(ADD_AUTHORITY_GROUP)) {
            command.getArguments().put(ADD_AUTHORITY_GROUP, Arrays.asList(cmdLine.getOptionValues(ADD_AUTHORITY_GROUP)));
        }
        if (cmdLine.hasOption(ADD_APP)) {
            command.getArguments().put(ADD_APP, Arrays.asList(cmdLine.getOptionValues(ADD_APP)));
        }
        if (cmdLine.hasOption(REMOVE_AUTHORITY_GROUP)) {
            command.getArguments().put(REMOVE_AUTHORITY_GROUP, Arrays.asList(cmdLine.getOptionValues(REMOVE_AUTHORITY_GROUP)));
        }
        if (cmdLine.hasOption(REMOVE_APP)) {
            command.getArguments().put(REMOVE_APP, Arrays.asList(cmdLine.getOptionValues(REMOVE_APP)));
        }
    }

    private static <E extends Enum<E>> String enumNames(Class<E> eClass) {
        return format("[%1$s]", join(newTreeSet(enumMap(eClass).keySet()), "|"));
    }

    private static String next(Iterator<String> itArgs, String wanted) {
        if (itArgs.hasNext()) {
            return itArgs.next();
        }
        throw new ApplicationException(format("Insufficient command-line parameters, expected: %1$s.", wanted));
    }

    private static <E extends Enum<E>> E next(Class<E> eClass, Iterator<String> itArgs) {
        return read(eClass, next(itArgs, enumNames(eClass)));
    }

    static <E extends Enum<E>> Map<String, E> enumMap(Class<E> eClass) {
        return uniqueIndex(asList(eClass.getEnumConstants()), e -> e.name().toLowerCase(Locale.ENGLISH));
    }

    static <E extends Enum<E>> E read(final Class<E> eClass, final String s) {
        Map<String, E> map = enumMap(eClass);
        return Optional.ofNullable(map.get(s)).<ApplicationException>orElseThrow(() -> {
            throw new ApplicationException(format("Invalid parameter: found '%1$s', expected one of %2$s.",
                s, enumNames(eClass)));
        });
    }

    /**
     * Prints the help onto the commandLine.
     */
    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(null); // to print in declaration order
        String syntax = join(asList("insightadmincli",
            enumNames(Resource.class),
            enumNames(OperationType.class)),
            " ");
        formatter.printHelp(new PrintWriter(out, true), PRINT_HELP_WIDTH,
            syntax, null, OPTS, formatter.getLeftPadding() , PRINT_HELP_LEFT_PADDING,
            null, true);
        return;
    }

    public static void setOut(PrintStream out) {
        InsightAdminCliParser.out = out;
    }

    public static void setReadConsolePassword(ReadConsolePassword readConsolePassword) {
        InsightAdminCliParser.readConsolePassword = readConsolePassword;
    }

    private static String validateOptionValue(String option, String errorMessage, CommandLine cmdLine) {
        if (!cmdLine.hasOption(option)) {
            throw new ApplicationException(errorMessage);
        }
        return cmdLine.getOptionValue(option);
    }

    /**
     * Parse the arguments for the User Add operation.
     * @param cmdLine
     * @param command
     */
    public static void parseArgumentsForUserAddOperation(CommandLine cmdLine, Command command) {
        command.getArguments().put(TARGET_USER_NAME, validateOptionValue(TARGET_USER_NAME,
            MANDATORY_TARGET_USERNAME_ADD_MSG, cmdLine));
        command.getArguments().put(FIRST_NAME, validateOptionValue(FIRST_NAME, MANDATORY_FIRST_NAME, cmdLine));
        command.getArguments().put(LAST_NAME, validateOptionValue(LAST_NAME, MANDATORY_LAST_NAME, cmdLine));
        //target new user password
        command.getArguments().put(TARGET_PASSWORD, validateOptionValue(TARGET_PASSWORD, MANDATORY_TARGET_PASSWORD_ADD_MSG,
            cmdLine));

        prepareBooleanOptionValues(cmdLine, command);

        //email
        if (cmdLine.hasOption(EMAIL)) {
            command.getArguments().put(EMAIL, cmdLine.getOptionValue(EMAIL));
        }

        //tableau Username
        populateOptionalFields(TABLEAU_USERNAME, cmdLine, command);
    }

    /**
     * Parse the arguments for the Authority Group Add operation.
     * @param cmdLine
     * @param command
     */
    public static void parseArgumentsForAuthorityGroupAddOperation(CommandLine cmdLine, Command command){
        //authority group name
        command.getArguments().put(ADD_AUTHORITY_GROUP_NAME, validateOptionValue(ADD_AUTHORITY_GROUP_NAME,
            MANDATORY_AUTHORITY_GROUP_NAME_ADD_MSG,
            cmdLine));

        //authority group description
        if (cmdLine.hasOption(ADD_AUTHORITY_GROUP_DESC)) {
            command.getArguments().put(ADD_AUTHORITY_GROUP_DESC, cmdLine.getOptionValue(ADD_AUTHORITY_GROUP_DESC));
        }
    }

    /**
     * Parse the arguments for the User Group Add operation.
     * @param cmdLine
     * @param command
     */
    public static void parseArgumentsForUpdateOperation(CommandLine cmdLine, Command command) {
        populateOptionalFields(FIRST_NAME, cmdLine, command);
        populateOptionalFields(LAST_NAME, cmdLine, command);
        populateOptionalFields(TARGET_PASSWORD, cmdLine, command);
        populateOptionalFields(EMAIL, cmdLine, command);
        populateOptionalFields(TABLEAU_USERNAME, cmdLine, command);
        prepareBooleanOptionValues(cmdLine, command);
    }

    /**
     *Utility method that checks whether optional field is present and if present adds it to the command object.
     * @param option
     * @param cmdLine
     * @param command
     */
    private static void populateOptionalFields(String option, CommandLine cmdLine, Command command) {
        if (cmdLine.hasOption(option)) {
            command.getArguments().put(option, cmdLine.getOptionValue(option));
        }
    }

    /**
     * Utility method to validate the boolean argument and add it to the command object.
     * @param trueOption
     * @param falseOption
     * @param errorMessage
     * @param cmdLine
     * @param command
     * @return
     */
    private static boolean populateAndValidateBooleanValue(String trueOption, String falseOption, String errorMessage, CommandLine cmdLine, Command command) {
        if (cmdLine.hasOption(trueOption) && cmdLine.hasOption(falseOption)) {
            throw new ApplicationException(errorMessage);
        } else if (cmdLine.hasOption(trueOption)) {
            command.getArguments().put(trueOption, true);
        } else if (cmdLine.hasOption(falseOption)) {
            command.getArguments().put(falseOption, false);
        }
        return false;
    }

    /**
     * Utility method to check add and remove for an argument present simultaneously or not and also
     * supplies the error message to populateAndValidateBooleanValue method.
     * @param cmdLine
     * @param command
     */
    private static void prepareBooleanOptionValues(CommandLine cmdLine, Command command) {
        //local account
        populateAndValidateBooleanValue(LOCAL_ACCOUNT_ENABLE, LOCAL_ACCOUNT_DISABLE,
            "Local Account enable and disable option cannot be present simultaneously.",
            cmdLine, command);

        //enabled
        populateAndValidateBooleanValue(ENABLED, DISABLED,
            "Enable and Disable option cannot be present simultaneously.",
            cmdLine, command);

        //locked
        populateAndValidateBooleanValue(LOCKED, UNLOCKED,
            "Locked and Unlocked option cannot be present simultaneously.",
            cmdLine, command);

        //tableau Enabled
        populateAndValidateBooleanValue(TABLEAU_ENABLED, TABLEAU_DISABLED,
            "Tableau enable and disable option cannot be present simultaneously.",
            cmdLine, command);
    }

    /**
     * Utility method to populate the multiple value arguments for authority group.
     * @param command
     * @param cmdLine
     */
    private static void checkForMultipleValueArgUpdateAuthorityGroup(Command command, CommandLine cmdLine) {
        if (cmdLine.hasOption(ADD_AUTHORITY_GROUP_AUTHORITIES)) {
            command.getArguments().put(ADD_AUTHORITY_GROUP_AUTHORITIES, Arrays.asList(cmdLine.getOptionValues(ADD_AUTHORITY_GROUP_AUTHORITIES)));
        }
        if (cmdLine.hasOption(REMOVE_AUTHORITY_GROUP_AUTHORITIES)) {
            command.getArguments().put(REMOVE_AUTHORITY_GROUP_AUTHORITIES, Arrays.asList(cmdLine.getOptionValues(REMOVE_AUTHORITY_GROUP_AUTHORITIES)));
        }
    }

    /**
     * Utility method to check whether password is supplied in the command. If not it prompts the user to do so.
     * @param cmdLine
     * @return
     */
    private static String getPasswordFromConsole(CommandLine cmdLine) {
        String password = cmdLine.getOptionValue(PASSWORD);
        if (password == null || password.trim().isEmpty()) {
            password = readConsolePassword.readPassword();
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ApplicationException(MANDATORY_PASSWORD_MSG);
        }
        return password;
    }
}
