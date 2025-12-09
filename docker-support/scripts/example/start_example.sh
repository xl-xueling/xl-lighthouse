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

check_required_services() {
    local services=("ldp_insights" "ldp_frontend" "setup_env" "ldp_standalone")
    for service in "${services[@]}"; do
        STATUS=$(service_status "$service")
        if [ "$STATUS" != "running" ]; then
            echo "Error: Please start xl-lighthouse before starting the demo service."
            exit 1
        fi
    done
}

check_required_services

START_STATUS=$(service_status "demo_start")

echo "Current status of demo: $START_STATUS"

if [ "$START_STATUS" = "running" ]; then
    echo "Demo service is already running. "
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

