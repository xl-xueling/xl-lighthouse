#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

CUR_DIR=$(cd "$(dirname "$0")";pwd)
LDP_HOME=$(dirname "$CUR_DIR")
LDP_DATA_DIR=${LDP_HOME}/data
ROOT_HOME=$(dirname "$LDP_HOME")
CUR_USER=${USER}
DEPLOY_FLAG="true"
CHECK_OS_VERSION="true"
LOCKFILE=/tmp/lighthouse_deploy.lock
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
            
easy_deploy(){
	trap "rm -f ${LOCKFILE}; exit" INT TERM EXIT
	echo $$ > ${LOCKFILE}
	echo $(exec sh -c 'echo $PPID') >> ${LOCKFILE}
	prepare_for_deploy;
	killAll;
  	install;
  	deploy;
	source ~/.bashrc;
	checkInstall;
	stop_all;
	start_all;
	rm -f ${LOCKFILE}
	echo "XL-LightHouse installed successfully!"
}

main(){
	[ -e ${LOCKFILE} ] && `cat ${LOCKFILE} | xargs --no-run-if-empty kill -9 >/dev/null 2>&1`;
	if [ ${USER} != "root" ];then
		echo "The operation is prohibited, only the \"root\" user is allowed to execute!"
		exit -1;
	fi
	local args=$@
	if [ -d "${LDP_HOME}/dependency" ];then
		echo "=Important=:This operation will delete all data of the cluster,Please execute it carefully !!!"
		if [[ ! "${args[@]}" =~ "--force" ]];then
			echo "Program has been deployed, please delete it and execute again, or execute \"deploy.sh --force\" to enforce it!"
      			rm -f ${LOCKFILE}
			exit -1;
		fi  
	fi
	if [[ "${args[@]}" =~ "--without-checkosversion" ]];then
	  CHECK_OS_VERSION="false";
	fi
	`ps -ef|grep "easy-deploy.sh"|grep -v grep |grep -v $$|awk '{print $2}' |xargs --no-run-if-empty kill -9`
  `ps -ef|grep "lighthouse"|grep -v grep |grep -v $$|awk '{print $2}' |xargs --no-run-if-empty kill -9`
	easy_deploy > ${LOG_FILE} 2>&1 &
	exit 0;	
}

main $@;
