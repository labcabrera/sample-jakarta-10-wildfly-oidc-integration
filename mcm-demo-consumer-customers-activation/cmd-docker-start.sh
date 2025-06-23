#!/bin/bash

APP_NAME="mcm-demo-consumer-customers-activation"
NETWORK_NAME="mcm-demo"

docker network inspect "$NETWORK_NAME" >/dev/null 2>&1 || docker network create "$NETWORK_NAME"

docker stop "$APP_NAME"

docker rm "$APP_NAME"

docker run -d --name "$APP_NAME" --network "$NETWORK_NAME" -p 8081:8080 -p 9990:9990 \
    -e APP_KAFKA_BROKER=192.168.49.2:30627 \
    -e APP_KAFKA_USERNAME=foo \
    -e APP_KAFKA_PASSWORD=bar \
    "$APP_NAME"

docker logs -f "$APP_NAME"
