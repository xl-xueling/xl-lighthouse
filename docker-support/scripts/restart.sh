#!/bin/bash

current_language=$(echo $LANG | cut -d_ -f1)

if [[ "$current_language" == "zh" ]]; then
  MESSAGE_RESTART="您即将执行一键重启，系统将重启所有容器。"
  MESSAGE_INPUT_PROMPT="请输入 'yes' 继续重启: "
  MESSAGE_ABORT="重启已被中止。"
  MESSAGE_RESTART_DOCKER="开始重启 Docker 环境..."
  MESSAGE_RESTART_CONTAINERS="重启容器..."
  MESSAGE_RESTART_DONE="重启完成！所有相关容器已重启。"
else
  MESSAGE_RESTART="You are about to perform a one-click restart. The system will restart all containers."
  MESSAGE_INPUT_PROMPT="Please enter 'yes' to continue with restarting: "
  MESSAGE_ABORT="Restart has been aborted."
  MESSAGE_RESTART_DOCKER="Restarting Docker environment..."
  MESSAGE_RESTART_CONTAINERS="Restarting containers..."
  MESSAGE_RESTART_DONE="Restart completed! All related containers have been restarted."
fi

echo "$MESSAGE_RESTART_DOCKER"

# 重启容器
echo "$MESSAGE_RESTART_CONTAINERS"
docker compose stop
docker compose start
echo "$MESSAGE_RESTART_DONE"

