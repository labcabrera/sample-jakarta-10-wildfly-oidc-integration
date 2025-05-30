#!/bin/bash

docker network inspect mcm-demo >/dev/null 2>&1 || docker network create mcm-demo

docker stop mcm-demo-ui

docker rm mcm-demo-ui

docker run -d --name mcm-demo-ui --network mcm-demo -p 8082:8080 \
    -e CUSTOMER_API_HOST=mcm-demo-api \
    -e CUSTOMER_API_PORT='8080' \
    -e CUSTOMER_API_SCHEME=http \
    -e CUSTOMER_API_BASE_PATH=/demo-api \
     mcm-demo-ui

docker logs -f mcm-demo-ui
