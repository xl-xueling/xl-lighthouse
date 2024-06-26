#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

source ~/.bashrc;
eval "$(awk '/^export /,0' ~/.bashrc)"

LOCKFILE=/tmp/lighthouse_example.lock
CMD_PATH=$(cd "$(dirname "$0")";pwd)
CUR_DIR=$(dirname $(dirname "$CMD_PATH"))
source "${CUR_DIR}/common/lib.sh"
source "${CUR_DIR}/prepare/prepare.sh"
source "${CUR_DIR}/install/install.sh"
source "${CUR_DIR}/compile/compile.sh"
source "${CUR_DIR}/deploy/deploy.sh"
source "${CUR_DIR}/check/check.sh"
source "${CUR_DIR}/clean/clean.sh"
source "${CUR_DIR}/run/start.sh"
source "${CUR_DIR}/run/stop.sh"
source "${CUR_DIR}/run/reload.sh"
LOG_FILE="${CUR_DIR}/log/example.log"

main(){
	prepare;
        if [[ ${USER} != ${DEPLOY_USER} ]];then
                log_error "The operation is prohibited, only user[\"${DEPLOY_USER}\"] is allowed to execute!"
                exit -1;
        fi
        jps -l|grep 'com.dtstep.lighthouse.test.LDPFlowTestInstance'|grep -v Jps|awk '{print $1}'|xargs --no-run-if-empty kill -9
	java -Xmx300m -Xms300m -cp ${LDP_HOME}/lib/*:${LDP_HOME}/lib/lighthouse-test-*.jar com.dtstep.lighthouse.test.example.StopExample
	jps -l|grep 'com.dtstep.lighthouse.test.example.StartExample'|grep -v Jps|awk '{print $1}'|xargs --no-run-if-empty kill -9
 	jps -l|grep 'com.dtstep.lighthouse.test.example.StopExample'|grep -v Jps|awk '{print $1}'|xargs --no-run-if-empty kill -9
	rm -f ${LOG_FILE};
	log_info "The statistical example task has been stopped!"
}

main $@;
