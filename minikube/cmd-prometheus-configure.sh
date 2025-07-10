#!/bin/bash

set -e

NAMESPACE_MONITORING="platform"

echo "📦 Agregando repositorios de Helm..."
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update

echo "----------------------------------------------------------------------------"
echo "🏗️ Creating namespace..."
echo "----------------------------------------------------------------------------"

kubectl create namespace "$NAMESPACE_MONITORING" --dry-run=client -o yaml | kubectl apply -f -

echo "----------------------------------------------------------------------------"
echo "📊 Installing Prometheus..."
echo "----------------------------------------------------------------------------"

helm install prometheus prometheus-community/kube-prometheus-stack \
  --namespace "$NAMESPACE_MONITORING" \
  --set prometheus.prometheusSpec.serviceMonitorSelectorNilUsesHelmValues=false \
  --set prometheus.prometheusSpec.podMonitorSelectorNilUsesHelmValues=false \
  --set prometheus.prometheusSpec.ruleSelectorNilUsesHelmValues=false \
  --set prometheus.prometheusSpec.retention=30d \
  --set prometheus.prometheusSpec.storageSpec.volumeClaimTemplate.spec.resources.requests.storage=10Gi \
  --set alertmanager.enabled=true \
  --set grafana.enabled=true \
  --set grafana.adminPassword=changeit \
  --set grafana.persistence.enabled=true \
  --set grafana.persistence.size=5Gi \
  --set grafana.service.type=NodePort \
  --set prometheus.service.type=NodePort \
  --timeout=600s

# echo "----------------------------------------------------------------------------"
# echo "⏳ Waiting for pods..."
# echo "----------------------------------------------------------------------------"

# kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=prometheus -n "$NAMESPACE_MONITORING" --timeout=300s
# kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=grafana -n "$NAMESPACE_MONITORING" --timeout=300s

echo "----------------------------------------------------------------------------"
echo "🌐 Configuring Ingress..."
echo "----------------------------------------------------------------------------"

kubectl apply -f ingress/grafana-ingress.yaml -n "$NAMESPACE_MONITORING"
kubectl apply -f ingress/prometheus-ingress.yaml -n "$NAMESPACE_MONITORING"
