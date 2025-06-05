#!/bin/bash

./mvnw clean package -DskipTests

APP_NAME="mcm-demo-api-users"

docker build -t "$APP_NAME" .
