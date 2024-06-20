#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

reload_dir="${LDP_HOME}/temp/reload"

function syncDependencyConf() {
      local userPasswd=($(getUserPassword ${DEPLOY_USER}));
        local service=${1}
        local ip=${2}
        expect ${CUR_DIR}/common/sync.exp ${DEPLOY_USER} "${reload_dir}/${service}/*" ${ip} ${userPasswd} ${LDP_HOME}/dependency/${service}
}

function reloadDependConfig(){
	for service in "${SERVICES[@]}"; do
		local templates_path=${CUR_DIR}/templates/${service}/
		if [[ ! -d ${templates_path} ]] || [[ ! "$(ls -A $templates_path)" ]]; then
			continue;
		fi
		rm -rf ${reload_dir}/${service} && mkdir -p ${reload_dir}/${service}
		local IPArray=($(getServiceIPS ${service}))
		for ip in ${IPArray[@]}; do
			cp -r ${CUR_DIR}/templates/${service}/* ${reload_dir}/${service}
                        for attr in ${!ATTRS_MAP[@]}; do
                       		find ${reload_dir}/${service}/ -type f | xargs --no-run-if-empty sed -i -e 's]${'${attr}'}]'"${ATTRS_MAP[$attr]}"']g'
                        done
			local index=$(getArrayIndex "${NODES[*]}" "${ip}")
			find ${reload_dir}/${service}/ -type f | xargs --no-run-if-empty sed -i -e 's]${ldp_lighthouse_nodeid}]'$(($index + 1))']g'
			if [ "$service" == "redis" ]; then
				for ((a=1;a<=${_REDIS_NUM_PIDS_PER_NODE};a++))
				do
					local port=$[7100+${a}]
					cp -r ${reload_dir}/${service}/conf/redis.conf ${reload_dir}/${service}/conf/redis-${port}.conf
					sed -i 's/${port}/'${port}'/g' ${reload_dir}/${service}/conf/redis-${port}.conf
				done	
			fi	
			syncDependencyConf ${service} ${ip}
		done		
	done
}

function reloadLightConfig(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
        local service="lighthouse"
        local templates_path=${CUR_DIR}/templates/${service}/
        rm -rf ${reload_dir}/${service} && mkdir -p ${reload_dir}/${service}
        for ip in ${NODES[@]}; do
                cp -r ${CUR_DIR}/templates/${service}/* ${reload_dir}/${service}
                for attr in ${!ATTRS_MAP[@]}; do
                        find ${reload_dir}/${service}/ -type f | xargs --no-run-if-empty sed -i -e 's]${'${attr}'}]'"${ATTRS_MAP[$attr]}"']g'
                done
                local index=$(getArrayIndex "${NODES[*]}" "${ip}")
                find ${reload_dir}/${service}/ -type f | xargs --no-run-if-empty sed -i -e 's]${ldp_lighthouse_nodeid}]'$(($index + 1))']g'
                find ${reload_dir}/${service}/ -type f | xargs --no-run-if-empty sed -i -e 's]${ldp_lighthouse_nodeip}]'${ip}']g'
                cp ${reload_dir}/${service}/conf/ldp-site-${RUNNING_MODE}.xml ${reload_dir}/${service}/conf/ldp-site.xml
                remoteExecute ${CUR_DIR}/common/exclude_sync.exp ${DEPLOY_USER} "ldp-site-*.xml" ${reload_dir}/${service}/conf/ ${ip} "${userPasswd}" ${LDP_HOME}/conf/
                remoteExecute ${CUR_DIR}/common/sync.exp ${DEPLOY_USER} ${reload_dir}/${service}/light-webapps/public/* ${ip} "${userPasswd}" ${LDP_HOME}/light-webapps/build/
        done
}

reloadClusterConfig(){
	reloadLightConfig;
	reloadDependConfig;
}

