#!/bin/bash

current_language=$(echo $LANG | cut -d_ -f1)

if [[ "$current_language" == "zh" ]]; then
  MESSAGE_DEPLOY="您即将执行一键部署，系统将拉取镜像并启动所有服务。"
  MESSAGE_INPUT_PROMPT="请输入 'yes' 继续部署: "
  MESSAGE_ABORT="部署已被中止。"
  MESSAGE_START_DEPLOY="开始部署 Docker 环境..."
  MESSAGE_PULL_IMAGES="拉取镜像..."
  MESSAGE_START_CONTAINERS="启动容器..."
  MESSAGE_DEPLOY_DONE="部署完成！"
else
  MESSAGE_DEPLOY="You are about to perform a one-click deployment. The system will pull images and start all services."
  MESSAGE_INPUT_PROMPT="Please enter 'yes' to continue with deployment: "
  MESSAGE_ABORT="Deployment has been aborted."
  MESSAGE_START_DEPLOY="Starting Docker environment deployment..."
  MESSAGE_PULL_IMAGES="Pulling images..."
  MESSAGE_START_CONTAINERS="Starting containers..."
  MESSAGE_DEPLOY_DONE="Deployment completed!"
fi

echo "$MESSAGE_DEPLOY"

read -p "$MESSAGE_INPUT_PROMPT" input
if [[ "$input" != "yes" ]]; then
  echo "$MESSAGE_ABORT"
  exit 1
fi

echo "$MESSAGE_START_DEPLOY"

echo "$MESSAGE_PULL_IMAGES"
docker compose pull

echo "$MESSAGE_START_CONTAINERS"
docker compose up -d

docker compose ps -a
echo "$MESSAGE_DEPLOY_DONE"

