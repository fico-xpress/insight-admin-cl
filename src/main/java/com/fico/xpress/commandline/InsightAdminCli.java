/*
    Xpress Insight User Admin Cli
    ============================
    InsightAdminCli.java
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

import com.fico.xpress.config.InsightAdminCliConfig;
import com.fico.xpress.exception.ApplicationException;
import com.fico.xpress.services.AuthorityGroupService;
import com.fico.xpress.services.UserService;
import com.fico.xpress.vos.Command;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.fico.xpress")
public class InsightAdminCli {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityGroupService authorityGroupService;

    private void showBanner() {
        System.out.println("=========================================");
        System.out.println(" FICO Xpress Insight administration tool ");
        System.out.println("=========================================");
    }

    public static void main(String[] args) {
        InsightAdminCli cmd = InsightAdminCliConfig.initializeInsightAdminCliApplicationContext();
        cmd.processCommand(args);
    }

    /**
     * Method that takes in string arguments, calls parseCommandArgs to create command object
     * and checks if help argument is present then call printHelp.
     * @param args
     */
    protected void processCommand (String[] args) {
        try {
            Command command = InsightAdminCliParser.parseCommandArgs(args);
            if (command.isShowHelp()) {
                showBanner();
                InsightAdminCliParser.printHelp();
                return;
            }
            processResource(command);
        } catch (ParseException exp) {
            System.err.println(exp.getMessage() + ". Process finished with exit code 1");
        } catch (ApplicationException exp) {
            System.err.println(exp.getMessage() + " Process finished with exit code 1");
        }
    }

    /**
     * Method to call the service method according to the resource in the command.
     * @param command
     */
    protected void processResource(Command command) {
        System.out.println("***** "+command.getResource()+" "+command.getOperationType()+" OPERATION BEGIN *****");
        switch (command.getResource()) {
            case USER:
                userService.processUserCommand(command).print(System.out);
                break;
            case AUTHGROUP:
                authorityGroupService.processAuthorityGroupCommand(command).print(System.out);
                break;
        }
        System.out.println("**** "+command.getResource()+" "+command.getOperationType()+" OPERATION END *****");
    }

}
