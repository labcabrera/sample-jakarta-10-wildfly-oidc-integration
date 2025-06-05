#!/bin/bash

mvn clean package -DskipTests

APP_NAME="mcm-demo-api-customers"

docker build -t "$APP_NAME" .
