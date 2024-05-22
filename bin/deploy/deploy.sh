#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

deployHadoop(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	local IPArray=($(getServiceIPS 'hadoop'))
        for ip in "${IPArray[@]}"
                do
			remoteExecute ${CUR_DIR}/common/delete.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "${LDP_DATA_DIR}/hadoop/name"
			remoteExecute ${CUR_DIR}/common/delete.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "${LDP_DATA_DIR}/hadoop/hdfsdata"
			remoteExecute ${CUR_DIR}/common/delete.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "${LDP_DATA_DIR}/hadoop/tmp"
		done
	local namenode=${ATTRS_MAP['ldp_hadoop_namenode_ip']}
	remoteExecute ${CUR_DIR}/deploy/deploy_hadoop.exp ${DEPLOY_USER} ${namenode} ${userPasswd} ${LDP_HOME}
	log_info "Program progress,deploy hadoop complete!"
}

deployMysql(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	local IPArray=($(getServiceIPS 'mysql'))
	local rootPwd=($(getVal 'ldp_mysql_root_passwd'))
	local operateUser=($(getVal 'ldp_mysql_operate_user'))
	local operateUserPwd=($(getVal 'ldp_mysql_operate_user_passwd'))
  local home=${LDP_HOME}/dependency/mysql
  for ip in "${IPArray[@]}"
		do
			remoteExecute ${CUR_DIR}/deploy/deploy_mysql.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME} ${LDP_DATA_DIR} ${rootPwd} ${operateUser} ${operateUserPwd}
			if [[ ${RUNNING_MODE} == "standalone" ]];then
			  local clusterId=`cat ${CUR_DIR}/config/cluster.id`
			  local warehouseDBName="cluster_"${clusterId}"_ldp_warehouse";
			  remoteExecute ${CUR_DIR}/deploy/create_mysqldb.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME} ${rootPwd} ${warehouseDBName}
			fi
		done
	log_info "Program progress,deploy mysql complete!"
}

deployRedis(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
  local IPArray=($(getServiceIPS 'redis'))
  if [[ ${RUNNING_MODE} == "standalone" ]];then
    echo "replicaof no one" >> ${LDP_HOME}/dependency/redis/conf/redis-7101.conf
    echo "replicaof ${IPArray[0]} 7101" >> ${LDP_HOME}/dependency/redis/conf/redis-7102.conf
  else
  	local nodes=''
    for ip in "${IPArray[@]}"
                  do
                          for ((a=1;a<=${_REDIS_NUM_PIDS_PER_NODE};a++))
                            do
                              local port=$[7100+${a}]
                              nodes=${nodes}" "${ip}:${port}
                              remoteExecute ${CUR_DIR}/deploy/deploy_redis.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME} ${LDP_DATA_DIR} ${port}
                            done
                  done
    sleep 5
    checkRedis;
    local clusterPwd=($(getVal 'ldp_redis_cluster_passwd'))
    remoteExecute ${CUR_DIR}/deploy/redis_cluster.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME} "${nodes}" ${clusterPwd}
  fi
	log_info "Program progress,deploy redis complete!"
}



function deleteZKPath(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
        local node=${1}
        local IPArray=($(getServiceIPS 'zookeeper'))
        local master=${IPArray[0]}
        remoteExecute ${CUR_DIR}/deploy/del_zk_node.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME} ${node}
}



deployKafka(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	startZookeeper;
	deleteZKPath "/brokers" >/dev/null 2>&1;
	local topicName=($(getVal 'ldp_kafka_topic_name'))
	local IPArray=($(getServiceIPS 'kafka'))
        for ip in "${IPArray[@]}"
                do
			 remoteExecute ${CUR_DIR}/deploy/deploy_kafka.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME}
		done
	sleep 10
	local ip=${IPArray[0]}
	local zoos=($(getVal 'ldp_zookeeper_ips_port'))
	local partitions=${_KAFKA_NUM_PARTITIONS};
	local factor=${_KAFKA_REPLICATION_FACTOR};
	remoteExecute ${CUR_DIR}/deploy/create_topic.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME} ${topicName} "${zoos}" ${partitions} ${factor}
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


