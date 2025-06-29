#!/bin/bash

APP_NAME="mcm-demo-job-customers"

docker tag "$APP_NAME" labcabrera/"$APP_NAME":latest

docker push labcabrera/"$APP_NAME":latest
