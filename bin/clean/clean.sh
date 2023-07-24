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
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/dependency/hadoop/logs/*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/dependency/hbase/logs/*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/dependency/spark/logs/*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/dependency/kafka/logs/server.log*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/dependency/kafka/logs/state-change.log*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-tasks/*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-tasks/.*.sw*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-ice/*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-ice/.*.sw*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-web/*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${DEPLOY_PASSWD}" "rm -rf ${LDP_HOME}/logs/lighthouse-web/.*.sw*"
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

