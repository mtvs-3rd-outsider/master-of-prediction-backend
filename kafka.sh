#!/bin/bash

# Kafka 컨테이너 이름
KAFKA_CONTAINER="kafka"

# Usage help
usage() {
  echo "사용법: ./kafka.sh [옵션]"
  echo "옵션:"
  echo "  1: Kafka 토픽 목록 확인"
  echo "  2: Kafka 토픽 생성"
  echo "  3: Kafka 토픽 삭제"
  echo "  4: Kafka 오프셋 초기화"
  echo "  5: Kafka 특정 토픽 정보 확인"
  exit 1
}

# Check if any argument is provided
if [ -z "$1" ]; then
  usage
fi

# Kafka 토픽 목록 확인
list_topics() {
  echo "Kafka 토픽 목록 확인 중..."
  docker compose exec $KAFKA_CONTAINER kafka-topics.sh --bootstrap-server localhost:9092 --list
}

# Kafka 토픽 생성
create_topic() {
  read -p "생성할 토픽 이름을 입력하세요: " topic_name
  read -p "파티션 수를 입력하세요 (기본값: 1): " partitions
  partitions=${partitions:-1}
  read -p "복제 인수를 입력하세요 (기본값: 1): " replication
  replication=${replication:-1}

  echo "Kafka 토픽 생성 중: $topic_name"
  docker compose exec $KAFKA_CONTAINER kafka-topics.sh --bootstrap-server localhost:9092 --create --topic $topic_name --partitions $partitions --replication-factor $replication

  if [ $? -eq 0 ]; then
    echo "토픽이 성공적으로 생성되었습니다: $topic_name"
  else
    echo "토픽 생성에 실패했습니다: $topic_name"
  fi
}

# Kafka 토픽 삭제
delete_topic() {
  read -p "삭제할 토픽 이름을 입력하세요: " topic_name

  echo "Kafka 토픽 삭제 중: $topic_name"
  docker compose exec $KAFKA_CONTAINER kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic $topic_name

  if [ $? -eq 0 ]; then
    echo "토픽이 성공적으로 삭제되었습니다: $topic_name"
  else
    echo "토픽 삭제에 실패했습니다: $topic_name"
  fi
}

# Kafka 오프셋 초기화
reset_offsets() {
  read -p "오프셋을 초기화할 소비자 그룹을 입력하세요: " consumer_group
  read -p "초기화할 토픽 이름을 입력하세요 (모든 토픽에 대해 초기화하려면 비워두세요): " topic_name

  if [ -z "$topic_name" ]; then
    echo "Kafka 모든 토픽에 대해 오프셋 초기화 중..."
    docker compose exec $KAFKA_CONTAINER kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group $consumer_group --reset-offsets --to-earliest --all-topics --execute
  else
    echo "Kafka 특정 토픽에 대해 오프셋 초기화 중: $topic_name"
    docker compose exec $KAFKA_CONTAINER kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group $consumer_group --reset-offsets --to-earliest --topic $topic_name --execute
  fi

  if [ $? -eq 0 ]; then
    echo "오프셋이 성공적으로 초기화되었습니다."
  else
    echo "오프셋 초기화에 실패했습니다."
  fi
}

# Kafka 특정 토픽 정보 확인
describe_topic() {
  read -p "정보를 확인할 토픽 이름을 입력하세요: " topic_name

  echo "Kafka 토픽 정보 확인 중: $topic_name"
  docker compose exec $KAFKA_CONTAINER kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic $topic_name

  if [ $? -eq 0 ]; then
    echo "토픽 정보가 성공적으로 확인되었습니다."
  else
    echo "토픽 정보 확인에 실패했습니다."
  fi
}

# Main script logic
case "$1" in
  1)
    list_topics
    ;;
  2)
    create_topic
    ;;
  3)
    delete_topic
    ;;
  4)
    reset_offsets
    ;;
  5)
    describe_topic
    ;;
  *)
    usage
    ;;
esac
