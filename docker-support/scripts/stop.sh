#!/bin/bash

current_language=$(echo $LANG | cut -d_ -f1)

if [[ "$current_language" == "zh" ]]; then
  MESSAGE_STOP_DOCKER="开始停止 Docker 环境..."
  MESSAGE_STOP_CONTAINERS="停止容器..."
  MESSAGE_STOP_DONE="停止完成！所有相关容器已停止。"
else
  MESSAGE_STOP_DOCKER="Stopping Docker environment..."
  MESSAGE_STOP_CONTAINERS="Stopping containers..."
  MESSAGE_STOP_DONE="Stop completed! All related containers have been stopped."
fi

echo "$MESSAGE_STOP_DOCKER"

# 停止容器
echo "$MESSAGE_STOP_CONTAINERS"
docker compose down

echo "$MESSAGE_STOP_DONE"

