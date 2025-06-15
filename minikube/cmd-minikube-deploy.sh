#!/bin/bash

kubectl apply -f mcm-demo-config-map.yaml -n mcm-dev

kubectl apply -f mcm-demo-secrets.yaml -n mcm-dev

kubectl apply -f ../mcm-demo-api-customers/k8s/deployment.yaml -n mcm-dev

kubectl apply -f ../mcm-demo-api-customers/k8s/service.yaml -n mcm-dev

kubectl apply -f ../mcm-demo-api-customers/k8s/ingress.yaml -n mcm-dev

kubectl apply -f ../mcm-demo-ui-jsf/k8s/deployment.yaml -n mcm-dev

kubectl apply -f ../mcm-demo-ui-jsf/k8s/service.yaml -n mcm-dev

kubectl apply -f ../mcm-demo-ui-jsf/k8s/ingress.yaml -n mcm-dev

kubectl apply -f ../mcm-demo-api-users/k8s/deployment.yaml -n mcm-dev

kubectl apply -f ../mcm-demo-api-users/k8s/service.yaml -n mcm-dev

kubectl apply -f ../mcm-demo-api-users/k8s/ingress.yaml -n mcm-dev
