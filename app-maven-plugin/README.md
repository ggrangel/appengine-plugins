# Google App Engine Maven plugin

This Maven plugin allows you to perform Java app management operations.

# Requirements

Maven is required to build the plugin. To download Maven, follow the [instructions](http://maven.apache.org/).

The remaining dependencies are specified in the pom.xml file and should be automatically downloaded when the plugin is built.

# How to use

In your Maven App Engine Java app, add the following line to the \<dependency\> block:

    <dependency>
      <groupId>com.google.cloud.app</groupId>
      <artifactId>google-cloud-app</artifactId>
      <version>0.1-SNAPSHOT</version>
    </dependency>

You can now run commands like "mvn google-cloud-java:deploy" in the root folder of your Java application. (i.e., where the project's pom.xml file is)

# Supported goals

google-cloud-java:deploy
google-cloud-java:run
google-cloud-java:run-async
google-cloud-java:stop
google-cloud-java:gen-files
google-cloud-java:module-delete
google-cloud-java:module-get-logs
google-cloud-java:module-list
google-cloud-java:module-set-default
google-cloud-java:module-set-managed-by
google-cloud-java:module-start
google-cloud-java:module-stop