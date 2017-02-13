[![Build Status](https://travis-ci.org/grantlittle/bdd-reporting.png)](https://travis-ci.org/grantlittle/bdd-reporting)

# BDD Enterprise Reporting

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
requirements on the BDD-Reporting tool.

To install and initiate the framework, you will need to add the following 
dependency into your build via something like Maven or Gradle

```xml
    <dependency>
        <groupId>com.github.grantlittle</groupId>
        <artifactId>bdd-reporting-server</artifactId>
        <version>0.1.6</version>
    </dependency>
```

With the required dependency in place. You should then be able to initiate the services 
by adding the following to the top of a Configuration class

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

##Usage

###Uploading Cucumber Reports

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
curl -X PUT --upload-file /path/to/reports/FeatureReport.json -H "Content-Type:application/json" http://localhost:8080/api/1.0/features/cucumber
```

If you want to add specific properties to the upload which make it possible to 
create specific dashboards, then include the BDD-Reporting-Properties header. 

Here is an example:

```bash
curl -X PUT --upload-file /path/to/reports/FeatureReport.json -H "Content-Type:application/json" -H "BDD-Reporting-Properties: environment=dev,build=1.1.1" http://localhost:8080/api/1.0/features/cucumber
```

##Screenshots

###Dashboard

![Alt text](github/images/dashboard.png?raw=true "Dashboard")

###Viewing Features

![Alt text](github/images/features.png?raw=true "Features")

###Viewing a Single Feature

![Alt text](github/images/feature.png?raw=true "Feature")