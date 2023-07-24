#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

killService(){
  local service=$1
  for ip in "${NODES[@]}"
                do
                        remoteExecute ${CUR_DIR}/common/kill.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${service}
                done
}

function stopZookeeper(){
	local IPArray=($(getServiceIPS 'zookeeper'))
        for ip in "${IPArray[@]}"
                do
                        expect ${CUR_DIR}/run/stop_zookeeper.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME}
		            done
	killService 'zookeeper';
	log_info "Program progress,stop zookeeper complete!"
}

function stopHadoop(){
	local namenode=${ATTRS_MAP['ldp_hadoop_namenode_ip']}
	remoteExecute ${CUR_DIR}/run/stop_hadoop.exp ${DEPLOY_USER} ${namenode} ${DEPLOY_PASSWD} ${LDP_HOME}
  killService 'hadoop';
	log_info "Program progress,stop hadoop complete!"
}

function stopHBase(){
	local master=${ATTRS_MAP['ldp_hbase_master']}
        remoteExecute ${CUR_DIR}/run/stop_hbase.exp ${DEPLOY_USER} ${master} ${DEPLOY_PASSWD} ${LDP_HOME}
	killService 'hbase';
	log_info "Program progress,stop hbase complete!"
}

function stopKafka(){
	local IPArray=($(getServiceIPS 'kafka'))
        for ip in "${IPArray[@]}"
                do
			remoteExecute ${CUR_DIR}/run/stop_kafka.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME}
		done
	#killService 'kafka';
	log_info "Program progress,stop kafka complete!"
}


function stopSpark(){
	local master=${ATTRS_MAP['ldp_spark_master']}
        remoteExecute ${CUR_DIR}/run/stop_spark.exp ${DEPLOY_USER} ${master} ${DEPLOY_PASSWD} ${LDP_HOME}
   killService 'spark';
	log_info "Program progress,stop spark complete!"
}

function stopRedis(){
	local clusterPwd=($(getVal 'ldp_redis_cluster_passwd'))
        local IPArray=($(getServiceIPS 'redis'))
	for ip in "${IPArray[@]}"
                do
			for ((a=1;a<=${_REDIS_NUM_PIDS_PER_NODE};a++))
                                do
                                        local port=$[7100+${a}]
                                        #remoteExecute ${CUR_DIR}/run/stop_redis.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME} ${port} ${clusterPwd}
                                done
		done
	killService 'redis';
	log_info "Program progress,stop redis complete!"
}

function stopMysql(){
         local IPArray=($(getServiceIPS 'mysql'))
         for ip in "${IPArray[@]}"
                do
                        remoteExecute ${CUR_DIR}/run/stop_mysql.exp  ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME}
                done
	killService 'mysql';
	log_info "Program progress,stop mysql complete!"
}

function stopLightHouseWeb(){
	killService 'lighthouse-web';
	log_info "Program progress,stop lighthouse-web complete!"
}

function stopLightHouseTasks() {
	killService 'lighthouse-tasks';
	log_info "Program progress,stop lighthouse-tasks complete!"
}

function stopLightHouseICE() {
  killService 'lighthouse-ice';
	log_info "Program progress,stop lighthouse-ice complete!"
}

stop_all(){
	if [[ "${SERVICES[@]}" =~ "kafka" ]];then
                stopKafka;
        fi
	if [[ "${SERVICES[@]}" =~ "redis" ]];then
                stopRedis;
        fi
        if [[ "${SERVICES[@]}" =~ "hbase" ]];then
                stopHBase;
        fi
        if [[ "${SERVICES[@]}" =~ "spark" ]];then
                stopSpark;
        fi
        if [[ "${SERVICES[@]}" =~ "hadoop" ]];then
                stopHadoop;
        fi
	      if [[ "${SERVICES[@]}" =~ "mysql" ]];then
                stopMysql;
        fi
	if [[ "${SERVICES[@]}" =~ "zookeeper" ]];then
                stopZookeeper;
        fi
	stopLightHouseWeb;
  stopLightHouseICE;
  stopLightHouseTasks;
}