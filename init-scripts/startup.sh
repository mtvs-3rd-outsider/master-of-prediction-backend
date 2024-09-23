#!/bin/bash

# Wait for Kafka Connect to be ready
echo "Waiting for Kafka Connect to start..."
sleep 10

# URL of the Kafka Connect REST API
CONNECT_URL="http://debezium:8083/connectors"

# Path to the connector configuration file
CONNECTOR_CONFIG="/kafka/connectors/mysql-connector.json"

# Register the Debezium connector
echo "Registering Debezium connector..."

curl -s http://debezium:8083/connectors/forecast-connector/status
#curl -X DELETE http://debezium:8083/connectors/forecast-connector
curl -i -X POST -H "Accept: application/json" -H "Content-Type: application/json" debezium:8083/connectors/ -d @/kafka/connectors/mysql-connector.json
#curl -X POST -H "Content-Type: application/json" --data @"$CONNECTOR_CONFIG" $CONNECT_URL
#
echo "Debezium connector registration complete."
