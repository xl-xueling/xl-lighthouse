#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

function startZookeeper(){
        local IPArray=($(getServiceIPS 'zookeeper'))
        for ip in "${IPArray[@]}"
		do
			remoteExecute ${CUR_DIR}/run/start_zookeeper.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME}
		done
	checkZookeeper;
}

function startHadoop(){
	 local namenode=${ATTRS_MAP['ldp_hadoop_namenode_ip']}
        remoteExecute ${CUR_DIR}/run/start_hadoop.exp ${DEPLOY_USER} ${namenode} ${DEPLOY_PASSWD} ${LDP_HOME}
	checkHadoop;
}

function startHBase(){
	local master=${ATTRS_MAP['ldp_hbase_master']}
	remoteExecute ${CUR_DIR}/run/start_hbase.exp ${DEPLOY_USER} ${master} ${DEPLOY_PASSWD} ${LDP_HOME}
	checkHBase;
}

function startKafka(){
	local IPArray=($(getServiceIPS 'kafka'))
        for ip in "${IPArray[@]}"
                do
                         remoteExecute ${CUR_DIR}/run/start_kafka.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME}
                done
	checkKafka;
}

function startSpark(){
	local master=${ATTRS_MAP['ldp_spark_master']}
        remoteExecute ${CUR_DIR}/run/start_spark.exp ${DEPLOY_USER} ${master} ${DEPLOY_PASSWD} ${LDP_HOME}
	checkSpark;
}

function startLightHouseICE(){
	local IPArray=($(getServiceIPS 'lighthouse_ice'))
	for i in {0..1}
		do
			local ip=${IPArray[$i]}
			if [ $i == '0' ];then
				remoteExecute ${CUR_DIR}/run/start_ice.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME} ${LDP_DATA_DIR} 'register_master'
			else
				remoteExecute ${CUR_DIR}/run/start_ice.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME} ${LDP_DATA_DIR} 'register_slaver'
			fi		
		done
	for ip in "${IPArray[@]}"
		do
			remoteExecute ${CUR_DIR}/run/start_ice.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME} ${LDP_DATA_DIR} 'start_node'
		done
	local IPArray=($(getServiceIPS 'lighthouse_ice'))
	local master=${IPArray[0]};
	remoteExecute ${CUR_DIR}/run/start_lighthouse_ice.exp ${DEPLOY_USER} ${master} ${DEPLOY_PASSWD} ${LDP_HOME}
	checkLightHouseICE;
}


function startRedis(){
        local IPArray=($(getServiceIPS 'redis'))
	for ip in "${IPArray[@]}" 
                do
                        for ((a=1;a<=${_REDIS_NUM_PIDS_PER_NODE};a++))
                                do
                                        local port=$[7100+${a}]
                                        remoteExecute ${CUR_DIR}/run/start_redis.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME} ${port}
                                done
                done
	checkRedis;
	if [[ ${_REDIS_CLUSTER_FIX_AT_STARTUP} == "true" ]];then
        	redisClusterFix;
  	fi
}

function startLightHouseWeb(){
  local web_xmx_memory=($(getVal 'ldp_lighthouse_web_xmx_memory'))
	local web_xms_memory=($(getVal 'ldp_lighthouse_web_xms_memory'))
  local IPArray=($(getServiceIPS 'lighthouse_web'))
	for ip in "${IPArray[@]}"
		do
			local jar_path=$(find ${LDP_HOME}/lib -type f -name 'lighthouse-web-*.jar'|head -n 1)
			local cmd="nohup java -Xms${web_xms_memory} -Xmx${web_xmx_memory} -XX:+UseG1GC -Dloader.path=${LDP_HOME}/lib,${LDP_HOME}/light-webapps -Dlogging.config=file:${LDP_HOME}/conf/log4j2-web.xml -Dspring.config.location=${LDP_HOME}/conf/lighthouse-web.yml -jar ${jar_path} >/dev/null 2>&1 &"
			remoteExecute ${CUR_DIR}/common/exec.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} "$cmd"
		done
	checkLightHouseWeb;
}

