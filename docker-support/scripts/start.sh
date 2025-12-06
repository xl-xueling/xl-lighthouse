#!/bin/bash

current_language=$(echo $LANG | cut -d_ -f1)

if [[ "$current_language" == "zh" ]]; then
  MESSAGE_START="您即将执行一键启动，系统将启动所有容器。"
  MESSAGE_INPUT_PROMPT="请输入 'yes' 继续启动: "
  MESSAGE_ABORT="启动已被中止。"
  MESSAGE_START_DOCKER="开始启动 Docker 环境..."
  MESSAGE_START_CONTAINERS="启动容器..."
  MESSAGE_START_DONE="启动完成！所有相关容器已启动。"
else
  MESSAGE_START="You are about to perform a one-click start. The system will start all containers."
  MESSAGE_INPUT_PROMPT="Please enter 'yes' to continue with starting: "
  MESSAGE_ABORT="Start has been aborted."
  MESSAGE_START_DOCKER="Starting Docker environment..."
  MESSAGE_START_CONTAINERS="Starting containers..."
  MESSAGE_START_DONE="Start completed! All related containers have been started."
fi


echo "$MESSAGE_START_DOCKER"

# 启动容器
echo "$MESSAGE_START_CONTAINERS"
docker compose start

docker compose ps -a
echo "$MESSAGE_START_DONE"

