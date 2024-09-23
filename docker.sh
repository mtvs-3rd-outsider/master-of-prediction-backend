#!/bin/bash

# docker.sh

if [ -z "$1" ]; then
  echo "사용법: ./docker.sh [옵션 번호]"
  echo "옵션:"
  echo "  1: docker compose logs debezium --tail 100"
  echo "  2: docker compose logs elasticsearch --tail 100"
  echo "  3: docker compose --profile required restart"
  echo "  4: docker compose --profile kafka restart"
  echo "  5: docker compose --profile elasticsearch restart"
  echo "  6: docker compose --profile required down -v (볼륨 제거)"
  echo "  7: docker compose --profile kafka down -v (볼륨 제거)"
  echo "  8: docker compose --profile elasticsearch down -v (볼륨 제거)"
  echo "  9: docker compose ps --format 'table {{.Name}}\t{{.State}}'"
  echo " 10: docker compose ps --filter 'status=running'"
  echo " 11: docker compose --profile db restart"
  echo " 12: docker compose --profile db down -v (볼륨 제거)"
  echo " 13: docker compose logs db --tail 100"
  exit 1
fi

case "$1" in
  1)
    docker compose logs debezium --tail 100
    ;;
  2)
    docker compose logs elasticsearch --tail 100
    ;;
  3)
    echo "required 프로필을 재시작합니다..."
    docker compose --profile required down
    docker compose --profile required up -d
    ;;
  4)
    echo "kafka 프로필을 재시작합니다..."
    docker compose --profile kafka down
    docker compose --profile kafka up -d
    ;;
  5)
    echo "elasticsearch 프로필을 재시작합니다..."
    docker compose --profile elasticsearch down
    docker compose --profile elasticsearch up -d
    ;;
  6)
    echo "required 프로필의 컨테이너와 볼륨을 제거합니다..."
    docker compose --profile required down -v
    ;;
  7)
    echo "kafka 프로필의 컨테이너와 볼륨을 제거합니다..."
    docker compose --profile kafka down -v
    ;;
  8)
    echo "elasticsearch 프로필의 컨테이너와 볼륨을 제거합니다..."
    docker compose --profile elasticsearch down -v
    ;;
  9)
    docker compose ps --format "table {{.Name}}\t{{.State}}"
    ;;
  10)
    docker compose ps --filter "status=running"
    ;;
  11)
    echo "db 프로필을 재시작합니다..."
    docker compose --profile db down
    docker compose --profile db up -d
    ;;
  12)
    echo "db 프로필의 컨테이너와 볼륨을 제거합니다..."
    docker compose --profile db down -v
    ;;
  13)
    docker compose logs db --tail 100
    ;;
  *)
    echo "잘못된 옵션입니다. 사용법: ./docker.sh [1-13]"
    ;;
esac
