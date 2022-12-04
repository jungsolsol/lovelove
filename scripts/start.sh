#!/usr/bin/env bash


PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"
APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
TIME_NOW=$(date +%c)

# build 파일 복사
echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
#nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

echo "> 새 애플리케이션 배포!! "
JAR_NAME=$(ls -tr $PROJECT_ROOT/ | grep jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"
nohup java -jar \
-Dspring.config.location=/home/ubuntu/app/src/main/resources/application-oauth.yml,/home/ubuntu/app/src/main/resources/application.yml \
  $PROJECT_ROOT/$JAR_NAME 2>&1 &
