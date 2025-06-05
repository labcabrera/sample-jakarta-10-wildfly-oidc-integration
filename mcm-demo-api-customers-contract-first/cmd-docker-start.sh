#!/bin/bash

NETWORK_NAME="mcm-demo"
APP_NAME="mcm-demo-api-customers-jakarta-contract-first"

docker network inspect "$NETWORK_NAME" >/dev/null 2>&1 || docker network create "$NETWORK_NAME"

docker stop "$APP_NAME"

docker rm "$APP_NAME"

docker run -d --name "$APP_NAME" --network mcm-demo -p 8081:8080 "$APP_NAME"

docker logs -f "$APP_NAME"
