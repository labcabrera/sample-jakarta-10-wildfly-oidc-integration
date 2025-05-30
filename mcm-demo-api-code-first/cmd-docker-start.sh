#!/bin/bash

docker network inspect mcm-demo >/dev/null 2>&1 || docker network create mcm-demo

docker stop mcm-demo-api

docker rm mcm-demo-api

docker run -d --name mcm-demo-api --network mcm-demo -p 8081:8080 mcm-demo-api

docker logs -f mcm-demo-api
