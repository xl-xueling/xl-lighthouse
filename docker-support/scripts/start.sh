#!/bin/bash

current_language=$(echo $LANG | cut -d_ -f1)

if [[ "$current_language" == "zh" ]]; then
  MESSAGE_START_DOCKER="开始启动 Docker 环境..."
  MESSAGE_START_CONTAINERS="启动容器..."
  MESSAGE_START_DONE="启动完成！所有相关容器已启动。"
else
  MESSAGE_START_DOCKER="Starting Docker environment..."
  MESSAGE_START_CONTAINERS="Starting containers..."
  MESSAGE_START_DONE="Start completed! All related containers have been started."
fi


echo "$MESSAGE_START_DOCKER"

# 启动容器
echo "$MESSAGE_START_CONTAINERS"
docker compose up -d --build

docker compose ps -a
echo "$MESSAGE_START_DONE"

