#!/bin/bash
#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------
source "${CUR_DIR}/check/check_process.sh"
source "${CUR_DIR}/check/check_command.sh"

function checkInstall() {
    checkJavaCommand;
    checkScalaCommand;
    checkICECommand;
    if [[ "${SERVICES[@]}" =~ "kafka" ]];then
    	checkMysqlCommand;
    fi		    
    if [[ "${SERVICES[@]}" =~ "hadoop" ]];then
    	 checkHadoopCommand;
    fi	    
    if [[ "${SERVICES[@]}" =~ "hbase" ]];then
    	checkHBaseCommand;
    fi	    
    if [[ "${SERVICES[@]}" =~ "spark" ]];then
    	checkSparkCommand;
    fi	    
    if [[ "${SERVICES[@]}" =~ "redis" ]];then
    	checkRedisCommand;
    fi	    
}
