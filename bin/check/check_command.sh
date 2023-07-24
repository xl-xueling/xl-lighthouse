#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

function checkLocalCommand(){
	local cmd="$1"
	command -v ${cmd} >/dev/null 2>&1 && return 0 || return 1;
}

function checkICECommand() {
    local IPArray=($(getServiceIPS 'lighthouse_ice'))
    for ip in "${IPArray[@]}"
      do
          remoteExecute ${CUR_DIR}/check/check_cmd_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "icegridadmin"
          remoteExecute ${CUR_DIR}/check/check_cmd_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "icegridnode"
          remoteExecute ${CUR_DIR}/check/check_cmd_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "icegridregistry"
      done
}

function checkJavaCommand() {
    local IPArray=($(getServiceIPS 'jdk'))
    for ip in "${IPArray[@]}"
      do
          remoteExecute ${CUR_DIR}/check/check_cmd_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "java"
      done
}

function checkScalaCommand() {
    local IPArray=($(getServiceIPS 'scala'))
    for ip in "${IPArray[@]}"
      do
          remoteExecute ${CUR_DIR}/check/check_cmd_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "scala"
      done
}

function checkHadoopCommand() {
    local IPArray=($(getServiceIPS 'hadoop'))
    for ip in "${IPArray[@]}"
      do
          remoteExecute ${CUR_DIR}/check/check_cmd_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "hadoop"
      done
}

function checkHBaseCommand() {
    local IPArray=($(getServiceIPS 'hbase'))
    for ip in "${IPArray[@]}"
      do
          remoteExecute ${CUR_DIR}/check/check_cmd_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "hbase"
      done
}

function checkSparkCommand() {
    local IPArray=($(getServiceIPS 'spark'))
    echo "ipArray:${IPArray[*]}"
    for ip in "${IPArray[@]}"
      do
          remoteExecute ${CUR_DIR}/check/check_cmd_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "spark-submit"
      done
}

function checkRedisCommand() {
    local IPArray=($(getServiceIPS 'redis'))
    for ip in "${IPArray[@]}"
      do
          remoteExecute ${CUR_DIR}/check/check_cmd_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "redis-server"
          remoteExecute ${CUR_DIR}/check/check_cmd_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "redis-cli"
      done
}

function checkMysqlCommand() {
    local IPArray=($(getServiceIPS 'mysql'))
    for ip in "${IPArray[@]}"
      do
          remoteExecute ${CUR_DIR}/check/check_cmd_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "mysql"
          remoteExecute ${CUR_DIR}/check/check_cmd_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "mysqld_safe"
      done
}