function startMysql(){
	 local IPArray=($(getServiceIPS 'mysql'))
         for ip in "${IPArray[@]}"
                do
                        remoteExecute ${CUR_DIR}/run/start_mysql.exp  ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME}
                done
	checkMysql;
}

function startLightHouseTasks(){
  if [[ ${_CLEAR_KAFKA_OFFSET_AT_STARTUP} == "true" ]];then
        clearCheckpoint;
  fi
	local tasks_driver_memory=($(getVal 'ldp_lighthouse_tasks_driver_memory'))
	local tasks_executor_memory=($(getVal 'ldp_lighthouse_tasks_executor_memory'))
	local tasks_direct_memory=($(getVal 'ldp_lighthouse_tasks_direct_memory'))
	local tasks_executor_cores=($(getVal 'ldp_lighthouse_tasks_executor_cores'))
	local tasks_num_executors=($(getVal 'ldp_lighthouse_tasks_num_executors')) 
	local cmd="spark-submit --class com.dtstep.lighthouse.tasks.executive.LightHouseEntrance --files ${LDP_HOME}/conf/log4j2-tasks.xml --conf \"spark.driver.extraJavaOptions=-Dlog4j.configurationFile=${LDP_HOME}/conf/log4j2-tasks.xml\"  --conf spark.memory.fraction=0.2 --conf spark.memory.storageFraction=0.1 --conf \"spark.executor.extraJavaOptions=-Xloggc:/tmp/gc-lighthouse-%t.log -verbose:gc -XX:+PrintGCDetails -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -XX:G1HeapRegionSize=16M -XX:MaxDirectMemorySize=${tasks_direct_memory} -XX:ErrorFile=/tmp/hs_err_pid<pid>.log -Dlog4j.configurationFile=${LDP_HOME}/conf/log4j2-tasks.xml\" --master yarn --conf spark.driver.memory=${tasks_driver_memory} --executor-memory ${tasks_executor_memory} --executor-cores ${tasks_executor_cores} --num-executors ${tasks_num_executors} --jars $(echo ${LDP_HOME}/lib/*.jar | tr ' ' ',') ${LDP_HOME}/lib/lighthouse-tasks-*.jar ${LDP_HOME}/conf/ldp-site.xml >/dev/null 2>&1 &"
	 local master=($(getVal 'ldp_spark_master'))
	if [ $CUR_USER == $DEPLOY_USER ];then
		 remoteExecute ${CUR_DIR}/common/exec.exp ${DEPLOY_USER} ${master} "-" "${cmd}"
	else
		 remoteExecute ${CUR_DIR}/common/exec.exp ${DEPLOY_USER} ${master} ${DEPLOY_PASSWD} "${cmd}"
	fi
	sleep 3;
	checkLightHouseTasks;
}

function track() {
    for ip in "${NODES[@]}"
      do
          remoteExecute ${CUR_DIR}/tools/track.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME}
      done
}

start_all(){
  log_info "Waiting to start MySQL ..."
	if [[ "${SERVICES[@]}" =~ "mysql" ]];then
		startMysql;
	fi
  log_info "Waiting to start Zookeeper ..."
  sleep 10;
	if [[ "${SERVICES[@]}" =~ "zookeeper" ]];then
		startZookeeper;
	fi
	log_info "Waiting to start Redis ..."
	sleep 10;
	if [[ "${SERVICES[@]}" =~ "redis" ]];then
		startRedis;
	fi
	log_info "Waiting to start Hadoop ..."
	sleep 10;
	if [[ "${SERVICES[@]}" =~ "hadoop" ]];then
		startHadoop;
	fi
	log_info "Waiting to start HBase ..."
	sleep 10;
	if [[ "${SERVICES[@]}" =~ "hbase" ]];then
		startHBase;
	fi
	log_info "Waiting to start Kafka ..."
	sleep 10;
	if [[ "${SERVICES[@]}" =~ "kafka" ]];then
		startKafka;
	fi
	log_info "Waiting to start Spark ..."
	sleep 10;
	if [[ "${SERVICES[@]}" =~ "spark" ]];then
		startSpark;
	fi
	log_info "Waiting to start LightHouse ..."
	sleep 20;
	startLightHouseICE;
	startLightHouseWeb;
  startLightHouseTasks;
  track;
}




