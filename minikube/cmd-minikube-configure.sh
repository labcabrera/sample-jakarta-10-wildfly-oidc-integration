#!/bin/bash

NAMESPACE="mcm-local"

minikube start

#kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "regcred"}]}' -n mcm-dev

minikube addons enable metrics-server

# Install Ingress

minikube addons enable ingress

# Basic configMaps and secrets for the demo

echo "✨ Creating namespace "$NAMESPACE" and installing configMaps and secrets..."

kubectl create namespace "$NAMESPACE"

kubectl apply -f mcm-demo-config-map.yaml -n "$NAMESPACE"

kubectl apply -f mcm-demo-secrets.yaml -n "$NAMESPACE"

# Install Kafka

echo "✨ Installing Kafka using NodePort..."

helm install kafka bitnami/kafka \
  --namespace mcm-local-kafka \
  --create-namespace \
  --set externalAccess.enabled=true \
  --set externalAccess.controller.service.type=NodePort \
  --set externalAccess.broker.service.type=NodePort \
  --set defaultInitContainers.autoDiscovery.enabled=true \
  --set serviceAccount.create=true \
  --set rbac.create=true \
  --set controller.automountServiceAccountToken=true \
  --set broker.automountServiceAccountToken=true

echo "✨ Installing ArgoCD and configuring demo applications..."

# Install ArgoCD

helm install argocd argo/argo-cd \
  --namespace argocd \
  --create-namespace \
  --set server.service.type=NodePort

# Create ArgoCD applications

kubectl apply -f ../argocd/mcm-demo-api-customers-app.yaml -n argocd

kubectl apply -f ../argocd/mcm-demo-api-users-app.yaml -n argocd

kubectl apply -f ../argocd/mcm-demo-ui-jsf-app.yaml -n argocd
