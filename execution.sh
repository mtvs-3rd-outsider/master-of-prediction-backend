#!/bin/bash

# Docker Compose 버전 확인
if docker-compose --version &>/dev/null; then
    # Docker Compose V1 명령어 사용
    DOCKER_COMPOSE="docker-compose"
elif docker compose version &>/dev/null; then
    # Docker Compose V2 명령어 사용
    DOCKER_COMPOSE="docker compose"
else
    echo "Docker Compose가 설치되어 있지 않거나 PATH에 없습니다."
    exit 1
fi

# 인자 처리
if [ -z "$1" ]; then
    # 프로필을 사용하는 명령어
    $DOCKER_COMPOSE --profile required down
    $DOCKER_COMPOSE --profile required up -d
else
    # 특정 인자를 사용하는 명령어
    $DOCKER_COMPOSE down "$1"
    $DOCKER_COMPOSE up -d "$1"
fi

