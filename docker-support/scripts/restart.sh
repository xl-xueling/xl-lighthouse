#!/bin/bash

current_language=$(echo $LANG | cut -d_ -f1)

if [[ "$current_language" == "zh" ]]; then
  MESSAGE_RESTART_DOCKER="开始重启 Docker 环境..."
  MESSAGE_RESTART_CONTAINERS="重启容器..."
  MESSAGE_RESTART_DONE="重启完成！所有相关容器已重启。"
else
  MESSAGE_RESTART_DOCKER="Restarting Docker environment..."
  MESSAGE_RESTART_CONTAINERS="Restarting containers..."
  MESSAGE_RESTART_DONE="Restart completed! All related containers have been restarted."
fi

echo "$MESSAGE_RESTART_DOCKER"

# 重启容器
echo "$MESSAGE_RESTART_CONTAINERS"
docker compose down
docker compose down -v demo_init
docker compose down -v demo_start
docker compose up -d --build
echo "$MESSAGE_RESTART_DONE"

