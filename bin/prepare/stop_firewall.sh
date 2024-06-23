#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

iptables_enabled() {
    if sudo systemctl is-active --quiet iptables; then
        return 0
    else
        return 1
    fi
}

firewalld_enabled() {
    if sudo systemctl is-active --quiet firewalld; then
        return 0
    else
        return 1
    fi
}

disable_iptables() {
    sudo systemctl stop iptables
    sudo systemctl disable iptables
    echo "iptables stopped!"
}

disable_firewalld() {
    sudo systemctl stop firewalld
    sudo systemctl disable firewalld
    echo "firewalld stopped!"
}

if iptables_enabled; then
    disable_iptables
elif firewalld_enabled; then
    disable_firewalld
fi