#!/bin/bash

mvn clean package -DskipTests

APP_NAME="mcm-demo-api-customers-jakarta-code-first"

docker build -t "$APP_NAME" .
