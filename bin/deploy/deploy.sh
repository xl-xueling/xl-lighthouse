#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

deployHadoop(){
	local IPArray=($(getServiceIPS 'hadoop'))
        for ip in "${IPArray[@]}"
                do
			remoteExecute ${CUR_DIR}/common/delete.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "${LDP_DATA_DIR}/hadoop/name"
			remoteExecute ${CUR_DIR}/common/delete.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "${LDP_DATA_DIR}/hadoop/hdfsdata"
			remoteExecute ${CUR_DIR}/common/delete.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "${LDP_DATA_DIR}/hadoop/tmp"
		done
	local namenode=${ATTRS_MAP['ldp_hadoop_namenode_ip']}
	remoteExecute ${CUR_DIR}/deploy/deploy_hadoop.exp ${DEPLOY_USER} ${namenode} ${DEPLOY_PASSWD} ${LDP_HOME}
	log_info "Program progress,deploy hadoop complete!"
}

deployMysql(){
	local IPArray=($(getServiceIPS 'mysql'))
	local rootPwd=($(getVal 'ldp_mysql_root_passwd'))
	local operateUser=($(getVal 'ldp_mysql_operate_user'))
	local operateUserPwd=($(getVal 'ldp_mysql_operate_user_passwd'))
  local home=${LDP_HOME}/dependency/mysql
  for ip in "${IPArray[@]}"
		do
			remoteExecute ${CUR_DIR}/deploy/deploy_mysql.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME} ${LDP_DATA_DIR} ${rootPwd} ${operateUser} ${operateUserPwd}
		done
	log_info "Program progress,deploy mysql complete!"
}

deployRedis(){
	local IPArray=($(getServiceIPS 'redis'))
  	local nodes=''
	for ip in "${IPArray[@]}"
                do
                        for ((a=1;a<=${_REDIS_NUM_PIDS_PER_NODE};a++))
				                  do
					                  local port=$[7100+${a}]
					                  nodes=${nodes}" "${ip}:${port}
					                  remoteExecute ${CUR_DIR}/deploy/deploy_redis.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME} ${LDP_DATA_DIR} ${port}
				                  done
                done
	sleep 5
	checkRedis;
	local clusterPwd=($(getVal 'ldp_redis_cluster_passwd'))
	remoteExecute ${CUR_DIR}/deploy/redis_cluster.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME} "${nodes}" ${clusterPwd}
	log_info "Program progress,deploy redis complete!"
}



function deleteZKPath(){
        local node=${1}
        local IPArray=($(getServiceIPS 'zookeeper'))
        local master=${IPArray[0]}
        remoteExecute ${CUR_DIR}/deploy/del_zk_node.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME} ${node}
}



deployKafka(){
	startZookeeper;
	deleteZKPath "/brokers" >/dev/null 2>&1;
	local topicName=($(getVal 'ldp_kafka_topic_name'))
	local IPArray=($(getServiceIPS 'kafka'))
        for ip in "${IPArray[@]}"
                do
			 remoteExecute ${CUR_DIR}/deploy/deploy_kafka.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME}
		done
	sleep 10
	local ip=${IPArray[0]}
	local zoos=($(getVal 'ldp_zookeeper_ips_port'))
	remoteExecute ${CUR_DIR}/deploy/create_topic.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME} ${topicName} "${zoos}"
	log_info "Program progress,deploy kafka complete!"
}


deploy(){
	if [[ "${SERVICES[@]}" =~ "hadoop" ]];then
		deployHadoop;
	fi
	if [[ "${SERVICES[@]}" =~ "mysql" ]];then
		deployMysql;
	fi
	if [[ "${SERVICES[@]}" =~ "redis" ]];then
                deployRedis;
        fi
	if [[ "${SERVICES[@]}" =~ "kafka" ]];then
		deployKafka;
	fi
	authorization;
}


