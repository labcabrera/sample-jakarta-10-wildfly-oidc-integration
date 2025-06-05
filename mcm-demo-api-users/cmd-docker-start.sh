#!/bin/bash

NETWORK_NAME="mcm-demo"
APP_NAME="mcm-demo-api-users"

docker network inspect "$NETWORK_NAME" >/dev/null 2>&1 || docker network create "$NETWORK_NAME"

docker stop "$APP_NAME"

docker rm "$APP_NAME"

docker run -d --name "$APP_NAME" --network mcm-demo -p 8082:8080 \
    -e ENABLED_SECURITY=true \
    -e JWK_URI=http://mcm-demo-keycloak:8080/realms/mcm-demo/protocol/openid-connect/certs \
    "$APP_NAME"

docker logs -f "$APP_NAME"
