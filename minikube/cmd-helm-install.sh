#!/bin/bash

helm install helm-mcm-demo ./helm-mcm-demo --namespace mcm-dev --create-namespace -f helm-properties.yaml