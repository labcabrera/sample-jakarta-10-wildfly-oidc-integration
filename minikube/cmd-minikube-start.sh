#!/bin/bash

minikube start

kubectl create namespace mcm-dev --dry-run=client -o yaml | kubectl apply -f -

kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "regcred"}]}' -n mcm-dev

minikube addons enable ingress
