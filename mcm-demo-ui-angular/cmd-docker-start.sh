#!/bin/bash

NETWORK_NAME="mcm-demo"
APP_NAME="mcm-demo-ui-angular"

docker network inspect "$NETWORK_NAME" >/dev/null 2>&1 || docker network create "$NETWORK_NAME"

docker stop "$APP_NAME"

docker rm "$APP_NAME"

docker run -d --name "$APP_NAME" --network "$NETWORK_NAME" -p 8100:80 \
  -e API_URL=http://api:8080 \
  -e OIDC_ISSUER=http://keycloak:8080/realms/demo \
  -e OIDC_CLIENT_ID=angular-client \
  mcm-demo-ui-angular

docker logs -f "$APP_NAME"
