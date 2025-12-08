#!/bin/bash

PROXY_CONF="/etc/systemd/system/docker.service.d/http-proxy.conf"
PROXY_CONF_BACKUP="/etc/systemd/system/docker.service.d/http-proxy.conf.disabled"

case "$1" in
    on)
        echo "启用 Docker 代理..."
        if [ -f "$PROXY_CONF_BACKUP" ]; then
            sudo mv "$PROXY_CONF_BACKUP" "$PROXY_CONF"
        fi
	sudo systemctl restart trojan
        sudo systemctl daemon-reload
        sudo systemctl restart docker
        echo "代理已启用"
        sudo systemctl show --property=Environment docker | grep PROXY
        ;;
    off)
        echo "禁用 Docker 代理..."
        if [ -f "$PROXY_CONF" ]; then
            sudo mv "$PROXY_CONF" "$PROXY_CONF_BACKUP"
        fi
	sudo systemctl stop trojan
        sudo systemctl daemon-reload
        sudo systemctl restart docker
        echo "代理已禁用"
        ;;
    status)
        echo "当前 Docker 代理状态:"
        sudo systemctl show --property=Environment docker | grep PROXY || echo "未配置代理"
        ;;
    *)
        echo "用法: $0 {on|off|status}"
        echo "  on     - 启用代理（用于推送镜像到 Docker Hub）"
        echo "  off    - 禁用代理（用于拉取镜像，使用国内源）"
        echo "  status - 查看当前代理状态"
        exit 1
        ;;
esac

