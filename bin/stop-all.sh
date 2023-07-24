#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

CUR_DIR="$PWD"
LDP_HOME=$(dirname "$PWD")
ROOT_HOME=$(dirname "$LDP_HOME")
CUR_USER=${USER}
LOCKFILE=/tmp/lighthouse_stop.lock
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

main(){
    [ -e ${LOCKFILE} ] && `cat ${LOCKFILE} | xargs --no-run-if-empty kill -9 >/dev/null 2>&1`;
    trap "rm -f ${LOCKFILE}; exit" INT TERM EXIT
	  echo $$ > ${LOCKFILE}
  	loadScriptConfig >/dev/null 2>&1;
	if [[ ${USER} != ${DEPLOY_USER} ]];then
        	echo "The operation is prohibited, only user[\"${DEPLOY_USER}\"] is allowed to execute!"
        	exit -1;
  	fi	
	source ~/.bashrc;
	stop_all;
	echo "service has stopped!"
	rm -f ${LOCKFILE}
}

main $@;
