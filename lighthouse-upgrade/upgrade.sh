#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

source ~/.bashrc;
eval "$(awk '/^export /,0' ~/.bashrc)"
UPGRADE_HOME=$(cd "$(dirname "$0")";pwd)
CUR_DIR=${UPGRADE_HOME}/bin
CUR_USER=${USER}
LOCKFILE=/tmp/lighthouse_upgrade.lock
source "${CUR_DIR}/common/lib.sh"
source "${CUR_DIR}/prepare/prepare.sh"
source "${CUR_DIR}/check/check.sh"
source "${CUR_DIR}/clean/clean.sh"
source "${CUR_DIR}/run/start.sh"
source "${CUR_DIR}/run/stop.sh"
source "${CUR_DIR}/run/reload.sh"

STEPS_TEMP_FILE=${UPGRADE_HOME}/upgrade_steps.tmp
FROM_HOME=$LDP_HOME

if [ -d "$LDP_HOME" ]; then
  cp -rf $LDP_HOME/bin/config ${UPGRADE_HOME}/bin
fi

main(){
  if [ ${USER} != "root" ];then
       echo "The operation is prohibited, only the \"root\" user is allowed to execute!"
       exit -1;
  fi
  [ -e ${LOCKFILE} ] && `cat ${LOCKFILE} | xargs --no-run-if-empty kill -9 >/dev/null 2>&1`
	trap "rm -f ${LOCKFILE}; exit" INT TERM EXIT
	echo $$ > ${LOCKFILE}
  prepare;
	checkProcessExist;
	local steps=0;
	if [ -f $STEPS_TEMP_FILE ]; then
    		read value < $STEPS_TEMP_FILE;
		steps=$(expr $value)
	fi
	echo "current step is:"${steps}
	local upgrade_directory=$(basename "$UPGRADE_HOME")
	local TARGET_VERSION;
	if [[ $upgrade_directory =~ ^lighthouse-upgrade-([0-9]+\.[0-9]+\.[0-9]+)(-pro\.([0-9]+))?$ ]]; then
                TARGET_VERSION="${BASH_REMATCH[1]}${BASH_REMATCH[2]}"
  else
		echo "Upgrade files verification failed!"
    exit -1;
	fi
	local from_directory=$(basename ${LDP_HOME})
	if [[ $from_directory =~ ^lighthouse-([0-9]+\.[0-9]+\.[0-9]+)(-pro\.([0-9]+))?$ ]]; then
                FROM_VERSION="${BASH_REMATCH[1]}${BASH_REMATCH[2]}"
        else
                echo "ldp_home verification failed!"
                exit -1;
        fi
	local TARGET_HOME="$(dirname "$LDP_HOME")/lighthouse-${TARGET_VERSION}"
	echo "FROM_VERSION:"${FROM_VERSION}
	echo "TARGET_VERSION:"${TARGET_VERSION}
	echo "FROM_HOME:"${FROM_HOME}
	echo "TARGET_HOME:"${TARGET_HOME}
	if [ $steps -lt 1 ];then
		for ip in "${NODES[@]}"
                	do
                        	remoteExecute ${CUR_DIR}/check/check_env.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} LDP_HOME ${FROM_HOME}
                	done
        	echo "1" > ${UPGRADE_HOME}/upgrade_steps.tmp
	fi	
	if [ $steps -lt 2 ];then
                for ip in "${NODES[@]}"
                        do
                        	remoteExecute ${CUR_DIR}/check/check_file_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} ${FROM_HOME}
			done
                echo "2" > ${UPGRADE_HOME}/upgrade_steps.tmp
        fi
	if [ $steps -lt 3 ];then
                for ip in "${NODES[@]}"
                        do
                        	remoteExecute ${CUR_DIR}/common/sync.exp ${CUR_USER} ${UPGRADE_HOME}/bin ${ip} ${NODES_MAP[$ip]} ${FROM_HOME}
				remoteExecute ${CUR_DIR}/common/delete.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} ${FROM_HOME}/lib
                        	remoteExecute ${CUR_DIR}/common/sync.exp ${CUR_USER} ${UPGRADE_HOME}/lib ${ip} ${NODES_MAP[$ip]} ${FROM_HOME}
                        	remoteExecute ${CUR_DIR}/common/delete.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} ${FROM_HOME}/light-webapps
                        	remoteExecute ${CUR_DIR}/common/sync.exp ${CUR_USER} ${UPGRADE_HOME}/light-webapps ${ip} ${NODES_MAP[$ip]} ${FROM_HOME}
				remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "chown -R ${DEPLOY_USER}:${DEPLOY_USER} ${LDP_HOME}"
                        	remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "find ${LDP_HOME}/bin -name '*.sh'|xargs chmod +x"
			done
                echo "3" > ${UPGRADE_HOME}/upgrade_steps.tmp
        fi
	if [ $steps -lt 4 ];then
                for ip in "${NODES[@]}"
                        do
                        	remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "mv ${FROM_HOME} ${TARGET_HOME}"	
			done
                echo "4" > ${UPGRADE_HOME}/upgrade_steps.tmp
        fi
	
	if [ $steps -lt 5 ];then
                for ip in "${NODES[@]}"
                        do
				local cmd="find ${TARGET_HOME}/dependency -maxdepth 1 -type l -exec sh -c 'ln -sf \"\$(readlink \"{}\" | sed \"s|'\"$FROM_HOME\"'|'\"$TARGET_HOME\"'|\")\" \"{}\"' \\;"
				echo $cmd;
                                remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "$cmd"
                        done
                echo "5" > ${UPGRADE_HOME}/upgrade_steps.tmp
        fi

   if [ $steps -lt 6 ];then
                for ip in "${NODES[@]}"
                        do
                        	remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "chown -R ${DEPLOY_USER}:${DEPLOY_USER} ${TARGET_HOME}"
                        	remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "find ${TARGET_HOME}/bin -name '*.sh'|xargs chmod +x"
			done
                echo "6" > ${UPGRADE_HOME}/upgrade_steps.tmp
        fi
	
	 if [ $steps -lt 7 ];then
                for ip in "${NODES[@]}"
                        do
                                local cmd1="sed -i \"s|${LDP_HOME}|${TARGET_HOME}|g\" /root/.bashrc";
				remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "$cmd1"
				local cmd2="sed -i \"s|${LDP_HOME}|${TARGET_HOME}|g\" /home/${DEPLOY_USER}/.bashrc";
				remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "$cmd2"
                        done
                echo "7" > ${UPGRADE_HOME}/upgrade_steps.tmp
        fi
	rm -f ${UPGRADE_HOME}/upgrade_steps.tmp;
	rm -f ${LOCKFILE};
	source ~/.bashrc;
	eval "$(awk '/^export /,0' ~/.bashrc)"
	sudo -u ${DEPLOY_USER} ${LDP_HOME}/bin/start-all.sh;
	exit 0;		
}

main $@;


