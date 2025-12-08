#!/bin/bash

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
PROJECT_ROOT=$(cd "$SCRIPT_DIR/../.." && pwd)
cd "$PROJECT_ROOT" || exit 1

DOCKER_USERNAME="dtstep"
source "$PROJECT_ROOT/.env"
DOCKER_TAG="$VERSION"

IMAGES=(
  "Dockerfile-demoInit:dtstep/ldp-demo-init"
  "Dockerfile-demoStart:dtstep/ldp-demo-start"
  "Dockerfile-frontend:dtstep/ldp-frontend"
  "Dockerfile-insights:dtstep/ldp-insights"
  "Dockerfile-redis:dtstep/ldp-redis"
  "Dockerfile-standalone:dtstep/ldp-standalone"
)

read -s -p "镜像推送，输入Push密码: " DOCKER_PASSWORD
echo

PROXY_SCRIPT="$PROJECT_ROOT/scripts/publish/docker-proxy-switch.sh"
"$PROXY_SCRIPT" on

echo "$DOCKER_PASSWORD" | docker login --username "$DOCKER_USERNAME" --password-stdin
if [ $? -ne 0 ]; then
    echo "Docker 登录失败！"
    exit 1
fi
"$PROXY_SCRIPT" off

for ITEM in "${IMAGES[@]}"; do
    DOCKERFILE_NAME="${ITEM%%:*}"
    IMAGE_NAME="${ITEM##*:}"
    echo "开始处理镜像: $IMAGE_NAME:$DOCKER_TAG"
    echo "Dockerfile: $PROJECT_ROOT/docker/$DOCKERFILE_NAME"
    "$PROXY_SCRIPT" off
    docker build -f "$PROJECT_ROOT/docker/$DOCKERFILE_NAME" -t "$IMAGE_NAME:$DOCKER_TAG" .
    if [ $? -ne 0 ]; then
        echo "构建失败: $IMAGE_NAME"
        exit 1
    fi
    "$PROXY_SCRIPT" on
    docker push "$IMAGE_NAME:$DOCKER_TAG"
    if [ $? -ne 0 ]; then
        echo "推送失败: $IMAGE_NAME"
        exit 1
    fi
done
"$PROXY_SCRIPT" off
echo "镜像均已成功构建并推送！"


