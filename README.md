# Xpress Insight User Admin Command Line

The Xpress Insight Admin application allows the users to perform **add**, **update**, **delete**, **list** and **describe** operations on **Users** and **Authority Groups** through the web app. This tool enables users to also perform these operations through command line.


#### Features of Insight admin command line 

* Can run on both `Windows` and `Linux` operating systems.
* Has built-in `help` support for the tool.
* Supports operation using http and https services.
* Has 96% test coverage.


#### Pre-requisites

* Java 11 
* JAVA_HOME environment variable should point to jdk11 installation path.
* Maven
 
 
#### Setup Instructions 
* Precompiled runnable artifact (insightadmincli.jar) is available in the target. Follow the instructions given below to regenerate.
In the console, enter the following commands, and press return after each one. 

1. git clone https://github.com/fico-xpress/insight-admin-cl.git
2. cd insight-admin-cl
3. mvn clean install (include the -DskipTests option to skip running the tests)
This will build a jar file. To run the jar file, type the following command in the console, and press enter:
**For Windows**
    `insightadmincli.bat -h`
    
**For Linux**
    `./insightadmincli.sh -h`      
   
    
#### Operations Supported :

* **List** - Return the list of all users/authority groups.
* **Describe** - Return the complete details of a specific user/authority group.
* **Add** - Create the specific user/authority group.
* **Update** - Edit the specific user/authority group.
	
#### Help command :
Enter the following command to open the Insight admin cli Help file detailing all the operations that can be performed:
`insightadmincli -h` or `insightadmincli --help`

#### Running on supported operating systems :
To run insight admin cli:
* On Windows, use insightadmincli.bat
* On Linux, use insightadmincli.sh


#### Setup instructions consuming the https service:
**For Windows**
There are two commands that can be used to install the certificates required to consume `https` services:
1)    keytool -import -file $PATH_OF_THE_CERTIFICATE_CER_FILE.cer -alias $ALIAS_NAME -keystore  $JAVA_HOME\lib\security\cacerts
2)    keytool -importcert -file $PATH_OF_THE_CERTIFICATE_CER_FILE.cer -alias $ALIAS_NAME -keystore  $JAVA_HOME\lib\security\cacerts

      For example:
      keytool -import -file C:\dev\certificate\xpressdev.cer -alias xpressdev -keystore  C:\jdks\openjdk-11.0.2_windows-x64_bin\jdk-11.0.2\lib\security\cacerts

	  When prompted for keystore password enter: `changeit`

**For Linux**
Execute the following command to install the certificates required to consume `https` services:

    keytool -importcert -file $PATH_OF_THE_CERTIFICATE_CER_FILE.cer -alias $ALIAS_NAME -keystore  $JAVA_HOME\lib\security\cacerts

    For example:
    sudo keytool -importcert /home/user/certificate/xpressdev.cer -alias comodo_new -keystore /etc/ssl/certs/java/cacerts

	When prompted for keystore password enter: `changeit`

#### Usage
**** Note
     In all the following examples, text prefixed with $ should be replaced with the actual value.
	 
* To list all the users in the system.        
  insightadmincli user list -u $loginuser -p $loginpassword -url $url        

  For example: 
  insightadmincli user list -u admin -p pwd123 -url https://insight4-ficoanalyticcloud.com

* To return the details for a specific user 
  insightadmincli user  describe -tu $targetuser -u $loginuser -p $loginpassword -url https://insight4-ficoanalyticcloud.com
  
  For example: 
  insightadmincli user describe -tu henry -u admin -p pwd123 -url https://insight4-ficoanalyticcloud.com will return details for the user 'henry'.        

* To add a new user 
  insightadmincli user add -tu $targetUser -fn $fNameValue -ln $lNameValue -tp $passwordvalue -em $email -en -lk -tbe -tbu $tableauUserName -ag $AddAuthorityGroupValue -aa $AddAppValue -u $loginuser -p $loginpassword -lad -url $url

  For example: 
  insightadmincli user add -tu demoUser -fn fName -ln lName -tp pwd123 -em test@gmail.com -en -lk -tbe -tbu DemoUser -ag Developer -aa "Quick Start" -u admin -p pwd123 -lad -url http://localhost:8860 will create a new user with the values provided.        

* To update an existing user
  insightadmincli user update -tu $targetUser -fn $fNameValue -ln $lNameValue -tp $passwordvalue -em $email -en -lk -tbe -tbu $tableauUserName -ag $AddAuthorityGroupValue -aa $AddAppValue -u $loginuser -p $loginpassword -lad -url $url

  For example:
  insightadmincli user update -tu demoUser -fn fName -ln lName -tp pwd123 -em test@gmail.com -en -lk -tbe -tbu DemoUser -ag Developer -aa "Quick Start" -u admin -p pwd123 -lad -url http://localhost:8860 will update an exisiting user with the values provided.        

** Similar operations can be performed on authority groups by replacing the keyword `user` with `authgroup` in the preceeding commands . 

      
#### Executing on Jenkins
To execute bulk operations, you can run scripts on Jenkins. In this case the username and password can be set using credentials provided by Jenkins.
You need to add a credential and then link it to the Username and Password Variables under Bindings(inside the configure job).
This method supplies the username and password to each command without compromising security, as the password is stored in encrypted form. 

In the following examples, `User` and `Pass` should be replaced with Username and Password variable names from Jenkins:
**For Windows**
call insightadmincli.bat user list -u %$UserName% -p %$Password%
call insightadmincli.bat user list -u %User% -p %Pass%
call insightadmincli.bat authgroup list -u %User% -p %Pass%
call insightadmincli.bat user describe -tu admin -u %User% -p %Pass%
    
**For Linux**	
insightadmincli.sh user list -u %$UserName% -p %$Password%;
insightadmincli.sh user list -u $User -p $Pass; 
insightadmincli.sh authgroup list -u $User -p $Pass;
insightadmincli.sh user describe -tu admin -u $User -p $Pass;

For the exact output, you need to create the log file and redirect the output to it as follows:
    
**For Windows**
    executeScript.bat >> test.log
	
**For Linux**
    executeScript.sh > test.log
	
where test.log is the file where all the logs will be written and executeScript contains the bulk commands.

**** Note
     The automation script can also be provided with absolute path.

     For example:
     executeScript.bat >> C:/logs/test.log
    