#!/bin/bash

echo "Starting validation process..."

# Java 프로세스 확인 및 대기
echo "Checking Java processes..."
MAX_WAIT=120
WAIT_COUNT=0

while [ $WAIT_COUNT -lt $MAX_WAIT ]; do
    # 더 자세한 프로세스 정보 출력
    echo "Current Java processes:"
    ps aux | grep java
    
    # jar 파일 실행 여부도 확인
    if ps aux | grep -v grep | grep -E "java|\.jar" > /dev/null; then
        echo "Java process found"
        break
    fi
    
    # 시스템 로그 확인
    echo "Checking system logs for Java startup:"
    tail -n 5 /var/log/syslog | grep java || true
    
    echo "Waiting for Java process to start... ($WAIT_COUNT/$MAX_WAIT)"
    sleep 2
    WAIT_COUNT=$((WAIT_COUNT+1))
done

if [ $WAIT_COUNT -eq $MAX_WAIT ]; then
    echo "Timeout waiting for Java process to start"
    echo "Last attempt to find Java process:"
    ps aux | grep java
    exit 1
fi

# 포트 확인 및 대기
PORT=8080
echo "Checking port $PORT..."
MAX_RETRIES=60
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    nc -zv localhost $PORT 2>&1
    if [ $? -eq 0 ]; then
        echo "Application is running on port $PORT"
        exit 0
    fi
    RETRY_COUNT=$((RETRY_COUNT+1))
    echo "Attempt $RETRY_COUNT of $MAX_RETRIES: Waiting for port $PORT..."
    sleep 2
done

echo "Deployment validation failed: Application did not start on port $PORT"
exit 1