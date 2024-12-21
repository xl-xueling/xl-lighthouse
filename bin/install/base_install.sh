#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

LDP_HOME=${1}
NET_MODE=${2}

source ${LDP_HOME}/bin/common/common.sh

baseInstallWithYum(){
        local YUM_OPTS="";
        if [ ${NET_MODE} == "offline" ];then
          YUM_OPTS="--disablerepo=* --enablerepo=xl-lighthouse-repo";
        fi
        local major=($(getLSBMajorVersion));
        local YUM_EXT_OPTS="";
        if [ "$major" -ge 8 ]; then
          YUM_EXT_OPTS="--allowerasing --nobest";
        fi
        pgrep -f 'dnf|yum' | xargs -r kill -9
        sudo rm -f /var/run/yum.pid
        sudo rm -f /var/lib/rpm/.rpm.lock
        sudo rm -f /var/lib/rpm/__db*
        sudo yum clean packages
        sudo yum install ${YUM_EXT_OPTS} -y yum-utils ${YUM_OPTS}
        sudo yum-config-manager --setopt=timeout=500 --save
        sudo yum-config-manager --setopt=minrate=1 --save
        sudo yum install ${YUM_EXT_OPTS} -y epel-release ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y expect jq rsync ${YUM_OPTS}
	      sudo yum install ${YUM_EXT_OPTS} -y libtool autoconf gcc gcc-c++ make autoconf automake ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y cmake gzip kernel-devel openssl openssl-devel ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y tcl glibc-devel numactl ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y nc ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y libncurses* ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y libaio-devel.x86_64 ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y tcl tcl-devel ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y snappy* ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y libzstd* ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y sysstat iotop ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y wget ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y pcre pcre-devel ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y acl ${YUM_OPTS}
        sudo yum install ${YUM_EXT_OPTS} -y nmap-ncat ${YUM_OPTS}
}


baseInstallWithApt(){
	sudo rm -f /var/lib/dpkg/lock-frontend
	sudo rm -f /var/cache/apt/archives/lock
	sudo rm -f /var/lib/dpkg/lock
	sudo apt-get install -y software-properties-common
	sudo apt-get install -y expect jq rsync
	sudo apt-get install -y gcc gcc-multilib 
	sudo apt-get install -y g++ g++-multilib
	sudo apt-get install -y cmake
	sudo apt-get install -y pkg-config
	sudo apt-get install -y libncurses*
	sudo apt-get install -y libtinfo5 libmecab2
	sudo apt-get install -y libaio1
	sudo apt-get install -y libssl-dev
	sudo apt-get install -y openssl
	sudo apt-get install -y zstd
	sudo apt-get install -y netcat-openbsd
	sudo apt-get install -y netcat*
	sudo apt-get install -y libzstd*
	sudo apt-get install -y tcl tk
	sudo apt-get install -y libncurses5
	sudo apt-get install -y build-essential
	sudo apt-get install -y *snappy*
  sudo apt-get install -y sysstat iotop
  sudo apt-get install -y wget
  sudo apt-get install -y zlib1g-dev
  sudo apt-get install -y libpcre3 libpcre3-dev
  sudo apt-get install -y acl
}



baseInstall(){
local packageManager=($(getPackageManager));
  if [[ $packageManager == "yum" ]];then
		baseInstallWithYum;
	elif [[ $packageManager == "apt-get" ]] ;then
		baseInstallWithApt;
	fi

  if [[ -f "/lib64/libtinfo.so.6" ]] && [[ ! -f "/lib64/libtinfo.so.5" ]];then
    ln -s /lib64/libtinfo.so.6 /lib64/libtinfo.so.5
  fi
  if [[ -f "/usr/lib/x86_64-linux-gnu/libtinfo.so.6" ]] && [[ ! -f "/usr/lib/x86_64-linux-gnu/libtinfo.so.5" ]];then
    ln -s /usr/lib/x86_64-linux-gnu/libtinfo.so.6 /usr/lib/x86_64-linux-gnu/libtinfo.so.5
  fi
}

baseInstall $@;

