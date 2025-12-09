#!/bin/bash

# Get script directory and project root
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

read -s -p "Enter push password for Docker images: " DOCKER_PASSWORD
echo
PROXY_SCRIPT="$PROJECT_ROOT/scripts/publish/docker-proxy-switch.sh"
"$PROXY_SCRIPT" on

echo "$DOCKER_PASSWORD" | docker login --username "$DOCKER_USERNAME" --password-stdin
if [ $? -ne 0 ]; then
    echo "Docker login failed!"
    exit 1
fi

"$PROXY_SCRIPT" off

for ITEM in "${IMAGES[@]}"; do
    DOCKERFILE_NAME="${ITEM%%:*}"
    IMAGE_NAME="${ITEM##*:}"
    echo "Processing image: $IMAGE_NAME:$DOCKER_TAG"
    echo "Using Dockerfile: $PROJECT_ROOT/docker/$DOCKERFILE_NAME"
    "$PROXY_SCRIPT" off

    docker build -f "$PROJECT_ROOT/docker/$DOCKERFILE_NAME" -t "$IMAGE_NAME:$DOCKER_TAG" .
    if [ $? -ne 0 ]; then
        echo "Build failed: $IMAGE_NAME"
        exit 1
    fi

    "$PROXY_SCRIPT" on

    docker push "$IMAGE_NAME:$DOCKER_TAG"
    if [ $? -ne 0 ]; then
        echo "Push failed: $IMAGE_NAME"
        exit 1
    fi
done

"$PROXY_SCRIPT" off

echo "All images have been successfully built and pushed!"
