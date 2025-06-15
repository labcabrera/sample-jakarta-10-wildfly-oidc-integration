#!/bin/bash

NETWORK_NAME="mcm-demo"
APP_NAME="mcm-demo-ui-jsf"

docker network inspect "$NETWORK_NAME" >/dev/null 2>&1 || docker network create "$NETWORK_NAME"

docker stop "$APP_NAME"

docker rm "$APP_NAME"

docker run -d --name "$APP_NAME" --network "$NETWORK_NAME" -p 8084:8080 \
    -e APP_CUSTOMER_API_URL=http://mcm-demo-api-customers:8080/api-customers \
    -e APP_USERS_API_URL=http://mcm-demo-api-users:8080/api-users/api \
    -e APP_TOKEN_URL=http://mcm-demo-keycloak:8080/realms/mcm-demo/protocol/openid-connect/token \
    -e APP_JWK_URL=http://mcm-demo-keycloak:8080/realms/mcm-demo/protocol/openid-connect/certs \
    -e APP_CLIENT_ID=mcm-demo-client-jsf \
    -e APP_CLIENT_SECRET=HtsCdaR7o5KoNQpI0BOOWwtds1sxorCT \
    -e APP_AUTH_SERVER_URL=http://127.0.0.1:8090/realms/mcm-demo/protocol/openid-connect/auth \
    -e APP_LOGOUT_URL=http://127.0.0.1:8090/realms/mcm-demo/protocol/openid-connect/logout \
    -e APP_LOGIN_REDIRECT_URL=http://127.0.0.1:8084/demo-ui \
    -e APP_CALLBACK_URL=http://127.0.0.1:8084/demo-ui/callback \
    -e APP_LOGOUT_REDIRECT_URL=http://127.0.0.1:8084/demo-ui \
    -e APP_OIDC_SCOPE='openid profile roles' \
    "$APP_NAME"

docker logs -f "$APP_NAME"
