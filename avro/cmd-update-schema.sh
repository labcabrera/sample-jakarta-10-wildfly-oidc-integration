#!/bin/bash

# Delete previous version of the schema if needed

#curl -X DELETE http://schema-registry.local/subjects/customer-created-value?permanent=true

SCHEMA_RAW=$(cat customer-created.avsc)
SCHEMA_ESCAPED=$(echo "$SCHEMA_RAW" | jq -cR .)

echo "Schema escaped: $SCHEMA_ESCAPED"

# Crear el payload JSON usando jq (más seguro)
PAYLOAD=$(jq -n --arg schema "$SCHEMA_RAW" '{schema: ($schema | tostring)}')

echo "Payload: $PAYLOAD"

curl -X POST http://schema-registry.local/subjects/customer-created-value/versions \
  -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  -d "$PAYLOAD"

# See current versions of the schema

curl http://schema-registry.local/subjects/customer-created-value/versions

# See version 1 of the schema

curl http://schema-registry.local/subjects/customer-created-value/versions/1