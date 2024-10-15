#!/bin/bash

# Kafka Connect 준비 대기 시간 (초)
RETRY_INTERVAL=5
MAX_RETRIES=30  # 최대 재시도 횟수
CONNECT_URL="http://debezium:8083/connectors/forecast-connector/status"
CONNECTOR_CONFIG="/kafka/connectors/mysql-connector.json"

# Kafka Connect 상태 확인 및 커넥터 등록 재시도
echo "Waiting for Kafka Connect or attempting to register connector if not 200..."

retry_count=0

while [[ $retry_count -lt $MAX_RETRIES ]]
do
  # Kafka Connect 상태 확인
  status_code=$(curl -s -o /dev/null -w "%{http_code}" $CONNECT_URL)

  if [[ $status_code -eq 200 ]]; then
    echo "Kafka Connect is ready (Status code: $status_code)."
    break
  else
    echo "Kafka Connect not ready yet (Status code: $status_code). Attempting to register connector..."

    # Kafka Connect 상태가 200이 아니면 커넥터 등록 시도
    curl -i -X POST -H "Accept: application/json" -H "Content-Type: application/json" \
      debezium:8083/connectors/ -d @"$CONNECTOR_CONFIG"

    echo "Connector registration attempt complete."

    # 잠시 대기 후 재시도
    sleep $RETRY_INTERVAL
    retry_count=$((retry_count+1))
  fi
done

if [[ $retry_count -eq $MAX_RETRIES ]]; then
  echo "Max retries reached. Kafka Connect did not become ready. Exiting."
  exit 1
fi

echo "Kafka Connect check and connector registration process complete."
