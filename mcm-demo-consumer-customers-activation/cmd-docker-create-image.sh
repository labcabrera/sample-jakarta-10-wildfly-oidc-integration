#!/bin/bash

APP_NAME="mcm-demo-consumer-customers-activation"

./gradlew clean build -x test

docker build -t "$APP_NAME" .
