#!/bin/bash
echo "Starting Spring Boot application..."

APP_DIR="/home/ubuntu/webapps/backend"
LOG_DIR="/home/ubuntu/logs"

# 작업 디렉토리 이동
cd $APP_DIR

# logs 디렉토리 생성 (없는 경우)
if [ ! -d "$LOG_DIR" ]; then
    mkdir -p "$LOG_DIR"
fi

# 환경변수 로드
set -a
source "$APP_DIR/.env"
set +a

# JAR 파일 존재 여부 확인
if [ ! -f "backend.jar" ]; then
    echo "Error: backend.jar not found in current directory"
    echo "Current directory contents:"
    ls -la
    
    # build/libs 디렉토리 확인
    if [ -f "build/libs/backend.jar" ]; then
        echo "Found backend.jar in build/libs directory"
        cp build/libs/backend.jar .
    else
        echo "Error: backend.jar not found in build/libs directory either"
        exit 1
    fi
fi

# 로그 파일로 출력
nohup java -jar backend.jar > "$LOG_DIR/application.log" 2>&1 &

sleep 60

echo "Application started"
exit 0