#!/bin/bash

function killAll(){
	for ip in "${NODES[@]}"
    do
			expect ${CUR_DIR}/common/kill.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "all"
		done
}

function clearLogFiles(){
	for ip in "${NODES[@]}"
    		do
    		  if [[ "${SERVICES[@]}" =~ "hadoop" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/dependency/hadoop/logs/*"
    		  fi
    		  if [[ "${SERVICES[@]}" =~ "hbase" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/dependency/hbase/logs/*"
    		  fi
    		  if [[ "${SERVICES[@]}" =~ "spark" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/dependency/spark/logs/*"
    		  fi
    		  if [[ "${SERVICES[@]}" =~ "kafka" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/dependency/kafka/logs/server.log*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/dependency/kafka/logs/state-change.log*"
    		  fi
    		  if [[ "${SERVICES[@]}" =~ "zookeeper" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/dependency/zookeeper/logs/*"
    		  fi
    		  if [[ ${_DEPLOY_LIGHTHOUSE_ICE} == "true" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-ice/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-ice/.*.sw*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/data/ice/nodeoutput/*"
    		  fi
    		  if [[ ${_DEPLOY_LIGHTHOUSE_INSIGHTS} == "true" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-insights/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-insights/.*.sw*"
    		  fi
    		  if [[ ${_DEPLOY_LIGHTHOUSE_TASKS} == "true" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-tasks/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-tasks/.*.sw*"
    		  fi
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/bin/log/track_pid.*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf /tmp/lighthouse_gc*"
		    done
}

function clearLightLogFiles(){
	for ip in "${NODES[@]}"
                do
          if [[ ${_DEPLOY_LIGHTHOUSE_ICE} == "true" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-ice/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-ice/.*.sw*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/data/ice/nodeoutput/*"
    		  fi
    		  if [[ ${_DEPLOY_LIGHTHOUSE_INSIGHTS} == "true" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-insights/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-insights/.*.sw*"
    		  fi
    		  if [[ ${_DEPLOY_LIGHTHOUSE_TASKS} == "true" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-tasks/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-tasks/.*.sw*"
    		  fi
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/bin/log/track_pid.*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf /tmp/lighthouse_gc*"
                    done
}

function clearCheckpoint(){
	delHDFSDir "/lighthouse/checkpoint"
}

function clearRedisBackData(){
	for ip in "${NODES[@]}"
                do
			 expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_DATA_DIR}/redis/*.rdb"
			 expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_DATA_DIR}/redis/*.aof"
		done	
}

