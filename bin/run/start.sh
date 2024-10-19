#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

function startZookeeper(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
        local IPArray=($(getServiceIPS 'zookeeper'))
        for ip in "${IPArray[@]}"
		do
			remoteExecute ${CUR_DIR}/run/start_zookeeper.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME}
		done
	checkZookeeper;
}

function startHadoop(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	 local namenode=${ATTRS_MAP['ldp_hadoop_namenode_ip']}
        remoteExecute ${CUR_DIR}/run/start_hadoop.exp ${DEPLOY_USER} ${namenode} ${userPasswd} ${LDP_HOME}
	checkHadoop;
}

function startHBase(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	local master=${ATTRS_MAP['ldp_hbase_master']}
	remoteExecute ${CUR_DIR}/run/start_hbase.exp ${DEPLOY_USER} ${master} ${userPasswd} ${LDP_HOME}
	checkHBase;
}

function startKafka(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	local IPArray=($(getServiceIPS 'kafka'))
        for ip in "${IPArray[@]}"
                do
                         remoteExecute ${CUR_DIR}/run/start_kafka.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME}
                done
	checkKafka;
}

function startSpark(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	local master=${ATTRS_MAP['ldp_spark_master']}
        remoteExecute ${CUR_DIR}/run/start_spark.exp ${DEPLOY_USER} ${master} ${userPasswd} ${LDP_HOME}
	checkSpark;
}

function startLightHouseICE(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	local IPArray=($(getServiceIPS 'lighthouse_ice'))
	local registerIndex=$([[ ${RUNNING_MODE} == "standalone" ]] && echo 0 || echo 1);
	for i in $( seq 0 ${registerIndex} )
		do
			local ip=${IPArray[$i]}
			if [ $i == '0' ];then
				remoteExecute ${CUR_DIR}/run/start_ice.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME} ${LDP_DATA_DIR} 'register_master'
			else
				remoteExecute ${CUR_DIR}/run/start_ice.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME} ${LDP_DATA_DIR} 'register_slaver'
			fi		
		done
	for ip in "${IPArray[@]}"
		do
			remoteExecute ${CUR_DIR}/run/start_ice.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME} ${LDP_DATA_DIR} 'start_node'
		done
	local IPArray=($(getServiceIPS 'lighthouse_ice'))
	local master=${IPArray[0]};
	remoteExecute ${CUR_DIR}/run/start_lighthouse_ice.exp ${DEPLOY_USER} ${master} ${userPasswd} ${LDP_HOME}
	checkLightHouseICE;
}


function startRedis(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
        local IPArray=($(getServiceIPS 'redis'))
	for ip in "${IPArray[@]}" 
                do
                        for ((a=1;a<=${_REDIS_NUM_PIDS_PER_NODE};a++))
                                do
                                        local port=$[7100+${a}]
                                        remoteExecute ${CUR_DIR}/run/start_redis.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME} ${port}
                                done
                done
	checkRedis;
	if [[ ${_REDIS_CLUSTER_FIX_AT_STARTUP} == "true" ]];then
        	redisClusterFix;
  	fi
}

function startLightHouseInsights(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
  local web_xmx_memory=($(getVal 'ldp_lighthouse_insights_xmx_memory'))
	local web_xms_memory=($(getVal 'ldp_lighthouse_insights_xms_memory'))
	local timezone=($(getVal 'ldp_lighthouse_timezone'))
  local IPArray=($(getServiceIPS 'lighthouse_insights'))
	for ip in "${IPArray[@]}"
		do
			local jar_path=$(find "${LDP_HOME}/lib" -type f -name 'lighthouse-pro-insights-*-pro.*.jar' | head -n 1)
      if [[ ! -n $jar_path ]]; then
        jar_path=$(find "${LDP_HOME}/lib" -type f -name 'lighthouse-insights-*.jar' | head -n 1)
      fi
			local serverCmd="nohup java -Xms${web_xms_memory} -Xmx${web_xmx_memory} -XX:+UseG1GC -Dloader.path=${LDP_HOME}/lib,${LDP_HOME}/light-webapps -Duser.timezone=${timezone} -Dfile.encoding=UTF-8 -Dlogging.config=file:${LDP_HOME}/conf/log4j2-insights.xml -Dspring.config.location=${LDP_HOME}/conf/lighthouse-insights.yml -jar ${jar_path} >/dev/null 2>&1 &"
			remoteExecute ${CUR_DIR}/common/exec.exp ${DEPLOY_USER} ${ip} ${userPasswd} "$serverCmd"
			local webappCmd="${LDP_HOME}/dependency/nginx/sbin/nginx -c ${LDP_HOME}/dependency/nginx/conf/nginx.conf -p ${LDP_HOME}/dependency/nginx"
			remoteExecute ${CUR_DIR}/common/exec.exp ${DEPLOY_USER} ${ip} ${userPasswd} "$webappCmd"
		done
	sleep 10;
	checkLightHouseInsights;
}

