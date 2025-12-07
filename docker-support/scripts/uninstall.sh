#!/bin/bash

current_language=$(echo $LANG | cut -d_ -f1)

if [[ "$current_language" == "zh" ]]; then
  MESSAGE_UNINSTALL="您即将执行一键卸载，系统将停止并删除所有容器、卷和镜像。"
  MESSAGE_INPUT_PROMPT="请输入 'yes' 继续卸载，其他任何输入将中止操作: "
  MESSAGE_ABORT="卸载已被中止。"
  MESSAGE_START_UNINSTALL="开始卸载 Docker 环境..."
  MESSAGE_STOP_CONTAINERS="停止并删除容器..."
  MESSAGE_DELETE_VOLUMES="删除所有卷..."
  MESSAGE_DELETE_IMAGES="删除所有镜像..."
  MESSAGE_UNINSTALL_DONE="卸载完成！所有相关资源已被删除。"
else
  MESSAGE_UNINSTALL="You are about to perform a one-click uninstallation. The system will stop and remove all containers, volumes, and images."
  MESSAGE_INPUT_PROMPT="Please enter 'yes' to continue with uninstallation: "
  MESSAGE_ABORT="Uninstallation has been aborted."
  MESSAGE_START_UNINSTALL="Starting Docker environment uninstallation..."
  MESSAGE_STOP_CONTAINERS="Stopping and removing containers..."
  MESSAGE_DELETE_VOLUMES="Removing all volumes..."
  MESSAGE_DELETE_IMAGES="Removing all images..."
  MESSAGE_UNINSTALL_DONE="Uninstallation completed! All related resources have been deleted."
fi

echo "$MESSAGE_UNINSTALL"
read -p "$MESSAGE_INPUT_PROMPT" input

if [[ "$input" != "yes" ]]; then
  echo "$MESSAGE_ABORT"
  exit 1
fi

echo "$MESSAGE_STOP_CONTAINERS"
docker compose down -v

echo "$MESSAGE_START_UNINSTALL"
docker ps -aq --filter name="^dtstep" | xargs -r docker rm -f
docker network ls -q --filter name="^dtstep" | xargs -r docker network rm

echo "$MESSAGE_DELETE_VOLUMES"
docker volume ls --filter name="^dtstep" -q | xargs -r docker volume rm

echo "$MESSAGE_DELETE_IMAGES"
docker images -q --filter reference="dtstep*" | xargs -r docker rmi -f

echo "$MESSAGE_UNINSTALL_DONE"
