#!/bin/bash

# MySQL 접속 정보 설정
DB_CONTAINER="db"
DB_USER="ohgiraffers"
DB_PASSWORD="ohgiraffers"

# MySQL 접속 상태 확인
check_mysql_connection() {
  echo "컨테이너 $DB_CONTAINER 내부에서 MySQL 연결 상태 확인 중..."

  # Docker 컨테이너 내부에서 MySQL 연결 상태 확인
  if docker compose exec "$DB_CONTAINER" mysqladmin ping -u "$DB_USER" -p"$DB_PASSWORD" > /dev/null 2>&1; then
    echo "컨테이너 내부에서 MySQL이 실행 중입니다."
  else
    echo "컨테이너 내부에서 MySQL에 연결할 수 없습니다."
    exit 1
  fi
}

# binlog 설정 확인
check_binlog_settings() {
  echo "컨테이너 내부에서 MySQL binlog 설정 확인 중..."

  binlog_enabled=$(docker compose exec "$DB_CONTAINER" mysql -u "$DB_USER" -p"$DB_PASSWORD" -e "SHOW VARIABLES LIKE 'log_bin';" | grep "ON")
  binlog_format=$(docker compose exec "$DB_CONTAINER" mysql -u "$DB_USER" -p"$DB_PASSWORD" -e "SHOW VARIABLES LIKE 'binlog_format';" | grep "ROW")

  if [ ! -z "$binlog_enabled" ] && [ ! -z "$binlog_format" ]; then
    echo "Binlog이 활성화되어 있으며 포맷이 ROW로 설정되었습니다."
  else
    echo "Binlog 설정이 올바르지 않습니다."
    exit 1
  fi
}

# DB 상태 확인 (간단한 쿼리 실행)
check_mysql_status() {
  echo "컨테이너 내부에서 MySQL 상태 확인 중..."

  # 실행할 쿼리 (간단한 상태 확인)
  query_result=$(docker compose exec "$DB_CONTAINER" mysql -u "$DB_USER" -p"$DB_PASSWORD" -e "SHOW STATUS LIKE 'Uptime';")

  # 쿼리 결과에서 필요한 정보만 추출 및 출력
  uptime=$(echo "$query_result" | grep "Uptime" | awk '{print $2}')
  if [ ! -z "$uptime" ]; then
    echo "MySQL이 실행 중입니다. 가동 시간: $uptime 초"
  else
    echo "MySQL 상태를 가져오는 데 실패했습니다."
    exit 1
  fi
}

# 메인 로직 실행
check_mysql_connection
check_binlog_settings
check_mysql_status