function startLightHouseStandalone(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
  local standalone_xmx_memory=($(getVal 'ldp_lighthouse_standalone_xmx_memory'))
	local standalone_xms_memory=($(getVal 'ldp_lighthouse_standalone_xms_memory'))
	local timezone=($(getVal 'ldp_lighthouse_timezone'))
  local IPArray=($(getServiceIPS 'lighthouse_standalone'))
	for ip in "${IPArray[@]}"
		do
      local jar_path=$(find "${LDP_HOME}/lib" -type f -name 'lighthouse-pro-standalone-*-pro.*.jar' | head -n 1)
      if [[ ! -n $jar_path ]]; then
        jar_path=$(find "${LDP_HOME}/lib" -type f -name 'lighthouse-standalone-*.jar' | head -n 1)
      fi
			local serverCmd="nohup java -Xms${standalone_xms_memory} -Xmx${standalone_xmx_memory} -XX:+UseG1GC -Duser.timezone=${timezone} -Dfile.encoding=UTF-8 -Dlog4j.configurationFile=${LDP_HOME}/conf/log4j2-standalone.xml -cp ${LDP_HOME}/lib/*:${jar_path} com.dtstep.lighthouse.standalone.executive.LightStandaloneEntrance >/dev/null 2>&1 &";
			remoteExecute ${CUR_DIR}/common/exec.exp ${DEPLOY_USER} ${ip} ${userPasswd} "$serverCmd"
		done
	sleep 10;
	checkLightHouseStandalone;
}

function startMysql(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	 local IPArray=($(getServiceIPS 'mysql'))
         for ip in "${IPArray[@]}"
                do
                        remoteExecute ${CUR_DIR}/run/start_mysql.exp  ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME}
                done
	checkMysql;
}

