#!/bin/bash

current_language=$(echo $LANG | cut -d_ -f1)

if [[ "$current_language" == "zh" ]]; then
  MESSAGE_STOP="您即将执行一键停止，系统将停止所有容器。"
  MESSAGE_INPUT_PROMPT="请输入 'yes' 继续停止: "
  MESSAGE_ABORT="停止已被中止。"
  MESSAGE_STOP_DOCKER="开始停止 Docker 环境..."
  MESSAGE_STOP_CONTAINERS="停止容器..."
  MESSAGE_STOP_DONE="停止完成！所有相关容器已停止。"
else
  MESSAGE_STOP="You are about to perform a one-click stop. The system will stop all containers."
  MESSAGE_INPUT_PROMPT="Please enter 'yes' to continue with stopping: "
  MESSAGE_ABORT="Stop has been aborted."
  MESSAGE_STOP_DOCKER="Stopping Docker environment..."
  MESSAGE_STOP_CONTAINERS="Stopping containers..."
  MESSAGE_STOP_DONE="Stop completed! All related containers have been stopped."
fi

echo "$MESSAGE_STOP_DOCKER"

# 停止容器
echo "$MESSAGE_STOP_CONTAINERS"
docker compose stop

echo "$MESSAGE_STOP_DONE"

