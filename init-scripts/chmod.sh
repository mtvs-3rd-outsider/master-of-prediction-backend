#!/bin/bash
# chmod.sh

# 권한 수정
chmod 644 /etc/mysql/my.cnf
chmod 644 /docker-entrypoint-initdb.d/init.sql
# MySQL 시작
exec "$@"
