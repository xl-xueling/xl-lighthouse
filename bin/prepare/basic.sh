#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

DEPLOY_USER=''
DEPLOY_PASSWD=''
RUNNING_MODE=''
declare -A NODES_MAP
NODES=()
SERVICES=()

function loadNodesPWD() {
  local FILE=${CUR_DIR}/config/nodelist
  if [ -f "$FILE" ]; then
    while read row; do
      if [[ -n "$row" && "$row" != \#* ]]; then
        local ip=${row%%;*}
        local passwd=${row#*;}
        local base64Pwd=$(echo ${passwd} | base64);
        NODES_MAP[${ip}]=${base64Pwd}
      fi
          done < ${FILE}
  fi
}

function loadNodes() {
  local FILE=${CUR_DIR}/config/nodelist.new
  if [ -f "$FILE" ]; then
    while read row; do
          if [[ -n "$row" && "$row" != \#* ]]; then
                  NODES+=(${row})
          fi
    done < ${FILE}
  else
    log_error "file[nodelist.new] does not exist!";
    exit -1;
  fi
}


declare -A DOWNS_MAP

function loadClusterDowns() {
	while read rows; do
		if [ -n "$rows" ]; then
			local array=($(echo $rows | tr '=' ' '))
			[[ ${array[0]} =~ ^#.* ]] && continue
			DOWNS_MAP["${array[0]}"]=${array[1]}
			if [ "$service" == "kafka" -a ${_DEPLOY_LIGHTHOUSE_ICE} == "false" ];then
        continue;
      fi
			SERVICES+=(${array[0]})
		fi
	done <${CUR_DIR}/config/sourcelist
}

function loadStandaloneDowns() {
	local needArray=("cmake" "jdk" "scala" "nginx" "redis" "mysql")
        while read rows; do
                if [ -n "$rows" ]; then
                        local array=($(echo $rows | tr '=' ' '))
                        [[ ${array[0]} =~ ^#.* ]] && continue
			if [[ ! "${needArray[@]}" =~ "${array[0]}" ]];then
				continue;
			fi	
                        DOWNS_MAP["${array[0]}"]=${array[1]}
                        SERVICES+=(${array[0]})
                fi
        done <${CUR_DIR}/config/sourcelist
}

declare -A ATTRS_MAP

function loadStandaloneBasicAttrs() {
	ATTRS_MAP["ldp_jdk_nodes_ips"]=$(
                IFS=,
                echo "${NODES[*]}"
        )
	ATTRS_MAP["ldp_scala_nodes_ips"]=$(
		IFS=,
		echo "${NODES[*]}"
	)
	ATTRS_MAP["ldp_cmake_nodes_ips"]=$(
		IFS=,
		echo "${NODES[*]}"
	)
	ATTRS_MAP["ldp_java_home"]=${LDP_HOME}/dependency/jdk
	ATTRS_MAP["ldp_scala_home"]=${LDP_HOME}/dependency/scala
	ATTRS_MAP["ldp_mysql_home"]=${LDP_HOME}/dependency/mysql
	ATTRS_MAP["ldp_redis_home"]=${LDP_HOME}/dependency/redis
	ATTRS_MAP["ldp_plugins_dir"]=${LDP_HOME}/plugins
	ATTRS_MAP["ldp_data_dir"]=${LDP_DATA_DIR}
	ATTRS_MAP["ldp_lighthouse_home"]=${LDP_HOME}
	ATTRS_MAP["ldp_lighthouse_running_mode"]="standalone"
}

function loadClusterBasicAttrs() {
        ATTRS_MAP["ldp_jdk_nodes_ips"]=$(
                IFS=,
                echo "${NODES[*]}"
        )
        ATTRS_MAP["ldp_scala_nodes_ips"]=$(
                IFS=,
                echo "${NODES[*]}"
        )
        ATTRS_MAP["ldp_cmake_nodes_ips"]=$(
                IFS=,
                echo "${NODES[*]}"
        )
        ATTRS_MAP["ldp_java_home"]=${LDP_HOME}/dependency/jdk
        ATTRS_MAP["ldp_scala_home"]=${LDP_HOME}/dependency/scala
        ATTRS_MAP["ldp_hadoop_home"]=${LDP_HOME}/dependency/hadoop
        ATTRS_MAP["ldp_hbase_home"]=${LDP_HOME}/dependency/hbase
        ATTRS_MAP["ldp_kafka_home"]=${LDP_HOME}/dependency/kafka
        ATTRS_MAP["ldp_zookeeper_home"]=${LDP_HOME}/dependency/zookeeper
        ATTRS_MAP["ldp_spark_home"]=${LDP_HOME}/dependency/spark
        ATTRS_MAP["ldp_mysql_home"]=${LDP_HOME}/dependency/mysql
        ATTRS_MAP["ldp_redis_home"]=${LDP_HOME}/dependency/redis
        ATTRS_MAP["ldp_plugins_dir"]=${LDP_HOME}/plugins
        ATTRS_MAP["ldp_data_dir"]=${LDP_DATA_DIR}
        ATTRS_MAP["ldp_lighthouse_home"]=${LDP_HOME}
        ATTRS_MAP["ldp_lighthouse_running_mode"]="cluster"
}

function loadStandaloneIPS() {
        local service=${1};
        if [ "$service" != "lighthouse" ]; then
                ATTRS_MAP['ldp_'${service}'_nodes_ips']=$(
                                IFS=,
                                echo "${NODES[*]}"
                        )
                ATTRS_MAP['ldp_'${service}'_nodes_size']=1
        else
                ATTRS_MAP['ldp_lighthouse_standalone_nodes_ips']=$(
                                IFS=,
                                echo "${NODES[*]}"
                        )
                ATTRS_MAP['ldp_lighthouse_standalone_nodes_size']=1
                ATTRS_MAP['ldp_lighthouse_insights_nodes_ips']=$(
                                IFS=,
                                echo "${NODES[*]}"
                        )
                ATTRS_MAP['ldp_lighthouse_insights_nodes_size']=1
                ATTRS_MAP['ldp_nginx_nodes_ips']=$(
                                IFS=,
                                echo "${NODES[*]}"
                        )
                ATTRS_MAP['ldp_nginx_nodes_size']=1
                local standalone_ips=${ATTRS_MAP['ldp_lighthouse_standalone_nodes_ips']}
                local iceNodesArray=($(echo $standalone_ips | tr ',' ' '))
                ATTRS_MAP["ldp_lighthouse_ice_locators"]=${iceNodesArray[0]}:4061
        fi
}

function loadClusterIPS() {
	local service=${1}
	if [ "$service" != "lighthouse" ]; then
		local service_ips=${ATTRS_MAP['ldp_'${service}'_nodes_ips']}
		if [ ! -n "$service_ips" ]; then
			local nodes_size=${ATTRS_MAP['ldp_'${service}'_nodes_size']}
			local sliceArray=${NODES[@]:0:${nodes_size}}
			local nodesArray=($(echo ${sliceArray[*]} | tr ' ' ' '))
			service_ips=$(
				IFS=,
				echo "${nodesArray[*]}"
			)
		fi
		ATTRS_MAP['ldp_'${service}'_nodes_ips']=${service_ips}
		local nodesArray=($(echo $service_ips | tr ',' ' '))
		ATTRS_MAP['ldp_'${service}'_nodes_size']=${#nodesArray[@]}
	else
		local ice_ips=${ATTRS_MAP['ldp_lighthouse_ice_nodes_ips']}
		if [ ! -n "$ice_ips" ]; then
                        local nodes_size=${ATTRS_MAP['ldp_lighthouse_ice_nodes_size']}
                        local sliceArray=${NODES[@]:0:${nodes_size}}
                        local nodesArray=($(echo ${sliceArray[*]} | tr ' ' ' '))
                        ice_ips=$(
                                IFS=,
                                echo "${nodesArray[*]}"
                        )
                fi
		ATTRS_MAP['ldp_lighthouse_ice_nodes_ips']=${ice_ips}
		local iceNodesArray=($(echo $ice_ips | tr ',' ' '))
		ATTRS_MAP['ldp_lighthouse_ice_nodes_size']=${#iceNodesArray[@]}
		local ldp_lighthouse_ice_default_locator='';
		if [[ ${#iceNodesArray[@]} -eq 1  ]];then
			ATTRS_MAP["ldp_lighthouse_ice_master"]=${iceNodesArray[0]}
                	ATTRS_MAP["ldp_lighthouse_ice_locators"]=${iceNodesArray[0]}:4061
			ATTRS_MAP["ldp_lighthouse_ice_locators_config"]="LightHouseIceGrid/Locator:tcp -h ${iceNodesArray[0]} -p 4061"
		else
			ATTRS_MAP["ldp_lighthouse_ice_master"]=${iceNodesArray[0]}
                	ATTRS_MAP["ldp_lighthouse_ice_slaver"]=${iceNodesArray[1]}
                	ATTRS_MAP["ldp_lighthouse_ice_locators"]=${iceNodesArray[0]}:4061,${iceNodesArray[1]}:4061
			ATTRS_MAP["ldp_lighthouse_ice_locators_config"]="LightHouseIceGrid/Locator:tcp -h ${iceNodesArray[0]} -p 4061:tcp -h ${iceNodesArray[1]} -p 4061"
		fi	
		local web_ips=${ATTRS_MAP['ldp_lighthouse_insights_nodes_ips']}
		if [ ! -n "$web_ips" ]; then
                        local nodes_size=${ATTRS_MAP['ldp_lighthouse_insights_nodes_size']}
                        local sliceArray=${NODES[@]:0:${nodes_size}}
                        local nodesArray=($(echo ${sliceArray[*]} | tr ' ' ' '))
                        web_ips=$(
                                IFS=,
                                echo "${nodesArray[*]}"
                        )
                fi
		ATTRS_MAP['ldp_lighthouse_insights_nodes_ips']=${web_ips}
		local webNodesArray=($(echo $web_ips | tr ',' ' '))
		ATTRS_MAP['ldp_lighthouse_insights_nodes_size']=${#webNodesArray[@]}
		ATTRS_MAP['ldp_nginx_nodes_ips']=${web_ips}
    ATTRS_MAP['ldp_nginx_nodes_size']=${#webNodesArray[@]}
	fi
}

function loadDeployAttrs() {
	local file=${LDP_HOME}/bin/config/${RUNNING_MODE}-deploy.json;
  echo "Running mode is:${RUNNING_MODE}"
	local services=$(cat ${file} | jq '.' | jq -r keys[])
        for service in ${services[@]};do
                local keys=$(cat ${file} | jq -r '.'${service} | jq -r keys[])
                if [ ! -n "${keys}" ]; then
                        continue
                fi
                for key in ${keys[@]}; do
                        if [ $key != 'attrs' ]; then
                                local var=$(cat ${file} | jq -r '.'${service}'["'${key}'"]')
                                ATTRS_MAP["ldp_${service}_${key}"]=${var}
                        else
                                local attrs=$(cat ${file} | jq -r '.'${service}["attrs"] | jq -r keys[])
                                for attr_key in ${attrs[@]}; do
                                        local attr_var=$(cat ${file} | jq -r '.'${service}'["'attrs'"]["'${attr_key}'"]')
                                        ATTRS_MAP["ldp_${service}_${attr_key}"]=${attr_var}
                                done
                        fi
                done
		if [[ ${RUNNING_MODE} == "standalone" ]];then
			loadStandaloneIPS ${service}
		else
			loadClusterIPS ${service};
		fi	
        done
	DEPLOY_USER=${ATTRS_MAP['ldp_lighthouse_deploy_user']}
	local base64Pwd=$(echo ${ATTRS_MAP['ldp_lighthouse_deploy_user_passwd']} | base64);
  DEPLOY_PASSWD=${base64Pwd}
}

function loadConfigAttrs() {
	local file=${LDP_HOME}/bin/config/${RUNNING_MODE}-config.json;	
	local services=$(cat ${file} | jq '.' | jq -r keys[])
	for service in ${services[@]};do
		local keys=$(cat ${file} | jq -r '.'${service} | jq -r keys[])
                if [ ! -n "${keys}" ]; then
                        continue
                fi
                for key in ${keys[@]}; do
                        if [ $key != 'attrs' ]; then
                                local var=$(cat ${file} | jq -r '.'${service}'["'${key}'"]')
                                ATTRS_MAP["ldp_${service}_${key}"]=${var}
                        else
                                local attrs=$(cat ${file} | jq -r '.'${service}["attrs"] | jq -r keys[])
                                for attr_key in ${attrs[@]}; do
                                        local attr_var=$(cat ${file} | jq -r '.'${service}'["'attrs'"]["'${attr_key}'"]')
                                        ATTRS_MAP["ldp_${service}_${attr_key}"]=${attr_var}
                                done
                        fi
                done
	done
}



function getArrayIndex() {
	local array=$1
	local item=$2
	local index=0
	for i in ${array[*]}; do
		if [[ $item == $i ]]; then
			echo $index
			return
		fi
		index=$(($index + 1))
	done
}

function loadExtendZooAttrs() {
	local IPArray=($(getServiceIPS 'zookeeper'))
	local zoo_servers=''
	for ip in ${IPArray[@]}; do
		local index=$(getArrayIndex "${NODES[*]}" "${ip}")
		zoo_servers+="server.$(($index + 1))=${ip}:2888:3888\n"
	done
	local zoo_ips_port=''
	local zoo_ips=''
	for i in ${!IPArray[@]};do
		if [ $i != '0' ];then
			zoo_ips_port+=","
		fi
		zoo_ips_port+=${IPArray[i]}":2181"
	done
	ATTRS_MAP['ldp_zookeeper_servers']=${zoo_servers}
	ATTRS_MAP['ldp_zookeeper_ips_port']=${zoo_ips_port}
}

function loadExtendRedisAttrs(){
  if [[ ${RUNNING_MODE} == "standalone" ]];then
      _REDIS_NUM_PIDS_PER_NODE=6;
  fi
	local IPArray=($(getServiceIPS 'redis'))
  local redis_cluster=''
	local index=0;
	for ip in "${IPArray[@]}"
                do
                        for ((a=1;a<=${_REDIS_NUM_PIDS_PER_NODE};a++))
                                do
					if [ $index != '0' ];then
                                		redis_cluster+=","
                        		fi
                                        local port=$[7100+${a}]
                                        redis_cluster+="${ip}:${port}"
               				index=$(($index + 1))
		                done
                done
	ATTRS_MAP['ldp_redis_cluster']=${redis_cluster}
	ATTRS_MAP['ldp_redis_cluster_enabled']="yes"
}

function loadExtendKafkaAttrs(){
	local IPArray=($(getServiceIPS 'kafka'))
	local brokers=''
	for i in ${!IPArray[@]};do
                if [ $i != '0' ];then
                        brokers+=","
                fi
                brokers+=${IPArray[i]}":9092"
        done
	ATTRS_MAP['ldp_kafka_brokers_port']=${brokers}
	if [[ ${RUNNING_MODE} == "standalone" ]];then
		_KAFKA_NUM_PARTITIONS=1;
		_KAFKA_REPLICATION_FACTOR=1;
		ATTRS_MAP['ldp_kafka_replication_factor']="1"
		ATTRS_MAP['ldp_kafka_topic_replication_factor']="1"
		ATTRS_MAP['ldp_kafka_state_log_replication_factor']="1"
		ATTRS_MAP['ldp_kafka_state_log_min_isr']="1"
	else
		ATTRS_MAP['ldp_kafka_replication_factor']="3"
		ATTRS_MAP['ldp_kafka_topic_replication_factor']="3"
                ATTRS_MAP['ldp_kafka_state_log_replication_factor']="3"
                ATTRS_MAP['ldp_kafka_state_log_min_isr']="3"
	fi	

}

function loadExtendLightHouseAttrs() {
	local IPArray=($(getServiceIPS 'lighthouse_ice'))
	local ice_nodes=''
	for ip in ${IPArray[@]}; do
		local index=$(getArrayIndex "${NODES[*]}" "${ip}")
		index=$(($index + 1))
		ice_nodes+="\	\<node\ name=\"node"${index}"\"\>\n"
		ice_nodes+="\	\	\<server-instance\ template=\"LightHouseServerIceBoxTemplate\"\ index=\"${index}1\"/\>\n"
		ice_nodes+="\	\	\<server-instance\ template=\"LightHouseServerIceBoxTemplate\"\ index=\"${index}2\"/\>\n"
		ice_nodes+="\	\</node\>\n"
	done
	ATTRS_MAP['ldp_lighthouse_ice_nodes']=${ice_nodes}
}

function loadExtendHadoopAttrs() {
	local IPArray=($(getServiceIPS 'hadoop'))
	local workerIndex=$([[ ${RUNNING_MODE} == "standalone" ]] && echo 0 || echo 1);
	local hadoop_workers=''
	for(( i=${workerIndex};i<${#IPArray[@]};i++ ));
	do
		hadoop_workers+="${IPArray[$i]}\n"
	done
	ATTRS_MAP['ldp_hadoop_namenode_ip']=${IPArray[0]}
	ATTRS_MAP['ldp_hadoop_workers']=${hadoop_workers}
}

function loadExtendHBaseAttrs() {
	local IPArray=($(getServiceIPS 'hbase'))
	local workerIndex=$([[ ${RUNNING_MODE} == "standalone" ]] && echo 0 || echo 1);
	local regionservers=''
	for(( i=${workerIndex};i<${#IPArray[@]};i++ ));
	do
		regionservers+="${IPArray[$i]}\n"
	done
	ATTRS_MAP['ldp_hbase_master']=${IPArray[0]}
	ATTRS_MAP['ldp_hbase_regionservers']=${regionservers}
}

function loadExtendSparkAttrs() {
	local IPArray=($(getServiceIPS 'spark'))
	local workers=''
	for ip in ${IPArray[@]}; do
		workers+="${ip}\n"
	done
	ATTRS_MAP['ldp_spark_master']=${IPArray[0]}
	ATTRS_MAP['ldp_spark_workers']=${workers}
}

function loadExtendMysqlAttrs() {
	local IPArray=($(getServiceIPS 'mysql'))
  	ATTRS_MAP['ldp_mysql_master']=${IPArray[0]}
}

function loadClusterExtendAttrs() {
	loadExtendZooAttrs
	loadExtendHadoopAttrs
	loadExtendHBaseAttrs
	loadExtendSparkAttrs
	loadExtendMysqlAttrs
	loadExtendKafkaAttrs;
	loadExtendRedisAttrs;
	loadExtendLightHouseAttrs
}

function loadStandaloneExtendAttrs() {
	loadExtendMysqlAttrs;
	loadExtendRedisAttrs;
}


function validClusterConfig(){
	for var in ${!ATTRS_MAP[@]}; do
		case "$var" in
			*_nodes_size) {
				if [ ${ATTRS_MAP[$var]} -gt ${#NODES[@]} ];then
					log_error "[${var}] exceeded the total number of nodes in the cluster!"
					exit -1;
				fi
				if [ ${ATTRS_MAP[$var]} -le 0 ];then
                                        log_error "The number of service[${var}] nodes cannot be empty!"
                                        exit -1;
                                fi
				if [ $var == "ldp_mysql_nodes_size"  -a   ${ATTRS_MAP[$var]} -gt 1 ];then
				  log_error "The mysql component currently does not support multi-node deployment!"
					exit -1;
				fi
				if [ $var == "ldp_lighthouse_insights_nodes_size"  -a   ${ATTRS_MAP[$var]} -gt 1 ];then
				  log_error "The lighthouse-insights component currently does not support multi-node deployment!"
					exit -1;
				fi
				if [ $var == "ldp_spark_nodes_size"  -a   ${ATTRS_MAP[$var]} -lt 3 ];then
					log_error "The spark component at least three nodes!"
					exit -1;
				fi
				if [ $var == "ldp_hbase_nodes_size"  -a   ${ATTRS_MAP[$var]} -lt 3 ];then
					log_error "The hbase component at least three nodes!"
					exit -1;
				fi
			  	if [ $var == "ldp_hadoop_nodes_size"  -a   ${ATTRS_MAP[$var]} -lt 3 ];then
					log_error "The hadoop component at least three nodes!"
					exit -1;
				fi
				if [ $var == "ldp_kafka_nodes_size"  -a   ${ATTRS_MAP[$var]} -lt 3 ];then
					log_error "The kafka component at least three nodes!"
					exit -1;
				fi
				if [ $var == "ldp_zookeeper_nodes_size"  -a   ${ATTRS_MAP[$var]} -lt 3 ];then
					log_error "The zookeeper component at least three nodes!"
					exit -1;
				fi
				if [ $var == "ldp_zookeeper_nodes_size"  -a   $((${ATTRS_MAP[$var]}%2)) -eq 0 ];then
          log_error "Zookeeper nodes need to be an odd number!"
          exit -1;
        fi
				if [ $var == "ldp_redis_nodes_size"  -a   ${ATTRS_MAP[$var]} -lt 3 ];then
					log_error "The redis component at least three nodes!"
					exit -1;
				fi
				if [ $var == "ldp_lighthouse_ice_nodes_size" -a ${ATTRS_MAP[$var]} -lt 3 ];then
					log_error "The lighthouse-ice component at least three nodes!"
					exit -1;
				fi	
			};;
			*) ;;
		esac
		log_info "config:"$var",value:"${ATTRS_MAP[$var]}
	done
	if [ ${ATTRS_MAP['ldp_hadoop_nodes_ips']} != ${ATTRS_MAP['ldp_hbase_nodes_ips']} ];then
		log_error "The nodes configuration of hadoop and hbase are inconsistent!"
                exit -1;
	fi
	if [ ${ATTRS_MAP['ldp_hadoop_nodes_ips']} != ${ATTRS_MAP['ldp_spark_nodes_ips']} ];then
                log_error "The nodes configuration of hadoop and spark are inconsistent!"
                exit -1;
        fi	
}

function validStandaloneConfig(){
	for var in ${!ATTRS_MAP[@]}; do
		log_info "config:"$var",value:"${ATTRS_MAP[$var]}
	done	
}

function loadNodesConfig(){
	loadNodesPWD;
  loadNodes;
  local nodesSize=${#NODES[@]}
  if [[ ${DEPLOY_FLAG} == "true" ]];then
    if [ $nodesSize -eq 0 ]; then
          log_error "Cluster node cannot be empty,process exist!"
          exit -1;
    elif [ $nodesSize -eq 1 ]; then
          RUNNING_MODE="standalone"
          echo "standalone" > ${LDP_HOME}/bin/config/running.mode
    elif [ $nodesSize -eq 2 ]; then
          log_error "Cluster deployment requires at least three nodes,process exist!"
          exit -1;
    else
          RUNNING_MODE="cluster"
          echo "cluster" > ${LDP_HOME}/bin/config/running.mode
    fi
  else
    RUNNING_MODE=`cat ${LDP_HOME}/bin/config/running.mode`
  fi
	log_info "Nodes info: [${NODES[*]}],Nodes size:"${nodesSize}",Running mode:"${RUNNING_MODE};

	local clusterId=''
        if [[ ${DEPLOY_FLAG} == "true" ]];then
        if [[ ! -f "${CUR_DIR}/config/nodelist" ]];then
                  log_error "File[nodelist] does not exist!"
      		exit -1;
        fi
                clusterId=`openssl rand -hex 8 | md5sum | cut -c1-8`
                echo $clusterId > ${CUR_DIR}/config/cluster.id
                log_info "Prepare to deploy a new cluster:${clusterId}"
        else
                clusterId=`cat ${CUR_DIR}/config/cluster.id`
                echo "The current operating cluster is:"${clusterId}
        fi
        if [ ! -n "${clusterId}" ];then
                log_error "cluster id not found!"
                exit -1;
        fi
        ATTRS_MAP["ldp_lighthouse_cluster_id"]=${clusterId}
}


function loadClusterConfig(){
	loadClusterBasicAttrs;
	loadClusterDowns;
	loadDeployAttrs;
	loadConfigAttrs;
	loadClusterExtendAttrs;
}

function loadStandaloneConfig(){
	loadStandaloneBasicAttrs;
	loadStandaloneDowns;
	loadDeployAttrs;
	loadConfigAttrs;
	loadStandaloneExtendAttrs;
}

function loadScriptConfig(){
	if [[ ${RUNNING_MODE} == "standalone" ]];then
        	loadStandaloneConfig;
	else
        	loadClusterConfig;
	fi
	if [ ! -n "${DEPLOY_USER}" ];then
                log_error "[deploy_user] cannot be empty!"
                exit -1;
        fi
        if [ "${DEPLOY_USER}" = "root" ]; then
                log_error "[deploy_user] verification failed, the cluster cannot be deployed to 'root' account!"
                exit -1;
        fi
        if [ ! -n "${DEPLOY_PASSWD}" ];then
                log_error "[deploy_user_passwd] cannot be empty!"
                exit -1;
        fi
        log_info "The program current deployment user is:${DEPLOY_USER}"
        for var in ${!DOWNS_MAP[@]}; do
                log_info "component:"$var",value:"${DOWNS_MAP[$var]}
        done

        if [[ ${RUNNING_MODE} == "standalone" ]];then
                validStandaloneConfig;
        else
                validClusterConfig;
        fi
}
