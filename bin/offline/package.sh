#!/bin/bash

MODE="cluster";
CUR_DIR=$(cd "$(dirname "$0")";pwd)

declare -A DOWNLOAD_URLS
DOWNLOAD_URLS["cmake"]="https://ldp-soft-1300542249.cos.accelerate.myqcloud.com/cmake-3.25.3-linux-x86_64.tar.gz"
DOWNLOAD_URLS["jdk"]="https://ldp-soft-1300542249.cos.accelerate.myqcloud.com/OpenJDK11U-jdk_x64_linux_hotspot_11.0.20_8.tar.gz"
DOWNLOAD_URLS["scala"]="https://ldp-soft-1300542249.cos.accelerate.myqcloud.com/scala-2.13.10.tgz"
DOWNLOAD_URLS["nginx"]="https://ldp-soft-1300542249.cos.accelerate.myqcloud.com/nginx-1.25.4.tar.gz"
DOWNLOAD_URLS["zookeeper"]="https://ldp-soft-1300542249.cos.accelerate.myqcloud.com/apache-zookeeper-3.5.8-bin.tar.gz"
DOWNLOAD_URLS["kafka"]="https://ldp-soft-1300542249.cos.accelerate.myqcloud.com/kafka_2.12-2.8.2.tgz"
DOWNLOAD_URLS["hadoop"]="https://ldp-soft-1300542249.cos.ap-nanjing.myqcloud.com/hadoop-3.3.5-mininal.tar.gz"
DOWNLOAD_URLS["hbase"]="https://ldp-soft-1300542249.cos.accelerate.myqcloud.com/hbase-2.5.4-hadoop3-bin.tar.gz"
DOWNLOAD_URLS["spark"]="https://ldp-soft-1300542249.cos.accelerate.myqcloud.com/spark-3.3.2-bin-hadoop3.tgz"
DOWNLOAD_URLS["redis"]="https://ldp-soft-1300542249.cos.accelerate.myqcloud.com/redis-6.2.6.tar.gz"
DOWNLOAD_URLS["mysql"]="https://ldp-soft-1300542249.cos.ap-nanjing.myqcloud.com/mysql-8.0.30-linux-glibc2.12-x86_64-mininal.tar.xz"
lsb=""
major=""
_CDN_PACKAGE_MIRROR_IP=123.207.64.67
_CDN_PACKAGE_MIRROR_PORT=39192

main(){
	lsb=($(getLSBName));
	major=($(getLSBMajorVersion))
	echo "lsb is:${lsb},major is:${major}"
	echo "CUR_DIR is:${CUR_DIR}"
	local args=$@
	if [[ "${args[@]}" =~ "--standalone" ]];then
		MODE="standalone";	
	else
		MODE="cluster";
	fi
	echo "main execute,mode:${MODE}";
	if [ ${MODE} == "standalone" ];then
                for value in 'cmake' 'jdk' 'scala' 'nginx' 'redis' 'mysql'; do
                        download ${value}
                done
        else
                for value in "${!DOWNLOAD_URLS[@]}"; do
                        download ${value}
                done
        fi
	local packageManager=($(getPackageManager));
  	echo "packageManager is:${packageManager}"
	if [[ $packageManager == "yum" ]];then
		yumPackage;
	elif [[ $packageManager == "apt-get" ]] ;then
		aptPackage;
	fi
	echo "start to package files!"
	cd ${CUR_DIR};
	local archive_dir="package"
	local archive_name="package-${lsb}${major}-${MODE}.tar.gz"
	tar -zcvf ${archive_name} ${archive_dir}
	echo "success!";	
}

function getLSBName(){
	local DISTRO='';
    if grep -Eqii "CentOS" /etc/issue || grep -Eq "CentOS" /etc/*-release; then
        DISTRO='CentOS'
    elif grep -Eqi "Red Hat Enterprise Linux" /etc/issue || grep -Eq "Red Hat Enterprise Linux" /etc/*-release; then
        DISTRO='RHEL'
    elif grep -Eqi "Debian" /etc/issue || grep -Eq "Debian" /etc/*-release; then
        DISTRO='Debian'
    elif grep -Eqi "Ubuntu" /etc/issue || grep -Eq "Ubuntu" /etc/*-release; then
        DISTRO='Ubuntu'
    elif grep -Eqi "Rocky" /etc/issue || grep -Eq "Rocky" /etc/*-release; then
	      DISTRO='Rocky'
    elif grep -Eqi "Alma" /etc/issue || grep -Eq "AlmaLinux" /etc/*-release; then
	      DISTRO='Alma'
    fi
	      echo $DISTRO;
} 

getLSBMajorVersion(){
	local lsb=($(getLSBName));
  local major=''
  if [ $lsb == "CentOS" ];then
    major=`cat /etc/redhat-release|sed -r 's/.* ([0-9]+)\..*/\1/'`
  elif [ $lsb == "Rocky" ];then
    major=`cat /etc/redhat-release|sed -r 's/.* ([0-9]+)\..*/\1/'`
  elif [ $lsb == "Alma" ];then
    major=`cat /etc/redhat-release|sed -r 's/.* ([0-9]+)\..*/\1/'`
  elif [ $lsb == "RHEL" ];then
    major=`cat /etc/redhat-release|sed -r 's/.* ([0-9]+)\..*/\1/'`
  elif [ $lsb == "Ubuntu" ];then
    major=`lsb_release -rs`
  elif [ $lsb == "Debian" ];then
     local debianVersion=`cat /etc/debian_version`
     major=${debianVersion%%.*}
  else
    major=`cat /etc/redhat-release|sed -r 's/.* ([0-9]+)\..*/\1/'`
  fi
  echo $major;
}

