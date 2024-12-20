#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

LDP_HOME=${1}
NET_MODE=${2}

source ${LDP_HOME}/bin/common/const.sh
source ${LDP_HOME}/bin/common/common.sh
source ${LDP_HOME}/bin/check/check_process.sh

installICEWithYum(){
  pgrep -f 'dnf|yum' | xargs -r kill -9
  sudo rm -f /var/run/yum.pid
  sudo rm -f /var/lib/rpm/.rpm.lock
  sudo rm -f /var/lib/rpm/__db*
  local YUM_OPTS="";
  if [ ${NET_MODE} == "offline" ];then
     YUM_OPTS="--disablerepo=* --enablerepo=xl-lighthouse-repo";
     sudo yum install -y icegrid icebox ice-compilers ice-slice icebridge icegridgui ${YUM_OPTS}
     sudo yum install -y libice* ${YUM_OPTS}
     ls ${LDP_HOME}/package/baselib/python2* >/dev/null 2>&1 && rpm -ivh ${LDP_HOME}/package/baselib/python2*
     ls ${LDP_HOME}/package/baselib/php* >/dev/null 2>&1 && rpm -ivh ${LDP_HOME}/package/baselib/php*
     ls ${LDP_HOME}/package/baselib/javapackages-filesystem-* >/dev/null 2>&1 && rpm -ivh ${LDP_HOME}/package/baselib/javapackages-filesystem-*
  else
    local major=($(getLSBMajorVersion))
    checkPortExist ${_CDN_PACKAGE_MIRROR_IP} ${_CDN_PACKAGE_MIRROR_PORT}
    if [ $? == '0' ];then
      wget http://${_CDN_PACKAGE_MIRROR_IP}:${_CDN_PACKAGE_MIRROR_PORT}/yum-mirror/ice/repo/zeroc-ice-el${major}-cdn.repo -P /etc/yum.repos.d
    fi
    sudo yum install -y https://zeroc.com/download/ice/3.7/el${major}/ice-repo-3.7.el${major}.noarch.rpm
  fi
	sudo yum install -y ice-all-runtime ice-all-devel ${YUM_OPTS}
	rm -f /etc/yum.repos.d/zeroc-ice-*-cdn.repo
}

installICEONUbuntu(){
  sudo rm -f /var/lib/dpkg/lock-frontend
  sudo rm -f /var/cache/apt/archives/lock
  sudo rm -f /var/lib/dpkg/lock
  if [ "${NET_MODE}" != "offline" ]; then
    sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv B6391CB2CFBA643D
    checkPortExist ${_CDN_PACKAGE_MIRROR_IP} ${_CDN_PACKAGE_MIRROR_PORT}
    if [ $? == '0' ];then
      sudo apt-add-repository -y -s "deb http://${_CDN_PACKAGE_MIRROR_IP}:${_CDN_PACKAGE_MIRROR_PORT}/apt-mirror/ice/download/Ice/3.7/ubuntu`lsb_release -rs` stable main"
    fi
    sudo apt-add-repository -y -s "deb http://zeroc.com/download/Ice/3.7/ubuntu`lsb_release -rs` stable main"
    sudo apt-get update
  fi
	sudo apt-get -y install zeroc-ice-all-runtime zeroc-ice-all-dev
	sed -i '/'${_CDN_PACKAGE_MIRROR_IP}'/d' /etc/apt/sources.list
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

function execInstallICE(){
  local lsb=($(getLSBName));
	if [[ "${lsb}" == "CentOS" ]];then
		installICEWithYum
	elif [ "${lsb}" == "Ubuntu" ];then
		installICEONUbuntu;
	elif [ "${lsb}" == "Debian" ];then
		installICEONDebian;
	elif [ "${lsb}" == "Rocky" ];then
    		installICEWithYum;
  elif [ "${lsb}" == "RHEL" ];then
    		installICEWithYum;
	elif [ "${lsb}" == "Alma" ];then
		    installICEWithYum;
	else
    local packageManager=($(getPackageManager));
    if [[ $packageManager == "yum" ]];then
      installICEWithYum;
    else
        echo "The current system version does not support!"
        exit -1;
    fi
	fi
}

execInstallICE $@;
