#!/bin/bash

# Debezium REST API URL
CONNECTOR_NAME="forecast-connector"
CONNECTORS_URL="http://localhost:8083/connectors"  # 커넥터 목록 생성 URL
CONNECTOR_URL="${CONNECTORS_URL}/${CONNECTOR_NAME}"
CONNECTOR_STATUS_URL="${CONNECTOR_URL}/status"

# Debezium MySQL Connector 설정
CONNECTOR_CONFIG='{
  "name": "forecast-connector",
  "config": {
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "tasks.max": "1",
    "database.hostname": "db",
    "database.port": "3306",
    "database.user": "ohgiraffers",
    "database.password": "ohgiraffers",
    "database.server.id": "1",
    "topic.prefix": "dbserver1",
    "database.include.list": "forecasthub",
    "schema.history.internal.kafka.bootstrap.servers": "kafka:9092",
    "schema.history.internal.kafka.topic": "schemahistory.forceasthub",
    "database.serverTimezone": "Asia/Seoul",
    "include.schema.changes": "true",
    "snapshot.mode": "initial"
  }
}'

# 커넥터 상태 확인 (인자를 주면 상태만 출력)
check_connector_status_only() {
  echo "Checking connector status..."
  curl -s $CONNECTOR_STATUS_URL
}

# 커넥터 상태 확인 후 필요 시 재시작
check_connector_status() {
  echo "Checking connector status..."
  response=$(curl -s -o /dev/null -w "%{http_code}" $CONNECTOR_STATUS_URL)

  if [[ "$response" == "200" ]]; then
    status=$(curl -s $CONNECTOR_STATUS_URL | jq -r '.connector.state')
    tasks_status=$(curl -s $CONNECTOR_STATUS_URL | jq -r '.tasks[].state')

    echo "Connector $CONNECTOR_NAME status: $status"

    if [[ "$status" == "RUNNING" && "$tasks_status" == "RUNNING" ]]; then
      echo "Connector and tasks are running properly."
      show_connector_status
    else
      echo "Connector or tasks are not running. Attempting to restart..."
      delete_connector
    fi
  else
    echo "Connector $CONNECTOR_NAME does not exist. Creating new connector..."
    create_connector
  fi
}

# 커넥터 삭제
delete_connector() {
  echo "Deleting connector $CONNECTOR_NAME..."
  curl -X DELETE $CONNECTOR_URL
  echo "Connector deleted."
  sleep 2  # 커넥터가 삭제된 후 잠시 대기
  create_connector
}

# 커넥터 생성
create_connector() {
  echo "Creating new connector $CONNECTOR_NAME..."
  response=$(curl -s -o /dev/null -w "%{http_code}" -X POST $CONNECTORS_URL -H "Content-Type: application/json" -d "$CONNECTOR_CONFIG")

  if [[ "$response" == "201" || "$response" == "200" ]]; then
    echo "Connector created successfully."
    show_connector_status
  else
    echo "Failed to create connector. Response code: $response"
  fi
}

# 커넥터 상태 출력
show_connector_status() {
  echo "Fetching connector status..."
  status_response=$(curl -s $CONNECTOR_STATUS_URL)
  echo "Connector Status: $status_response"
}

# 메인 로직 실행
if [[ "$1" == "1" ]]; then
  check_connector_status_only
else
  check_connector_status
fi
