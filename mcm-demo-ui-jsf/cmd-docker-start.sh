#!/bin/bash

NETWORK_NAME="mcm-demo"
APP_NAME="mcm-demo-ui-jsf"

docker network inspect "$NETWORK_NAME" >/dev/null 2>&1 || docker network create "$NETWORK_NAME"

docker stop "$APP_NAME"

docker rm "$APP_NAME"

docker run -d --name "$APP_NAME" --network "$NETWORK_NAME" -p 8084:8080 \
    -e CUSTOMER_API_URL=http://mcm-demo-api-customers:8080/api-customers \
    -e TOKEN_URL=http://mcm-demo-keycloak:8080/realms/mcm-demo/protocol/openid-connect/token \
    -e JWK_URL=http://mcm-demo-keycloak:8080/realms/mcm-demo/protocol/openid-connect/certs \
    -e CLIENT_ID=mcm-demo-client-jsf \
    -e CLIENT_SECRET=HtsCdaR7o5KoNQpI0BOOWwtds1sxorCT \
    -e AUTH_SERVER_URL=http://127.0.0.1:8090/realms/mcm-demo/protocol/openid-connect/auth \
    -e LOGOUT_URL=http://127.0.0.1:8090/realms/mcm-demo/protocol/openid-connect/logout \
    -e LOGIN_REDIRECT_URL=http://127.0.0.1:8084/demo-ui \
    -e CALLBACK_URL=http://127.0.0.1:8084/demo-ui/callback \
    -e LOGOUT_REDIRECT_URL=http://127.0.0.1:8084/demo-ui \
    -e SCOPE='openid profile roles' \
    "$APP_NAME"

docker logs -f "$APP_NAME"