function download(){
	local service=${1}
	local archive_dir=${CUR_DIR}/package/${service}
	mkdir -p ${archive_dir} && rm -rf ${archive_dir}/*
	wget ${DOWNLOAD_URLS[$service]} -P ${archive_dir}
}

function yumPackage(){
  yum -y install yum-utils;
  yum -y install createrepo*
  yum -y install modulemd-tools*
	mkdir -p ${CUR_DIR}/package/baselib && rm -rf ${CUR_DIR}/package/baselib/*
	cd ${CUR_DIR}/package/baselib;
	wget http://123.207.64.67:39192/yum-mirror/ice/repo/zeroc-ice-el${major}-cdn.repo -P /etc/yum.repos.d
	yum install -y https://zeroc.com/download/ice/3.7/el${major}/ice-repo-3.7.el${major}.noarch.rpm
	repotrack ice-all-runtime ice-all-devel
	repotrack yum-utils epel-release expect jq rsync libtool autoconf gcc gcc-c++ make autoconf automake cmake gzip kernel-devel openssl openssl-devel tcl glibc-devel numactl nc git maven libncurses* libaio-devel.x86_64 tcl tcl-devel snappy* libzstd* sysstat iotop wget pcre pcre-devel acl ice-all-runtime ice-all-devel createrepo
	repotrack nmap-ncat;
	createrepo ${CUR_DIR}/package/baselib;
  repo2module ./
	modifyrepo --mdtype=modules modules.yaml repodata/
}

installICEONDebian(){
  local major=($(getLSBMajorVersion))
        sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv B6391CB2CFBA643D
        checkPortExist ${_CDN_PACKAGE_MIRROR_IP} ${_CDN_PACKAGE_MIRROR_PORT}
        if [ $? == '0' ];then
    sudo apt-add-repository -y -s "deb http://${_CDN_PACKAGE_MIRROR_IP}:${_CDN_PACKAGE_MIRROR_PORT}/apt-mirror/ice/download/ice/3.7/debian${major} stable main"
  fi
        sudo apt-add-repository -y -s "deb http://zeroc.com/download/ice/3.7/debian${major} stable main"
        sudo apt-get update
  sudo apt-get -y install zeroc-ice-all-runtime zeroc-ice-all-dev
  sed -i '/'${_CDN_PACKAGE_MIRROR_IP}'/d' /etc/apt/sources.list
}

installICEONUbuntu(){
  sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv B6391CB2CFBA643D
  checkPortExist ${_CDN_PACKAGE_MIRROR_IP} ${_CDN_PACKAGE_MIRROR_PORT}
        if [ $? == '0' ];then
    sudo apt-add-repository -y -s "deb http://${_CDN_PACKAGE_MIRROR_IP}:${_CDN_PACKAGE_MIRROR_PORT}/apt-mirror/ice/download/Ice/3.7/ubuntu`lsb_release -rs` stable main"
  fi
        sudo apt-add-repository -y -s "deb http://zeroc.com/download/Ice/3.7/ubuntu`lsb_release -rs` stable main"
        sudo apt-get update
        sudo apt-get -y install zeroc-ice-all-runtime zeroc-ice-all-dev
        sed -i '/'${_CDN_PACKAGE_MIRROR_IP}'/d' /etc/apt/sources.list
}

function checkPortExist(){
        local ip=$1
        local port=$2
        nc -v -z -w 5 ${ip} ${port} >/dev/null 2>&1
        return $?;
}

function aptPackage(){
        apt-get install -y dpkg-dev;
        mkdir -p ${CUR_DIR}/package/baselib && rm -rf ${CUR_DIR}/package/baselib/*;
        if [[ "${lsb}" == "Debian" ]];then
                installICEONDebian;
        elif [ "${lsb}" == "Ubuntu" ];then
                installICEONUbuntu;
        fi
        apt-get -y -d install software-properties-common expect jq rsync gcc gcc-multilib g++ g++-multilib cmake pkg-config libncurses* libtinfo5 libmecab2 libaio1 libssl-dev openssl zstd netcat-openbsd netcat* libzstd* tcl tk libncurses5 build-essential *snappy* sysstat iotop wget zlib1g-dev libpcre3 libpcre3-dev acl;
        cp /var/cache/apt/archives/*.deb ${CUR_DIR}/package/baselib/
        cd ${CUR_DIR}/package/baselib
        dpkg-scanpackages . /dev/null > Packages
        gzip -k Packages
        xz -k Packages
}

function getPackageManager() {
    if command -v yum &>/dev/null; then
        echo "yum"
    elif command -v apt-get &>/dev/null; then
        echo "apt-get"
    else
        echo "No valid package manager[yum/apt-get] found!"
        exit -1;
    fi
}
main $@;
