#!/bin/bash
echo "Stopping Spring Boot application..."

# 현재 실행 중인 Spring Boot 프로세스 찾기
pid=$(pgrep -f "backend.jar")

if [ -n "$pid" ]; then
    echo "Killing process $pid"
    kill $pid
    sleep 10
    
    # 프로세스가 여전히 살아있다면 강제 종료
    if ps -p $pid > /dev/null; then
        echo "Force killing process $pid"
        kill -9 $pid
    fi
else
    echo "No running application found"
fi

echo "Application stopped"
exit 0