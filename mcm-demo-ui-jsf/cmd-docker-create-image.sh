#!/bin/bash

./mvnw clean package -DskipTests

docker build -t mcm-demo-ui-jsf .
