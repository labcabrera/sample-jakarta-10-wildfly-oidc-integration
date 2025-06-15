#!/bin/bash

APP_NAME="mcm-demo-api-users"

docker tag "$APP_NAME" labcabrera/"$APP_NAME":latest

docker push labcabrera/"$APP_NAME":latest
