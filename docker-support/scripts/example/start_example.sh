#!/bin/bash
set -e

service_status() {
    local service=$1
    if ! docker compose ps -a --format "{{.Names}}" | grep -q "$service"; then
        echo "not_exist"
    elif docker compose ps --format "{{.Names}} {{.Status}}" | grep -q "$service" && \
         docker compose ps --format "{{.Names}} {{.Status}}" | grep "$service" | grep -q "Up"; then
        echo "running"
    else
        echo "stopped"
    fi
}

INIT_STATUS=$(service_status "demo_init")
START_STATUS=$(service_status "demo_start")

echo "demo_init 状态: $INIT_STATUS"
echo "demo_start 状态: $START_STATUS"

if [ "$START_STATUS" = "running" ]; then
    echo "demo_start 已经在运行，无需操作"
    exit 0
fi

case "$START_STATUS" in
    "not_exist")
        docker compose up -d demo_init demo_start
	;;
    "stopped")
        docker compose start demo_start
        ;;
esac

docker compose ps demo_start
