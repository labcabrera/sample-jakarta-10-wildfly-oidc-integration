#!/bin/bash

set -e

MINIKUBE_MEMORY=16384
MINIKUBE_CPUS=6
MINIKUBE_DISK=20g
K8S_VERSION=v1.33.2

NAMESPACE="apps-local"
NAMESPACE_KAFKA="kafka-local"
NAMESPACE_DB="db-local"
NAMESPACE_TOOLS="platform"

echo "----------------------------------------------------------------------------"
echo "✨ Starting minikube..."
echo "----------------------------------------------------------------------------"

minikube start \
  --driver=docker \
  --memory=$MINIKUBE_MEMORY \
  --cpus=$MINIKUBE_CPUS \
  --disk-size=$MINIKUBE_DISK \
  --kubernetes-version=$K8S_VERSION

minikube addons enable metrics-server

minikube addons enable ingress

echo "----------------------------------------------------------------------------"
echo "✨ Installing Kafka using NodePort..."
echo "----------------------------------------------------------------------------"

helm install kafka bitnami/kafka \
  --namespace "$NAMESPACE_KAFKA" \
  --create-namespace \
  --set externalAccess.enabled=true \
  --set externalAccess.controller.service.type=NodePort \
  --set externalAccess.broker.service.type=NodePort \
  --set defaultInitContainers.autoDiscovery.enabled=true \
  --set serviceAccount.create=true \
  --set rbac.create=true \
  --set controller.automountServiceAccountToken=true \
  --set broker.automountServiceAccountToken=true

# Read kafka generated pwd

KAFKA_CLIENT_PWD=$(kubectl get secret kafka-user-passwords -n "$NAMESPACE_KAFKA" -o jsonpath="{.data.client-passwords}")
KAFKA_CLIENT_PWD_DECODED=$(echo "$KAFKA_CLIENT_PWD" | base64 -d)

# Basic configMaps and secrets for the demo

echo "----------------------------------------------------------------------------"
echo "✨ Creating namespace "$NAMESPACE" and installing configMaps and secrets..."
echo "----------------------------------------------------------------------------"

kubectl create namespace "$NAMESPACE"

find ../k8s-config-maps -name "*.yaml" -o -name "*.yml" | while read -r file; do
  kubectl apply -f "$file" -n "$NAMESPACE"
done

find ../k8s-secrets -name "*.yaml" -o -name "*.yml" | while read -r file; do
  sed "s/{kafka_password_b64}/$KAFKA_CLIENT_PWD/g" "$file" | kubectl apply -f - -n "$NAMESPACE"
done

echo "----------------------------------------------------------------------------"
echo "✨ Installing ArgoCD and configuring applications..."
echo "----------------------------------------------------------------------------"

# Install ArgoCD with fixed admin password 'changeit'

helm install argocd argo/argo-cd \
  --namespace "$NAMESPACE_TOOLS" \
  --create-namespace \
  --set server.service.type=NodePort \
  --set configs.secret.argocdServerAdminPassword='$2y$10$vAKJfNlItZH/h500v0DObOd5IFBAUJifgSLTiVpKzqJ2AKGhqozVy'

# Create ArgoCD applications

find ../argocd -name "*.yaml" -o -name "*.yml" | while read -r file; do
  kubectl apply -f "$file" -n "$NAMESPACE_TOOLS"
done

echo "----------------------------------------------------------------------------"
echo "✨ Installing Kafka-UI..."
echo "----------------------------------------------------------------------------"

sed "s/{password}/$KAFKA_CLIENT_PWD_DECODED/g" ingress/kafka-ui-config.yaml | kubectl apply -f - -n "$NAMESPACE_TOOLS"

helm install kafka-ui kafka-ui/kafka-ui \
  --namespace "$NAMESPACE_TOOLS" \
  --set existingConfigMap="kafka-ui-config" \
  --set ingress.enabled=true \
  --set ingress.className=nginx \
  --set ingress.hosts[0].host=kafka-ui.local \
  --set ingress.hosts[0].paths[0].path=/ \
  --set ingress.hosts[0].paths[0].pathType=Prefix

echo "----------------------------------------------------------------------------"
echo "✨ Installing Kafka Schema Registry..."
echo "----------------------------------------------------------------------------"

helm install schema-registry bitnami/schema-registry \
  --namespace "$NAMESPACE_KAFKA" \
  --set kafka.enabled=false \
  --set externalKafka.brokers=SASL_PLAINTEXT://kafka.kafka-local:9092 \
  --set externalKafka.auth.protocol=sasl \
  --set ingress.enabled=true \
  --set ingress.hostname=schema-registry.local \
  --set extraEnvVars[0].name=SCHEMA_REGISTRY_KAFKA_SASL_USER \
  --set extraEnvVars[0].value=user1 \
  --set extraEnvVars[1].name=SCHEMA_REGISTRY_KAFKA_SASL_PASSWORD \
  --set extraEnvVars[1].value="$KAFKA_CLIENT_PWD_DECODED" \
  --timeout=600s

echo ""
echo "✅ Instalation complete"
echo ""
echo "To access the administration console, run:"
echo ""
echo "  minikube dashboard"
