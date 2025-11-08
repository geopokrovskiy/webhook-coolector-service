#!/bin/bash

SCHEMA_REGISTRY_URL="http://schema-registry:8081"
TOPIC="outbox-events"
SCHEMA_FILE="/schemas/outbox-event-schema.avsc"

curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  --data "{\"schema\": $(cat $SCHEMA_FILE | jq -Rs .)}" \
  $SCHEMA_REGISTRY_URL/subjects/$TOPIC-value/versions