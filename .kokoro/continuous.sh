#!/bin/bash

# Fail on any error.
set -e
# Display commands to stderr.
set -x

sudo /opt/google-cloud-sdk/bin/gcloud components update --quiet
sudo /opt/google-cloud-sdk/bin/gcloud components install app-engine-java --quiet

cd github/appengine-plugins

# Use GCP Maven Mirror
mkdir -p ${HOME}/.m2
cp settings.xml ${HOME}/.m2

if [ "$EUID" -ne 0 ]
then
  # not running as root
  ./mvnw clean install -V -B -U -Dorg.slf4j.simpleLogger.showDateTime=true -Dorg.slf4j.simpleLogger.dateTimeFormat=HH:mm:ss:SSS
else
  # running as root - skip file permissions tests that don't work on Docker
  ./mvnw clean install -V -B -U -Dorg.slf4j.simpleLogger.showDateTime=true -Dorg.slf4j.simpleLogger.dateTimeFormat=HH:mm:ss:SSS -Dtest=!FilePermissionsTest
fi
