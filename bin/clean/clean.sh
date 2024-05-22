#!/bin/bash

function killAll(){
	for ip in "${NODES[@]}"
    do
		if [[ ${RUNNING_MODE} == "standalone" ]];then
			expect ${CUR_DIR}/common/kill.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "standalone-all"
		else
			expect ${CUR_DIR}/common/kill.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "cluster-all"
		fi	
		done
}



function clearLogFiles(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	for ip in "${NODES[@]}"
    		do
    		  if [[ "${SERVICES[@]}" =~ "hadoop" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/dependency/hadoop/logs/*"
    		  fi
    		  if [[ "${SERVICES[@]}" =~ "hbase" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/dependency/hbase/logs/*"
    		  fi
    		  if [[ "${SERVICES[@]}" =~ "spark" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/dependency/spark/logs/*"
    		  fi
    		  if [[ "${SERVICES[@]}" =~ "kafka" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/dependency/kafka/logs/server.log*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/dependency/kafka/logs/state-change.log*"
    		  fi
    		  if [[ "${SERVICES[@]}" =~ "zookeeper" ]];then
    		    expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/dependency/zookeeper/logs/*"
    		  fi
          if [[ ${RUNNING_MODE} == "standalone" ]];then
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-insights/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-insights/.*.sw*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-standalone/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-standalone/.*.sw*"
          else
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-ice/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-ice/.*.sw*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/data/ice/nodeoutput/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-insights/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-insights/.*.sw*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-tasks/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-tasks/.*.sw*"
          fi

          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/bin/log/track_pid.*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf /tmp/lighthouse_gc*"
		    done
}

function clearLightLogFiles(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	for ip in "${NODES[@]}"
                do
          if [[ ${RUNNING_MODE} == "standalone" ]];then
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-insights/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-insights/.*.sw*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-standalone/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-standalone/.*.sw*"
          else
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-ice/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-ice/.*.sw*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/data/ice/nodeoutput/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-insights/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-insights/.*.sw*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-tasks/*"
            expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/logs/lighthouse-tasks/.*.sw*"
          fi
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_HOME}/bin/log/track_pid.*"
          expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf /tmp/lighthouse_gc*"
                    done
}

function clearCheckpoint(){
	delHDFSDir "/lighthouse/checkpoint"
}

function clearRedisBackData(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	for ip in "${NODES[@]}"
                do
			 expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_DATA_DIR}/redis/*.rdb"
			 expect ${CUR_DIR}/common/exec.exp "${DEPLOY_USER}" "${ip}" "${userPasswd}" "rm -rf ${LDP_DATA_DIR}/redis/*.aof"
		done	
}

