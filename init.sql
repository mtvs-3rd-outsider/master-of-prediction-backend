-- Set binlog format to ROW for Debezium
SET GLOBAL binlog_format = 'ROW';
-- Grant replication privileges to the user
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'ohgiraffers'@'%';
GRANT RELOAD, FLUSH_TABLES ON *.* TO 'ohgiraffers'@'%';
FLUSH PRIVILEGES;
