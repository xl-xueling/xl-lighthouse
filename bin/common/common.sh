#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

LOG_FILE="${LDP_HOME}/bin/log/log.txt"
mkdir -p ${LDP_HOME}/bin/log/
#>${LOG_FILE}
#exec &>>${LOG_FILE}

function log_info(){
	echo "$*"|sed 's/\r//'
}

function log_error(){
	echo 
	echo "[ERROR]:"$*|sed 's/\r//'
}

function batch_install(){
	if command -v yum >/dev/null 2>&1;then
	  	sudo rm -f /var/run/yum.pid
		sudo yum -y install $@;
	elif command -v apt >/dev/null 2>&1;then
	  sudo rm -f /var/lib/dpkg/lock-frontend
	  sudo rm -f /var/cache/apt/archives/lock
	  sudo rm -f /var/lib/dpkg/lock
	  sudo apt -y install $@
	else
		log_error "The installation command[yum or apt] was not found in the system,process exit!"
		exit -1;
	fi	
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
  fi
  echo $major;
}

checkOSVersion(){
	local lsb=$1
	local major=$2;
	if [[ ! -n $lsb ]] || [[ ! -n $major ]];then
		log_info "The current deployment environment does not support,process exit!"
	        exit -1;	
	elif [[ ${lsb} == "CentOS" ]] || [[ ${lsb} == "Rocky" ]] || [[ ${lsb} == "Alma" ]] || [[ ${lsb} == "RHEL" ]];then
	  local supportVersions=("7" "8" "9")
	  if [[ ! "${supportVersions[@]}" =~ "$major" ]];then
			log_info "The current deployment environment['os':'${lsb}','version:':'${major}'] does not support,process exit!"
			exit -1;
		fi
	elif [ ${lsb} == "Debian" ];then
		local supportVersions=("9" "10" "11")
		if [[ ! "${supportVersions[@]}" =~ "$major" ]];then
			log_info "The current deployment environment['os':'${lsb}','version:':'${major}'] does not support,process exit!"
			exit -1;	
		fi			
	elif [ ${lsb} == "Ubuntu" ];then
		local supportVersions=("16.04" "18.04" "20.04" "22.04")
                if [[ ! "${supportVersions[@]}" =~ "$major" ]];then
                        log_info "The current deployment environment['os':'${lsb}','version:':'${major}'] does not support,process exit!"
                        exit -1;
                fi
	else
		log_info "The current deployment environment['os':'${lsb}','version:':'${major}'] does not support,process exit!"
		exit -1;
	fi
		
}

trim()
{
    trimmed=$1
    trimmed=${trimmed%% }
    trimmed=${trimmed## }
    echo $trimmed
}





