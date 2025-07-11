#!/bin/bash

NETWORK_NAME="mcm-demo"
APP_NAME="mcm-demo-api-customers"

docker network inspect "$NETWORK_NAME" >/dev/null 2>&1 || docker network create "$NETWORK_NAME"

docker stop "$APP_NAME"

docker rm "$APP_NAME"

# Read configuration from Kubernetes
KAFKA_NODE_PORT=$(kubectl get svc kafka-controller-0-external -n kafka-local -o jsonpath='{.spec.ports[0].nodePort}')
KAFKA_CLIENT_PWD=$(kubectl get secret kafka-user-passwords -n kafka-local -o jsonpath="{.data.client-passwords}")
KAFKA_CLIENT_PWD_DECODED=$(echo "$KAFKA_CLIENT_PWD" | base64 -d)

echo "KAFKA_CLIENT_PWD_DECODED: $KAFKA_CLIENT_PWD_DECODED"

docker run -d --name "$APP_NAME" --network "$NETWORK_NAME" -p 8081:8080 \
    -e APP_JWK_URL=http://mcm-demo-keycloak:8080/realms/mcm-demo/protocol/openid-connect/certs \
    -e APP_USERS_API_URL=http://mcm-demo-api-users:8080/api-users/api \
    -e APP_DB_HOST=mcm-demo-postgres \
    -e APP_DB_USERNAME=user \
    -e APP_DB_PASSWORD=changeit \
    -e APP_KAFKA_BROKER=192.168.49.2:"$KAFKA_NODE_PORT" \
    -e APP_KAFKA_TOPIC=customers \
    -e APP_KAFKA_TOPIC_CUSTOMER_CREATED=customers-created-topic \
    -e APP_KAFKA_USERNAME=user1 \
    -e APP_KAFKA_PASSWORD="$KAFKA_CLIENT_PWD_DECODED" \
    "$APP_NAME"

docker logs -f "$APP_NAME"
