#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

initSystem(){

  sed -i '/vm.swappiness/d' /etc/sysctl.conf
  echo "vm.swappiness = 0" >> /etc/sysctl.conf

  echo never > /sys/kernel/mm/transparent_hugepage/enabled
  echo never > /sys/kernel/mm/transparent_hugepage/defrag

  sed -i '/fs.nr_open/d' /etc/sysctl.conf
  echo "fs.nr_open = 10000000" >> /etc/sysctl.conf

  sed -i '/fs.file-max/d' /etc/sysctl.conf
  echo "fs.file-max = 11000000" >> /etc/sysctl.conf

  sed -i '/net.ipv4.tcp_keepalive_time/d' /etc/sysctl.conf
  echo "net.ipv4.tcp_keepalive_time = 1200" >> /etc/sysctl.conf

  sed -i '/net.core.somaxconn/d' /etc/sysctl.conf
  echo "net.core.somaxconn = 26214" >> /etc/sysctl.conf

  sed -i '/net.ipv4.ip_local_range/d' /etc/sysctl.conf
  echo "net.ipv4.ip_local_range = 1024 65535" >> /etc/sysctl.conf

  sed -i '/net.ipv4.tcp_max_syn_backlog/d' /etc/sysctl.conf
  echo "net.ipv4.tcp_max_syn_backlog = 262144" >> /etc/sysctl.conf

  sed -i '/net.ipv4.tcp_max_tw_buckets/d' /etc/sysctl.conf
  echo "net.ipv4.tcp_max_tw_buckets = 6000" >> /etc/sysctl.conf

  sed -i '/net.core.rmem_max/d' /etc/sysctl.conf
  echo "net.core.rmem_max = 16777216" >> /etc/sysctl.conf

  sed -i '/net.core.wmem_max/d' /etc/sysctl.conf
  echo "net.core.wmem_max = 16777216" >> /etc/sysctl.conf

  sed -i '/net.ipv4.tcp_rmem/d' /etc/sysctl.conf
  echo "net.ipv4.tcp_rmem = 4096 87380 16777216" >> /etc/sysctl.conf

  sed -i '/net.ipv4.tcp_wmem/d' /etc/sysctl.conf
  echo "net.ipv4.tcp_wmem = 4096 65536 16777216" >> /etc/sysctl.conf

  sed -i '/net.ipv4.tcp_fin_timeout/d' /etc/sysctl.conf
  echo "net.ipv4.tcp_fin_timeout = 30" >> /etc/sysctl.conf

  sed -i '/net.ipv4.tcp_sack/d' /etc/sysctl.conf
  echo "net.ipv4.tcp_sack = 0" >> /etc/sysctl.conf

  sed -i '/net.ipv4.tcp_max_orphans/d' /etc/sysctl.conf
  echo "net.ipv4.tcp_max_orphans = 262144" >> /etc/sysctl.conf

  sed -i '/net.ipv4.tcp_syn_retries/d' /etc/sysctl.conf
  echo "net.ipv4.tcp_syn_retries = 2" >> /etc/sysctl.conf

  sed -i '/net.ipv4.tcp_syncookies/d' /etc/sysctl.conf
  echo "net.ipv4.tcp_syncookies = 1" >> /etc/sysctl.conf

  sed -i '/vm.min_free_kbytes/d' /etc/sysctl.conf
  echo "vm.min_free_kbytes = 1024000" >> /etc/sysctl.conf

  sed -i '/vm.zone_reclaim_mode/d' /etc/sysctl.conf
  echo "vm.zone_reclaim_mode = 0" >> /etc/sysctl.conf

  sed -i '/kernel.shmmax/d' /etc/sysctl.conf
  echo "kernel.shmmax = 8589934592" >> /etc/sysctl.conf

  sysctl -p

  sed -i '/soft nofile/d' /etc/security/limits.conf
  sed -i '/hard nofile/d' /etc/security/limits.conf
  echo "root soft nofile 65535" >> /etc/security/limits.conf
  echo "root hard nofile 65535" >> /etc/security/limits.conf
  echo "* soft nofile 65535" >> /etc/security/limits.conf
  echo "* hard nofile 65535" >> /etc/security/limits.conf

  if [[ -f "/etc/needrestart/needrestart.conf" ]];then
    sed -i "/\$nrconf{restart}/s/.*/\$nrconf{restart} = 'a';/" /etc/needrestart/needrestart.conf
  fi
}

initSystem $@;