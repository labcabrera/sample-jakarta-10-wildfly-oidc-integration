#!/bin/bash

mvn clean package -DskipTests

APP_NAME="mcm-demo-api-customers-jakarta-contract-first"

docker build -t "$APP_NAME" .
