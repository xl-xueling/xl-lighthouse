#!/bin/bash

set -euo pipefail  # 开启严格模式

current_language=$(echo $LANG | cut -d_ -f1)

if [[ "$current_language" == "zh" ]]; then
    MSG_UPGRADE_TEMPLATE="您即将执行一键升级到目标版本：%s，系统将自动拉取镜像并重启所有服务。"
    MSG_INPUT_PROMPT="请输入 'yes' 以继续升级: "
    MSG_START="开始升级 Docker 环境..."
    MSG_INVALID_PATH="未找到有效的安装路径，请检查部署状态。"
    MSG_PULL_IMAGES="正在拉取最新镜像..."
    MSG_RESTART_SERVICES="正在重启服务..."
    MSG_COPY_FILES="正在将升级包文件覆盖到安装路径..."
    MSG_RENAME_PATH="重命名安装路径: %s -> %s"
    MSG_NO_RENAME="安装路径与目标路径相同，无需重命名。"
    MSG_COMPLETE="升级完成！"
    MSG_ABORT="操作已取消。"
    MSG_TARGET_EXISTS="目标路径已存在，请先清理或更改版本号。"
    MSG_CURRENT_PATH="当前 Docker Compose 安装路径: %s"
else
    MSG_UPGRADE_TEMPLATE="You are about to perform a one-click upgrade to version %s. The system will pull images and restart all services."
    MSG_INPUT_PROMPT="Please enter 'yes' to continue the upgrade: "
    MSG_START="Starting Docker environment upgrade..."
    MSG_INVALID_PATH="No valid install path found. Please check deployment status."
    MSG_PULL_IMAGES="Pulling the latest images..."
    MSG_RESTART_SERVICES="Restarting services..."
    MSG_COPY_FILES="Copying upgrade files to installation path..."
    MSG_RENAME_PATH="Renaming installation path: %s -> %s"
    MSG_NO_RENAME="Installation path is the same as target path, skipping rename."
    MSG_COMPLETE="Upgrade completed!"
    MSG_ABORT="Operation aborted."
    MSG_TARGET_EXISTS="Target path already exists. Please clean up or change the version."
    MSG_CURRENT_PATH="Current Docker Compose installation path: %s"
fi


INSTALL_DIR=""
while [[ $# -gt 0 ]]; do
    case $1 in
        --install-path=*)
            INSTALL_DIR="${1#*=}"
            shift
            ;;
        *)
            echo "Unknown option: $1"
            exit 1
            ;;
    esac
done

if [[ -z "$INSTALL_DIR" ]]; then
    echo "Error: --install-path is required."
    exit 1
fi

VERSION=$(grep -oP '^VERSION=\K.*' ./upgrade-files/.env)
printf "$MSG_UPGRADE_TEMPLATE\n" "$VERSION"

read -r -p "$MSG_INPUT_PROMPT" user_input
if [[ "$user_input" != "yes" ]]; then
    echo "$MSG_ABORT"
    exit 1
fi

INSTALL_DIR=$(realpath "$INSTALL_DIR")

printf "$MSG_CURRENT_PATH\n" "$INSTALL_DIR"

required_dirs=("scripts" "templates" "docker" "config")

for d in "${required_dirs[@]}"; do
    if [[ ! -d "$INSTALL_DIR/$d" ]]; then
        echo "Error:$MSG_INVALID_PATH"
	      exit 1
    fi
done

if command -v realpath >/dev/null 2>&1; then
    UPGRADE_DIR=$(realpath "$(dirname "$0")/upgrade-files")
else
    UPGRADE_DIR="$(cd "$(dirname "$0")/upgrade-files" && pwd)"
fi

cd "$INSTALL_DIR"
if [ -x ./scripts/stop.sh ]; then
  ./scripts/stop.sh
fi

docker compose down || true
echo "$MSG_COPY_FILES"
shopt -s dotglob
cp -r "$UPGRADE_DIR"/* "$INSTALL_DIR"/
shopt -u dotglob

NEW_INSTALL_DIR="$(dirname "$INSTALL_DIR")/lighthouse-docker-${VERSION}"

if [[ "$INSTALL_DIR" != "$NEW_INSTALL_DIR" ]]; then
    printf "$MSG_RENAME_PATH\n" "$INSTALL_DIR" "$NEW_INSTALL_DIR"
    mv "$INSTALL_DIR" "$NEW_INSTALL_DIR"
else
    echo "$MSG_NO_RENAME"
fi

cd "$NEW_INSTALL_DIR"

echo "$MSG_PULL_IMAGES"
docker compose pull

echo "$MSG_RESTART_SERVICES"
docker compose up -d --build

docker images --filter "reference=dtstep/*" --format "{{.ID}}" | while read id; do
    if ! docker ps -a --filter "ancestor=$id" --format "{{.ID}}" | grep -q .; then
        echo "delete image is: $id"
        docker rmi "$id"
    fi
done

echo "$MSG_COMPLETE"
