#!/bin/bash

NETWORK_NAME="mcm-demo"
APP_NAME="mcm-demo-job-customers"

docker network inspect "$NETWORK_NAME" >/dev/null 2>&1 || docker network create "$NETWORK_NAME"

docker stop "$APP_NAME"

docker rm "$APP_NAME"

docker run -d --name "$APP_NAME" --network "$NETWORK_NAME" \
    -e APP_KAFKA_BROKER=192.168.49.2:31108 \
    -e APP_DB_HOST=mcm-demo-postgres \
    "$APP_NAME"

docker logs -f "$APP_NAME"
