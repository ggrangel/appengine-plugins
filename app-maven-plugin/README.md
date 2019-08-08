![project status image](https://img.shields.io/badge/stability-stable-brightgreen.svg)
[![Build Status](http://travis-ci.org/GoogleCloudPlatform/app-maven-plugin.svg)](http://travis-ci.org/GoogleCloudPlatform/app-maven-plugin)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.google.cloud.tools/appengine-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.google.cloud.tools/appengine-maven-plugin)
# Google App Engine Maven plugin

This Maven plugin provides goals to build and deploy Google App Engine applications.

| 2.0.0 is Live |
| :------------------------------ |
| 2.0.0 has been published. The behavior of the appengine-maven-plugin has changed since v1.+; please see the [CHANGELOG](CHANGELOG.md) for a full list of changes and an updated [USER GUIDE](USER_GUIDE.md) for details. If you are having trouble using or updating your plugin, please file a [new issue](https://github.com/GoogleCloudPlatform/app-maven-plugin/issues).|

# Reference Documentation

App Engine Standard Environment:
* [Using Apache Maven and the App Engine Plugin (standard environment)](https://cloud.google.com/appengine/docs/java/tools/using-maven)
* [App Engine Maven Plugin Goals and Parameters (standard environment)](https://cloud.google.com/appengine/docs/java/tools/maven-reference)

App Engine Flexible Environment:
* [Using Apache Maven and the App Engine Plugin (flexible environment)](https://cloud.google.com/appengine/docs/flexible/java/using-maven)
* [App Engine Maven Plugin Goals and Parameters (flexible environment)](https://cloud.google.com/appengine/docs/flexible/java/maven-reference)

# Requirements

[Maven](http://maven.apache.org/) is required to build and run the plugin.

You must have [Google Cloud SDK](https://cloud.google.com/sdk/) installed.

Cloud SDK app-engine-java component is also required. Install it by running:

    gcloud components install app-engine-java

Login and configure Cloud SDK:

    gcloud init

# How to use

In your Maven App Engine Java app, add the following plugin to your pom.xml:

```XML
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>appengine-maven-plugin</artifactId>
    <version>2.1.0</version>
</plugin>
```

You can now run commands like `mvn package appengine:deploy` in the root folder of your Java application.

# Supported goals

Goal | Description
--- | ---
appengine:cloudSdkLogin|Login and set the Cloud SDK common configuration user.
appengine:stage|Generates an application directory for deployment.
appengine:deploy|Stages and deploys an application to App Engine.
appengine:deployCron|Deploy cron configuration.
appengine:deployDispatch|Deploy dispatch configuration.
appengine:deployDos|Deploy dos configuration.
appengine:deployIndex|Deploy datastore index configuration.
appengine:deployQueue|Deploy queue configuration.
appengine:deployAll|Deploy the application with all available valid configuration.
appengine:run|Runs the App Engine local development server. *(App Engine Standard Only)*
appengine:start|Starts running the App Engine devserver asynchronously and then returns to the command line. When this goal runs, the behavior is the same as the run goal except that Maven continues processing goals and exits after the server is up and running. *(App Engine Standard Only)*
appengine:stop|Stops a running App Engine web development server. *(App Engine Standard Only)*
appengine:genRepoInfoFile|Generates source context files for use by Stackdriver Debugger.
appengine:help|Displays help information on the plugin. Use `mvn appengine:help -Ddetail=true -Dgoal=[goal]` for detailed goal documentation.

To automatically run the `appengine:genRepoInfoFile` goal during the Maven build workflow, add the following to your plugin executions section:

```XML
<plugin>
  ...
  <executions>
    <execution>
      <phase>prepare-package</phase>
      <goals>
        <goal>genRepoInfoFile</goal>
      </goals>
    </execution>
  </executions>
  ...
</plugin>
```
