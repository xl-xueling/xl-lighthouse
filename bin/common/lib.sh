#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

source ${CUR_DIR}/common/common.sh
source ${CUR_DIR}/common/const.sh

function isFolderEmpty() {
    local folder="$1"
    local file_count=$(ls -A "$folder" 2>/dev/null | wc -l)
    if [ "$file_count" -eq 0 ]; then
        echo true
    else
        echo false
    fi
}

function getFolderUsage() {
    local folder="$1"
    du -s "$folder" | cut -f1
}

function getServiceIPS(){
	local service=$1
	local ips=${ATTRS_MAP['ldp_'${service}'_nodes_ips']}
	local IPArray=(`echo $ips | tr ',' ' '` )
	echo ${IPArray[*]}
}

function getUserPassword() {
    local user="$1";
    local ip="$2";
    local passwd;
    if [ ${user} == "root" ];then
      passwd=${NODES_MAP[$ip]}
    elif [ ${user} == ${DEPLOY_USER} ];then
      passwd=${DEPLOY_PASSWD}
    fi

    if [ -z "$passwd" ]; then
      echo "-"
    else
      echo $passwd;
    fi
}


function getVal(){
  local key=$1
	local value=${ATTRS_MAP[${key}]}
	echo ${value}
}

function getAllIps() {
    local ip_addresses=();
    while IFS= read -r line; do
        ip=$(echo "$line" | awk '{print $2}' | cut -d/ -f1)
        if [[ -n "$ip" ]]; then
            ip_addresses+=("$ip")
        fi
    done < <(ip addr show | grep 'inet ')
    echo "${ip_addresses[@]}"
}

function getLocalIP(){
        local ipArray=($(getAllIps));
        for ip in "${ipArray[@]}"
                do
                        for var in "${NODES[@]}"
                                do
                                        if [ ${var} == ${ip} ];then
                                                echo $ip
                                        fi
                                done
                done
}

function remoteExecute(){
	local O_IFS=$IFS
	IFS=$'####'
	log_info "start to execute:${1}"
	expect ${@}
	local result=$?
	if [ "$result" == '101' ];then
		log_error "Password configuration error, process exit!"
		exit -1;
	elif [ "$result" == '102' ];then
		log_error "The directory[${5}] does not exist, failed to sync files to remote server,process exit!"
		exit -1;
	elif [ "$result" == '103' ];then
		log_error "The command[${5}] does not exist, failed to install component to remote server,process exit!"
		exit -1;
	elif [ "${result}" != '0' ];then
		log_info "remote execute[${1}] failed, process exit!"
		exit -1;
	fi
	IFS=$O_IFS
}

function delHDFSDir(){
	local dir=${1}
	if [ ! -n "${dir}" ];then
		log_error "hdfs dir cannot be empty!"
		exit -1;
	fi
  ${LDP_HOME}/dependency/hadoop/bin/hadoop fs -test -e ${dir}
  if [ $? -eq 0 ] ;then
          ${LDP_HOME}/dependency/hadoop/bin/hadoop fs -rm -r ${dir}
  fi
}

function redisClusterFix(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
      local clusterPwd=($(getVal 'ldp_redis_cluster_passwd'))
        local IPArray=($(getServiceIPS 'redis'))
              for ip in "${IPArray[@]}"
                do
                        for ((a=1;a<=${_REDIS_NUM_PIDS_PER_NODE};a++))
                                do
                                        local port=$[7100+${a}]
                                        remoteExecute ${LDP_HOME}/bin/tools/redisfix/redis_fix.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME} ${port} ${clusterPwd}
                                done
                done
}

