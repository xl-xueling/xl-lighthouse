#!/bin/bash

services=("demo_init" "demo_start")
services_exist=false

cleanup() {
    local service=$1
    local container_id
    container_id=$(docker compose ps -a -q "$service" 2>/dev/null)
    if [ -n "$container_id" ]; then
        services_exist=true
        if docker compose ps -a "$service" --status running --quiet > /dev/null 2>&1; then
            	echo "Stopping service '$service'..."
		docker compose stop "$service"
        fi
        if docker compose rm -f "$service"; then
            echo "Service '$service' has been successfully cleaned up."
        else
            echo "Failed to remove service '$service'."
	    return 1
        fi
    fi
}

for service in "${services[@]}"; do
    cleanup "$service"
done

if [ "$services_exist" = false ]; then
    echo "No demo services were running or available for cleanup."
fi
