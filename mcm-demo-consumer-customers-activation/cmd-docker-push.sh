#!/bin/bash

APP_NAME="mcm-demo-consumer-customers-activation"

docker tag "$APP_NAME" labcabrera/"$APP_NAME":latest

docker push labcabrera/"$APP_NAME":latest
