#!/bin/bash
PROXY_CONF="/etc/systemd/system/docker.service.d/http-proxy.conf"
PROXY_CONF_BACKUP="/etc/systemd/system/docker.service.d/http-proxy.conf.disabled"
case "$1" in
    on)
        echo "Enabling Docker proxy..."
        if [ -f "$PROXY_CONF_BACKUP" ]; then
            sudo mv "$PROXY_CONF_BACKUP" "$PROXY_CONF"
        fi
        sudo systemctl restart trojan
        sudo systemctl daemon-reload
        sudo systemctl restart docker
        echo "Proxy enabled"
        sudo systemctl show --property=Environment docker | grep PROXY
        ;;
    off)
        echo "Disabling Docker proxy..."
        if [ -f "$PROXY_CONF" ]; then
            sudo mv "$PROXY_CONF" "$PROXY_CONF_BACKUP"
        fi
        sudo systemctl stop trojan
        sudo systemctl daemon-reload
        sudo systemctl restart docker
        echo "Proxy disabled"
        ;;
    status)
        echo "Current Docker proxy status:"
        sudo systemctl show --property=Environment docker | grep PROXY || echo "No proxy configured"
        ;;
    *)
        echo "Usage: $0 {on|off|status}"
        echo "  on     - Enable proxy"
        echo "  off    - Disable proxy"
        echo "  status - Show current proxy status"
        exit 1
        ;;
esac
