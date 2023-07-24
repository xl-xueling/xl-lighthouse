#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

DEPLOY_USER=''
DEPLOY_PASSWD=''
declare -A NODES_MAP
NODES=()
SERVICES=()

function loadNodesPWD() {
  local FILE=${LDP_HOME}/bin/config/nodelist
  if [ -f "$FILE" ]; then
    while read row; do
      if [ -n "$row" ]; then
        local ip=${row%%;*}
        local passwd=${row#*;}
        NODES_MAP[${ip}]=${passwd}
      fi
	  done < ${FILE}
  fi
}

function loadNodes() {
  local FILE=${LDP_HOME}/bin/config/nodelist.new
  if [ -f "$FILE" ]; then
    while read row; do
          if [ -n "$row" ]; then
                  NODES+=(${row})
          fi
    done < ${FILE}
  else
    log_error "file[nodelist.new] does not exist!";
    exit -1;
  fi
}

declare -A DOWNS_MAP

function loadDowns() {
	while read rows; do
		if [ -n "$rows" ]; then
			local array=($(echo $rows | tr '=' ' '))
			[[ ${array[0]} =~ ^#.* ]] && continue
			DOWNS_MAP["${array[0]}"]=${array[1]}
			SERVICES+=(${array[0]})
		fi
	done <${LDP_HOME}/bin/config/sourcelist
}

declare -A ATTRS_MAP

function loadBasicAttrs() {
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
}

function loadIPS() {
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
		if [ ${ATTRS_MAP['ldp_lighthouse_ice_nodes_size']} -lt 3 ];then
			log_error "The lighthouse-ice component at least three nodes!"
			exit -1;
		fi
		ATTRS_MAP["ldp_lighthouse_ice_master"]=${iceNodesArray[0]}
		ATTRS_MAP["ldp_lighthouse_ice_slaver"]=${iceNodesArray[1]}
		ATTRS_MAP["ldp_lighthouse_ice_locators"]=${iceNodesArray[0]}:4061,${iceNodesArray[1]}:4061
		local web_ips=${ATTRS_MAP['ldp_lighthouse_web_nodes_ips']}
		if [ ! -n "$web_ips" ]; then
                        local nodes_size=${ATTRS_MAP['ldp_lighthouse_web_nodes_size']}
                        local sliceArray=${NODES[@]:0:${nodes_size}}
                        local nodesArray=($(echo ${sliceArray[*]} | tr ' ' ' '))
                        web_ips=$(
                                IFS=,
                                echo "${nodesArray[*]}"
                        )
                fi
		ATTRS_MAP['ldp_lighthouse_web_nodes_ips']=${web_ips}
		local webNodesArray=($(echo $web_ips | tr ',' ' '))
		ATTRS_MAP['ldp_lighthouse_web_nodes_size']=${#webNodesArray[@]}	              
	fi
}

function loadConfigAttrs() {
	local file=${LDP_HOME}/bin/config/config.json
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
				echo "sss"
                                local attrs=$(cat ${file} | jq -r '.'${service}["attrs"] | jq -r keys[])
                                for attr_key in ${attrs[@]}; do
                                        local attr_var=$(cat ${file} | jq -r '.'${service}'["'attrs'"]["'${attr_key}'"]')
                                        ATTRS_MAP["ldp_${service}_${attr_key}"]=${attr_var}
                                done
                        fi
                done
                loadIPS ${service}
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
	DEPLOY_USER=${ATTRS_MAP['ldp_lighthouse_deploy_user']}
	DEPLOY_PASSWD=${ATTRS_MAP['ldp_lighthouse_deploy_user_passwd']}
}

function loadExtendHadoopAttrs() {
	local IPArray=($(getServiceIPS 'hadoop'))
	local hadoop_workers=''
	for ip in ${IPArray[@]:1}; do
		hadoop_workers+="${ip}\n"
	done
	ATTRS_MAP['ldp_hadoop_namenode_ip']=${IPArray[0]}
	ATTRS_MAP['ldp_hadoop_workers']=${hadoop_workers}
}

function loadExtendHBaseAttrs() {
	local IPArray=($(getServiceIPS 'hbase'))
	local regionservers=''
	for ip in ${IPArray[@]:1}; do
		regionservers+="${ip}\n"
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

function loadExtendAttrs() {
	loadExtendZooAttrs
	loadExtendHadoopAttrs
	loadExtendHBaseAttrs
	loadExtendSparkAttrs
	loadExtendMysqlAttrs
	loadExtendKafkaAttrs;
	loadExtendRedisAttrs;
	loadExtendLightHouseAttrs
}

function loadScriptConfig() {
	local clusterId=''
	if [[ ${DEPLOY_FLAG} == "true" ]];then
	  if [[ ! -f "${LDP_HOME}/bin/config/nodelist" ]];then
		  log_error "File[nodelist] does not exist!"
      exit -1;
		fi
		clusterId=`openssl rand -hex 8 | md5sum | cut -c1-8`
		echo $clusterId > ${LDP_HOME}/bin/config/cluster.id
		log_info "Prepare to deploy a new cluster:${clusterId}"
	else
		clusterId=`cat ${LDP_HOME}/bin/config/cluster.id`
		echo "The current operating cluster is:"${clusterId}
	fi
	if [ ! -n "${clusterId}" ];then
                log_error "cluster id not found!"
                exit -1;
        fi	
	ATTRS_MAP["ldp_lighthouse_cluster_id"]=${clusterId}
	loadNodesPWD;
	loadNodes;
	loadDowns
	loadBasicAttrs
	loadConfigAttrs
	loadExtendAttrs
	if [ ! -n "${DEPLOY_USER}" ];then
		log_error "Deployment username cannot be empty!"
		exit -1;
	fi
	if [ ! -n "${DEPLOY_PASSWD}" ];then
                log_error "Deployment user password cannot be empty!"
                exit -1;
        fi
	log_info "The program current deployment user is:${DEPLOY_USER}"
	for var in ${!DOWNS_MAP[@]}; do
		log_info "component:"$var",value:"${DOWNS_MAP[$var]}
	done
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
				if [ $var == "ldp_lighthouse_web_nodes_size"  -a   ${ATTRS_MAP[$var]} -gt 1 ];then
				  log_error "The lighthouse-web component currently does not support multi-node deployment!"
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

