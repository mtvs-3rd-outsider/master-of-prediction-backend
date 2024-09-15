#!/bin/bash

# Wait for Elasticsearch to be ready
until curl -s http://localhost:9200/_cluster/health | grep -q '"status":"yellow"'; do
  echo "Waiting for Elasticsearch..."
  sleep 5
done

# Set number of replicas to 0
curl -X PUT "http://localhost:9200/your_index_name/_settings" -H 'Content-Type: application/json' -d'{
  "index": {
    "number_of_replicas": 0
  }
}'
