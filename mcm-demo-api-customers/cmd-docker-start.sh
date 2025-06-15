#!/bin/bash

NETWORK_NAME="mcm-demo"
APP_NAME="mcm-demo-api-customers"

docker network inspect "$NETWORK_NAME" >/dev/null 2>&1 || docker network create "$NETWORK_NAME"

docker stop "$APP_NAME"

docker rm "$APP_NAME"

docker run -d --name "$APP_NAME" --network "$NETWORK_NAME" -p 8081:8080 \
    -e APP_JWK_URL=http://mcm-demo-keycloak:8080/realms/mcm-demo/protocol/openid-connect/certs \
    -e APP_USERS_API_URL=http://mcm-demo-api-users:8080/api-users/api \
    -e EXCLUDED_APP_KAFKA_BROKER=mcm-demo-broker:9093 \
    "$APP_NAME"

docker logs -f "$APP_NAME"
