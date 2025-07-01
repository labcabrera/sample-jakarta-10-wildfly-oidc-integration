#!/bin/bash

#helm uninstall schema-registry --namespace mcm-local-schema-registry

helm install schema-registry bitnami/schema-registry \
  --namespace mcm-local-kafka \
  --set kafka.enabled=false \
  --set externalKafka.brokers=SASL_PLAINTEXT://kafka.mcm-local-kafka:9092 \
  --set externalKafka.auth.protocol=sasl \
  --set ingress.enabled=true \
  --set ingress.hostname=schema-registry.local \
  --set extraEnvVars[0].name=SCHEMA_REGISTRY_KAFKA_SASL_USER \
  --set extraEnvVars[0].value=user1 \
  --set extraEnvVars[1].name=SCHEMA_REGISTRY_KAFKA_SASL_PASSWORD \
  --set extraEnvVars[1].value=aNG8pRrzXF
