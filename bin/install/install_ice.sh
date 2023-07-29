#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

LDP_HOME=${1}

source ${LDP_HOME}/bin/common/common.sh

installICEONCentOS(){
  local major=($(getLSBMajorVersion))
  sudo yum install -y https://zeroc.com/download/ice/3.7/el${major}/ice-repo-3.7.el${major}.noarch.rpm
	sudo yum install -y ice-all-runtime ice-all-devel
}

installICEONAlma(){
  local major=($(getLSBMajorVersion))
  sudo yum install -y https://zeroc.com/download/ice/3.7/el${major}/ice-repo-3.7.el${major}.noarch.rpm
  sudo yum install -y ice-all-runtime ice-all-devel
}

installICEONRocky(){
  local major=($(getLSBMajorVersion))
  sudo yum install -y https://zeroc.com/download/ice/3.7/el${major}/ice-repo-3.7.el${major}.noarch.rpm
  sudo yum install -y ice-all-runtime ice-all-devel
}


installICEONRHEL(){
  local major=($(getLSBMajorVersion))
  sudo yum install -y https://zeroc.com/download/ice/3.7/el${major}/ice-repo-3.7.el${major}.noarch.rpm
	sudo yum install -y ice-all-runtime ice-all-devel
}

installICEONUbuntu(){
  sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv B6391CB2CFBA643D
	sudo apt-add-repository -y -s "deb http://zeroc.com/download/Ice/3.7/ubuntu`lsb_release -rs` stable main"
	sudo apt-get update
	sudo apt-get -y install zeroc-ice-all-runtime zeroc-ice-all-dev
}

installICEONDebian(){
  local major=($(getLSBMajorVersion))
	sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv B6391CB2CFBA643D
	sudo apt-add-repository -y -s "deb http://zeroc.com/download/ice/3.7/debian${major} stable main"
	sudo apt-get update
  sudo apt-get -y install zeroc-ice-all-runtime zeroc-ice-all-dev
}

function execInstallICE(){
  local lsb=($(getLSBName));
	if [[ "${lsb}" == "CentOS" ]];then
		installICEONCentOS
	elif [ "${lsb}" == "Ubuntu" ];then
		installICEONUbuntu;
	elif [ "${lsb}" == "Debian" ];then
		installICEONDebian;
	elif [ "${lsb}" == "Rocky" ];then
    		installICEONRocky;
  elif [ "${lsb}" == "RHEL" ];then
    		installICEONRHEL;
	elif [ "${lsb}" == "Alma" ];then
		installICEONAlma;
	fi
}

execInstallICE $@;
