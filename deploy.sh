#!/bin/bash

# gradle java -jar 빌드
echo "JAR file 빌드"
./gradlew build

if [ $? -ne 0 ]; then
  echo "빌드 실패"
  exit 1
fi

# Docker Compose 실행
echo "기존 Docker compose 종료"
docker compose -f docker-compose.yml down

echo "Docker compose 실행"
docker compose -f docker-compose.yml up -d

if [ $? -ne 0 ]; then
  echo "docker compose 실행 실패"
  exit 1
fi