#!/bin/bash

minikube start

kubectl create namespace mcm-dev --dry-run=client -o yaml | kubectl apply -f -

kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "regcred"}]}' -n mcm-dev

minikube addons enable ingress

kubectl apply -f mcm-demo-config-map.yaml -n mcm-dev

kubectl apply -f mcm-demo-secrets.yaml -n mcm-dev
