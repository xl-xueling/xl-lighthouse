#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

source ${LDP_HOME}/bin/common/common.sh
source ${LDP_HOME}/bin/common/const.sh


function getServiceIPS(){
	local service=$1
	local ips=${ATTRS_MAP['ldp_'${service}'_nodes_ips']}
	local IPArray=(`echo $ips | tr ',' ' '` )
	echo ${IPArray[*]}
}

function getVal(){
  local key=$1
	local value=${ATTRS_MAP[${key}]}
	echo ${value}
}

function getLocalIP(){
        local ipArray=(`ifconfig -a|grep inet|grep -v 127.0.0.1|grep -v inet6|awk '{print $2}'|tr -d "addr:"`);
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
      local clusterPwd=($(getVal 'ldp_redis_cluster_passwd'))
        local IPArray=($(getServiceIPS 'redis'))
              for ip in "${IPArray[@]}"
                do
                        for ((a=1;a<=${_REDIS_NUM_PIDS_PER_NODE};a++))
                                do
                                        local port=$[7100+${a}]
                                        remoteExecute ${LDP_HOME}/bin/tools/redis_fix.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME} ${port} ${clusterPwd}
                                done
                done
}

