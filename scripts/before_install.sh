#!/bin/bash

# 실행 위치를 프로젝트 디렉토리로 설정
PROJECT_ROOT=/home/ubuntu/webapps/backend

# .env 파일 백업
if [ -f "$PROJECT_ROOT/.env" ]; then
    echo ".env 파일 백업 중..."
    cp $PROJECT_ROOT/.env /tmp/.env.backup
fi

# 기존 프로젝트 디렉토리가 있다면 백업
if [ -d "$PROJECT_ROOT" ]; then
    echo "기존 디렉토리 백업 중..."
    mv $PROJECT_ROOT $PROJECT_ROOT-backup-$(date +%Y%m%d-%H%M%S)
fi

# 새로운 프로젝트 디렉토리 생성
echo "새 프로젝트 디렉토리 생성 중..."
mkdir -p $PROJECT_ROOT

# .env 파일 복원
if [ -f "/tmp/.env.backup" ]; then
    echo ".env 파일 복원 중..."
    cp /tmp/.env.backup $PROJECT_ROOT/.env
    rm /tmp/.env.backup
fi

# 필요한 디렉토리 생성
echo "필요한 디렉토리 생성 중..."
mkdir -p $PROJECT_ROOT/logs
mkdir -p $PROJECT_ROOT/temp

# 디렉토리 권한 설정
echo "디렉토리 권한 설정 중..."
chown -R ubuntu:ubuntu $PROJECT_ROOT
chmod -R 755 $PROJECT_ROOT

echo "before_install.sh 완료"