function startLightHouseTasks(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
  delHDFSDir "/user/work/.sparkStaging/"
  if [[ ${_CLEAR_KAFKA_OFFSET_AT_STARTUP} == "true" ]];then
        clearCheckpoint;
  fi
	local tasks_driver_memory=($(getVal 'ldp_lighthouse_tasks_driver_memory'))
	local tasks_executor_memory=($(getVal 'ldp_lighthouse_tasks_executor_memory'))
	local tasks_direct_memory=($(getVal 'ldp_lighthouse_tasks_direct_memory'))
	local tasks_executor_cores=($(getVal 'ldp_lighthouse_tasks_executor_cores'))
	local tasks_num_executors=($(getVal 'ldp_lighthouse_tasks_num_executors'))
	local timezone=($(getVal 'ldp_lighthouse_timezone'))
	local master=($(getVal 'ldp_spark_master'))
	local jar_path=$(find "${LDP_HOME}/lib" -type f -name 'lighthouse-pro-tasks-*-pro.*.jar' | head -n 1)
  if [[ ! -n $jar_path ]]; then
    jar_path=$(find "${LDP_HOME}/lib" -type f -name 'lighthouse-tasks-*.jar' | head -n 1)
  fi
	local cmd;
	if [[ "${SERVICES[@]}" =~ "hadoop" ]];then
	  cmd="spark-submit --class com.dtstep.lighthouse.tasks.executive.LightHouseEntrance --files ${LDP_HOME}/conf/log4j2-tasks.xml --conf \"spark.driver.extraJavaOptions=-Duser.timezone=${timezone} -Dfile.encoding=UTF-8 -Dlog4j.configurationFile=${LDP_HOME}/conf/log4j2-tasks.xml\"  --conf spark.memory.fraction=0.2 --conf spark.memory.storageFraction=0.1 --conf \"spark.executor.extraJavaOptions=-Duser.timezone=${timezone} -Xloggc:/tmp/gc-lighthouse-%t.log -verbose:gc -XX:-OmitStackTraceInFastThrow -XX:+PrintGCDetails -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -XX:G1HeapRegionSize=16M -XX:MaxDirectMemorySize=${tasks_direct_memory} -XX:ErrorFile=/tmp/hs_err_pid<pid>.log -Dlog4j.configurationFile=${LDP_HOME}/conf/log4j2-tasks.xml\" --master yarn --conf spark.driver.memory=${tasks_driver_memory} --executor-memory ${tasks_executor_memory} --executor-cores ${tasks_executor_cores} --num-executors ${tasks_num_executors} --exclude-packages org.apache.logging.log4j:log4j-slf4j-impl --jars $(find ${LDP_HOME}/lib/ -type f -name "*.jar" ! -name "spring*.jar" -exec readlink -f {} \; | tr '\n' ',') ${jar_path} ${LDP_HOME}/conf/ldp-site.xml >/dev/null 2>&1 &"
	else
	  cmd="spark-submit --class com.dtstep.lighthouse.tasks.executive.LightHouseEntrance --files ${LDP_HOME}/conf/log4j2-tasks.xml --conf \"spark.driver.extraJavaOptions=-Duser.timezone=${timezone} -Dfile.encoding=UTF-8 -Dlog4j.configurationFile=${LDP_HOME}/conf/log4j2-tasks.xml\"  --conf spark.memory.fraction=0.2 --conf spark.memory.storageFraction=0.1 --conf \"spark.executor.extraJavaOptions=-Duser.timezone=${timezone} -Xloggc:/tmp/gc-lighthouse-%t.log -verbose:gc -XX:-OmitStackTraceInFastThrow -XX:+PrintGCDetails -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -XX:G1HeapRegionSize=16M -XX:MaxDirectMemorySize=${tasks_direct_memory} -XX:ErrorFile=/tmp/hs_err_pid<pid>.log -Dlog4j.configurationFile=${LDP_HOME}/conf/log4j2-tasks.xml\" --master spark://${master}:7077 --conf spark.driver.memory=${tasks_driver_memory} --executor-memory ${tasks_executor_memory} --executor-cores ${tasks_executor_cores} --num-executors ${tasks_num_executors} --exclude-packages org.apache.logging.log4j:log4j-slf4j-impl --jars $(find ${LDP_HOME}/lib/ -type f -name "*.jar" ! -name "spring*.jar" -exec readlink -f {} \; | tr '\n' ',') ${jar_path} ${LDP_HOME}/conf/ldp-site.xml >/dev/null 2>&1 &"
	fi
	if [ $CUR_USER == $DEPLOY_USER ];then
		 remoteExecute ${CUR_DIR}/common/exec.exp ${DEPLOY_USER} ${master} "-" "${cmd}"
	else
		 remoteExecute ${CUR_DIR}/common/exec.exp ${DEPLOY_USER} ${master} ${userPasswd} "${cmd}"
	fi
	sleep 3;
	checkLightHouseTasks;
}

function track() {
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
    for ip in "${NODES[@]}"
      do
          remoteExecute ${CUR_DIR}/tools/track.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME}
      done
}

start_all(){
	if [[ "${SERVICES[@]}" =~ "mysql" ]];then
	  log_info "Waiting to start MySQL ..."
		startMysql;
		sleep 10;
	fi
	if [[ "${SERVICES[@]}" =~ "zookeeper" ]];then
	  log_info "Waiting to start Zookeeper ..."
		startZookeeper;
		sleep 10;
	fi
	if [[ "${SERVICES[@]}" =~ "redis" ]];then
	  log_info "Waiting to start Redis ..."
		startRedis;
		sleep 10;
	fi
	if [[ "${SERVICES[@]}" =~ "hadoop" ]];then
	  log_info "Waiting to start Hadoop ..."
		startHadoop;
		sleep 10;
	fi
	if [[ "${SERVICES[@]}" =~ "hbase" ]];then
	  log_info "Waiting to start HBase ..."
		startHBase;
		sleep 10;
	fi
	if [[ "${SERVICES[@]}" =~ "kafka" ]];then
	  log_info "Waiting to start Kafka ..."
		startKafka;
		sleep 10;
	fi
	if [[ "${SERVICES[@]}" =~ "spark" ]];then
	  log_info "Waiting to start Spark ..."
		startSpark;
		sleep 10;
	fi
	log_info "Waiting to start LightHouse ..."
	if [[ ${RUNNING_MODE} == "standalone" ]];then
	    sleep 10;
	    startLightHouseInsights;
	    startLightHouseStandalone;
	else
	  sleep 20;
	  startLightHouseICE;
    startLightHouseInsights;
	  startLightHouseTasks;
	fi
  track;
}


start_lighthouse(){
  if [[ ${RUNNING_MODE} == "standalone" ]];then
	    startLightHouseInsights;
	    startLightHouseStandalone;
	else
	  startLightHouseICE;
    startLightHouseInsights;
	  startLightHouseTasks;
	fi
  track;
}

