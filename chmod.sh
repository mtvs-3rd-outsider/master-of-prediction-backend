#!/bin/bash
# chmod.sh

# 권한 수정
chmod 644 /etc/mysql/my.cnf

# MySQL 시작
exec "$@"
