#!/bin/bash

mvn clean package -DskipTests

docker build -t mcm-demo-ui-jsf .
