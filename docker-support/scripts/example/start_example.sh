#!/bin/bash
# demo-start.sh

set -e

echo "启动 Demo 服务..."

# 函数：检查服务状态
#service_status() {
#    local service=$1
#    if ! docker compose ps -a $service &>/dev/null; then
#        echo "not_exist"
#    elif docker compose ps $service 2>/dev/null | grep -q "Up"; then
#        echo "running"
#    else
#        echo "stopped"
#    fi
#}

service_status() {
    local service=$1
    # 检查容器是否存在（通过 NAMES 列匹配服务名）
    if ! docker compose ps -a --format "{{.Names}}" | grep -q "$service"; then
        echo "not_exist"
    # 如果容器存在，检查容器是否正在运行
    elif docker compose ps --format "{{.Names}} {{.Status}}" | grep -q "$service" && \
         docker compose ps --format "{{.Names}} {{.Status}}" | grep "$service" | grep -q "Up"; then
        echo "running"
    else
        echo "stopped"
    fi
}

# 获取状态
INIT_STATUS=$(service_status "demo_init")
START_STATUS=$(service_status "demo_start")

echo "INIT_STATUS is:${INIT_STATUS}"
echo "START_STATUS is:${START_STATUS}"

echo "demo_init 状态: $INIT_STATUS"
echo "demo_start 状态: $START_STATUS"

# 如果 demo_start 已经在运行，直接退出
if [ "$START_STATUS" = "running" ]; then
    echo "demo_start 已经在运行，无需操作"
    exit 0
fi

# 处理 demo_init
#case "$INIT_STATUS" in
#    "not_exist")
#        echo "创建并启动 demo_init..."
#        docker compose up -d demo_init
#        ;;
#    "stopped")
#        # 检查是否成功完成
#        if docker compose ps -a demo_init | grep -q "Exited (0)"; then
#            echo "demo_init 已成功完成，直接启动容器"
#            docker compose start demo_init
#        else
#            echo "重新运行 demo_init..."
#            docker compose up -d demo_init
#        fi
#        ;;
#    "running")
#        echo "demo_init 正在运行中..."
#        ;;
#esac

# 等待 demo_init 完成（如果是新运行的）
#if [ "$INIT_STATUS" != "running" ]; then
#    echo "等待 demo_init 完成..."
#    for i in {1..30}; do
#        if docker compose ps -a demo_init | grep -q "Exited (0)"; then
#            echo "✓ demo_init 完成"
#            break
#        fi
#        sleep 1
#    done
#fi

# 处理 demo_start
case "$START_STATUS" in
    "not_exist")
        echo "创建并启动 demo_start..."
        docker compose up -d demo_init demo_start
	;;
    "stopped")
        echo "启动 demo_start 容器..."
        docker compose start demo_start
        ;;
esac

echo "=== 完成 ==="
docker compose ps demo_start
