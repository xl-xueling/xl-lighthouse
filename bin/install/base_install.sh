#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

LDP_HOME=${1}

source ${LDP_HOME}/bin/common/common.sh

baseCentOSInstall(){
        sudo rm -f /var/run/yum.pid
        sudo yum clean packages
        sudo yum-config-manager --setopt=timeout=500 --save
        sudo yum-config-manager --setopt=minrate=1 --save
        sudo yum install -y expect jq rsync
	      sudo yum install -y libtool autoconf gcc gcc-c++ make autoconf automake
        sudo yum install -y cmake gzip *openssl* kernel-devel tcl glibc-devel numactl nc
        sudo yum install -y git maven
        sudo yum install -y libncurses*
        sudo yum install -y libaio-devel.x86_64
        sudo yum install -y tcl tcl-devel
        sudo yum install -y snappy*
        sudo yum install -y libzstd*
        sudo yum install -y sysstat iotop
}

baseRockeyInstall(){
	      sudo rm -f /var/run/yum.pid
	      sudo yum clean packages
	      sudo yum-config-manager --setopt=timeout=500 --save
        sudo yum-config-manager --setopt=minrate=1 --save
	      sudo yum install -y expect jq rsync
        sudo yum install -y libtool autoconf gcc gcc-c++ make autoconf automake
        sudo yum install -y cmake gzip *openssl* kernel-devel tcl glibc-devel numactl nc
        sudo yum install -y git maven
        sudo yum install -y libncurses*
        sudo yum install -y libaio-devel.x86_64
        sudo yum install -y tcl tcl-devel
        sudo yum install -y snappy*
        sudo yum install -y libzstd*
        sudo yum install -y sysstat iotop
}

baseAlmaInstall(){
	      sudo rm -f /var/run/yum.pid
	      sudo yum clean packages
	      sudo yum-config-manager --setopt=timeout=500 --save
        sudo yum-config-manager --setopt=minrate=1 --save
	      sudo yum install -y expect jq rsync
        sudo yum install -y libtool autoconf gcc gcc-c++ make autoconf automake
        sudo yum install -y cmake gzip *openssl* kernel-devel tcl glibc-devel numactl nc
        sudo yum install -y git maven
        sudo yum install -y libncurses*
        sudo yum install -y libaio-devel.x86_64
        sudo yum install -y tcl tcl-devel
        sudo yum install -y snappy*
        sudo yum install -y libzstd*
        sudo yum install -y sysstat iotop
}


baseRHELInstall(){
	      sudo rm -f /var/run/yum.pid
	      sudo yum clean packages
	      sudo yum-config-manager --setopt=timeout=500 --save
        sudo yum-config-manager --setopt=minrate=1 --save
	      sudo yum install -y expect jq rsync
        sudo yum install -y libtool autoconf gcc gcc-c++ make autoconf automake
        sudo yum install -y cmake gzip *openssl* kernel-devel tcl glibc-devel numactl nc
        sudo yum install -y git maven
        sudo yum install -y libncurses*
        sudo yum install -y libaio-devel.x86_64
        sudo yum install -y tcl tcl-devel
        sudo yum install -y snappy*
        sudo yum install -y libzstd*
        sudo yum install -y sysstat iotop
}

baseUbuntuInstall(){
	sudo rm -f /var/lib/dpkg/lock-frontend
	sudo rm -f /var/cache/apt/archives/lock
	sudo rm -f /var/lib/dpkg/lock
	sudo apt-get install -y expect jq rsync
	sudo apt-get install -y gcc gcc-multilib 
	sudo apt-get install -y g++ g++-multilib
	sudo apt-get install -y cmake
	sudo apt-get install -y software-properties-common
	sudo apt-get install -y pkg-config
	sudo apt-get install -y libncurses*
	sudo apt-get install -y libtinfo5 libmecab2
	sudo apt-get install -y libaio1
	sudo apt-get install -y libssl-dev
	sudo apt-get install -y openssl
	sudo apt-get install -y zstd
	sudo apt-get install -y libzstd*
	sudo apt-get install -y tcl tk
	sudo apt-get install -y libncurses5
	sudo apt-get install -y build-essential
	sudo apt-get install -y *snappy*
  sudo apt-get install -y sysstat iotop
}

baseDebianInstall(){ 
	sudo rm -f /var/lib/dpkg/lock-frontend
	sudo rm -f /var/cache/apt/archives/lock
	sudo rm -f /var/lib/dpkg/lock
	sudo apt-get install -y expect jq rsync
        sudo apt-get install -y gcc gcc-multilib
        sudo apt-get install -y g++ g++-multilib
        sudo apt-get install -y cmake
        sudo apt-get install -y pkg-config
	      sudo apt-get install -y software-properties-common
        sudo apt-get install -y libncurses*
        sudo apt-get install -y libtinfo5 libmecab2
        sudo apt-get install -y libaio1
        sudo apt-get install -y libssl-dev
        sudo apt-get install -y openssl
        sudo apt-get install -y zstd
        sudo apt-get install -y libzstd*
	sudo apt-get install -y tcl tk
        sudo apt-get install -y libncurses5
        sudo apt-get install -y build-essential
        sudo apt-get install -y *snappy*
        sudo apt-get install -y sysstat iotop
}

baseInstall(){
	local lsb=($(getLSBName));
	if [[ "${lsb}" == "CentOS" ]];then
		baseCentOSInstall;
	elif [ "${lsb}" == "Ubuntu" ];then
		baseUbuntuInstall;
	elif [ "${lsb}" == "Debian" ];then
		baseDebianInstall;
	elif [ "${lsb}" == "Rocky" ];then
    		baseRockeyInstall;
  elif [ "${lsb}" == "RHEL" ];then
    		baseRHELInstall;
	elif [ "${lsb}" == "Alma" ];then
		baseAlmaInstall;
	fi

  if [[ -f "/lib64/libtinfo.so.6" ]] && [[ ! -f "/lib64/libtinfo.so.5" ]];then
    ln -s /lib64/libtinfo.so.6 /lib64/libtinfo.so.5
  fi
  if [[ -f "/usr/lib/x86_64-linux-gnu/libtinfo.so.6" ]] && [[ ! -f "/usr/lib/x86_64-linux-gnu/libtinfo.so.5" ]];then
    ln -s /usr/lib/x86_64-linux-gnu/libtinfo.so.6 /usr/lib/x86_64-linux-gnu/libtinfo.so.5
  fi
}

baseInstall $@;

