#!/bin/bash

#helm uninstall schema-registry --namespace mcm-local-schema-registry

helm install schema-registry bitnami/schema-registry \
  --namespace mcm-local-schema-registry \
  --create-namespace \
  --set kafka.enabled=false \
  --set kafka.auth.protocol=SASL_PLAINTEXT \
  --set kafka.auth.saslMechanism=plain \
  --set kafka.auth.username=user1 \
  --set kafka.auth.password=aNG8pRrzXF \
  --set kafka.service.ports.client=9092 \
  --set externalKafka.brokers[0]="kafka.mcm-local-kafka:9092" \
  --set service.type=ClusterIP

# NOT WORKING SASL_PLAINTEXT