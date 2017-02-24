[![Build Status](https://travis-ci.org/grantlittle/bdd-reporting.png)](https://travis-ci.org/grantlittle/bdd-reporting)

# BDD-Reporting Service

The aim of this project is to create a centralised view of all 
projects in an organisation that use BDD tooling such as Cucumber 
or SpecFlow.

Although the the output from each of these tools provides the information
needed, it doesn't provide a consolidated view of all projects which makes
it easy for organisations to see the overall quality of the software they
are creating.

This project is still very much in the early stages of development and features
will be implemented based on end customer requirements. If there is some feature
that you would like from the tool then please raise a request.

## Installation

This project does not currently provide a standalone server that you can simply "run". 
Instead a simple standalone Spring Boot application can be created using something 
like the [Spring Initializr](http://start.spring.io). This allows companies to apply  
whatever internal security and configuration they want without imposing those 
requirements on the BDD-Reporting service.

It is possible that in the future we may provide an executable "demo" server with no security etc included, but that is 
currently still on the planning board.

To install and initiate the framework, you will need to add the following 
dependency into your build via something like Maven or Gradle

```xml
<dependency>
    <groupId>com.github.grantlittle</groupId>
    <artifactId>bdd-reporting-service</artifactId>
    <version>0.1.9</version>
</dependency>
```

With the required dependency in place. You should then be able to initiate the services 
by adding the following to the top of a ```Configuration``` class

```text
@EnableBddReporting
```

for example

```java
@Configuration
@EnableBddReporting
public class MyApplicationConfiguration {
   
}
```

Currently the implementation of BDD-Reporting runs an embedded Elasticsearch cluster and in
in memory database. That is because the product is not considered ready for production yet 
and when it reaches this point, it will require an external database and Elasticsearch cluster
to be pre-configured. However embedded functionality allows for easy testing and demo at this
early phase of the project

## Usage

### Cucumber

#### Uploading Cucumber Reports

Currently the tool only supports cucumber reports in the json format. Therefore you
will need to configure your CucumberOptions to output in this format. Please see
the [Cucumber documentation](https://cucumber.io/docs/reference/jvm#configuration) 
on how to do this, but as a quick demonstration on
how to do this, you can use something like the following (used within Maven):
```
@CucumberOptions(plugin = arrayOf("json:target/cucumber-report/FeatureReport.json"))
```
To upload you files to the BDD Reporting tool. You can simply use curl:-

```bash
curl -X PUT --upload-file /path/to/reports/FeatureReport.json -H "Content-Type:application/json" http://bdd-reporting-server/api/features/1.0/cucumber
```

If you want to add specific properties to the upload which make it possible to 
create specific dashboards, then include the BDD-Reporting-Properties header. 

Here is an example:

```bash
curl -X PUT --upload-file /path/to/reports/FeatureReport.json -H "Content-Type:application/json" -H "BDD-Reporting-Properties: environment=dev,build=1.1.1" http://bdd-reporting-server/api/features/1.0/cucumber
```

#### Integration with Apache Maven

Currently there isn't a custom Maven Plugin that integrates with the BDD-Reporting service (although one has been suggested). 
However it is possible to use the [maven-exec-plugin](http://www.mojohaus.org/exec-maven-plugin/)
 
```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <version>1.5.0</version>
    <executions>
        <execution>
            <phase>integration-test</phase>
            <goals>
                <goal>exec</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <executable>curl</executable>
        <!-- optional -->
        <workingDirectory>${build.directory}</workingDirectory>
        <arguments>
            <argument>--upload-file</argument>
            <argument>cucumber-report/SearchTests.json</argument>
            <argument>-H</argument>
            <argument>Content-Type:application/json</argument>
            <argument>-H</argument>
            <argument>BDD-Reporting-Properties: environment=dev,build=${project.version}</argument>
            <argument>http://localhost:8080/api/features/1.0/cucumber</argument>
        </arguments>
    </configuration>
</plugin>
``` 

### SpecFlow

SpecFlow test runs can be reported on in a number of different ways. The BDD-Reporting service supports some of these, 
namely:-

1. NUnit XML output
2. NUnit output run through the Pickles reporting tool to produce JSON

Reports generated using SpecFlow's own specflow.exe reporting tool is currently not supported.


#### Using NUnit Reports

SpecFlow reports created with NUnit can be uploaded to the BDD-Reporting service, however the standard output is less 
verbose than a standard Cucumber output. For example Scenario descriptions are missing from the output. So although they 
are supported it's generally better to use the Pickles tool to generate JSON output, which the BDD-Reporting service also
supports.
 

Generally to create a NUnit3 test report from the your SpecFlow tests you need to run the nunit3-console command. 
More information on how to do this can be found. You will need to include the relevant assemblies in your project to 
be able to execute the tests 

```commandline
c:\Projects\MySolution\SpecflowProject>..\packages\NUnit.ConsoleRunner.3.5.0\tools\nunit3-console.exe SpecflowProject.csproj
```

This will generally create an output file called TestResult.xml. This can be uploaded to the BDD-Reporting service

For example:

```bash
curl -X PUT --upload-file /path/to/reports/TestResult.xml -H "Content-Type:text/plain" -H "BDD-Reporting-Properties: environment=dev,build=1.1.1" http://bdd-reporting-server/api/features/1.0/nunit
```
 
#### Uploading Pickles Reports

It's also possible to upload JSON reports creates from SpecFlow using the Pickles tools. For documentation on how to
do this have a look at the [Pickles documentation](http://docs.picklesdoc.com/en/latest/).
 
Here is an example:


```bash
curl -X PUT --upload-file /path/to/reports/Pickles.json -H "Content-Type:application/json" -H "BDD-Reporting-Properties: environment=dev,build=1.1.1" http://bdd-reporting-server/api/features/1.0/pickles
```

## Security

Security is rightly a major concern for many companies and their needs vary widely depending on their particular industry 
and the products or services they produce. It is likely that your BDD Features and Scenarios are commercially sensitive and you don't want anybody being able 
to see your reports.

With such varying needs it would be extremely difficult to create a good BDD-Reporting service that meets everybody's needs. 
Therefore I have decided to provide the BDD-Reporting service as a library (service) rather than a server. 
This does mean it is typically not possible to simply "run" a server. You will need to do a little work to get the service up 
and running, however this should provide more flexibility to allow companies to implement security as they wish to meet their
companies individual needs and requirements.

For some examples on how to setup security within a Spring Boot application, have a look at some of these tutorials:-

1. [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
2. [Spring Boot](https://spring.io/guides/tutorials/spring-boot-oauth2/)
3. [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)

## Screenshots

### Dashboard

![Alt text](github/images/dashboard.png?raw=true "Dashboard")

### Viewing Features

![Alt text](github/images/features.png?raw=true "Features")

### Viewing a Single Feature

![Alt text](github/images/feature.png?raw=true "Feature")