#!/bin/bash

source ~/.bashrc;
eval "$(awk '/^export /,0' ~/.bashrc)"
CUR_DIR=${LDP_HOME}/bin
source "${CUR_DIR}/common/lib.sh"
source "${CUR_DIR}/prepare/prepare.sh"
source "${CUR_DIR}/check/check.sh"
source "${CUR_DIR}/clean/clean.sh"
source "${CUR_DIR}/run/start.sh"
source "${CUR_DIR}/run/stop.sh"
source "${CUR_DIR}/run/reload.sh"
CUR_USER=${USER}
current_date=$(date +'%Y-%m-%d')
cur_hostname=$(hostname)
PACK_HOME=${LDP_HOME}/temp/logpack/
DAYS=2;
main(){
  	prepare;
	if [[ ${USER} != ${DEPLOY_USER} ]];then
        	echo "The operation is prohibited, only user[\"${DEPLOY_USER}\"] is allowed to execute!"
        	exit -1;
  	fi
	for ip in "${NODES[@]}"
                do
                	remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} "-" "${CUR_DIR}/tools/logpack/collect.sh ${DAYS}"
		done

	local currentIP=($(getLocalIP));
	for ip in "${NODES[@]}"
                do
			if [ "$ip" == "$currentIP" ]; then
        			continue;
    			fi
			local source_path=${LDP_HOME}/temp/logpack/${current_date}/*.gz;
			local dest_path=${LDP_HOME}/temp/logpack/;
                        remoteExecute ${CUR_DIR}/common/getback_files.exp ${CUR_USER} ${ip} "-" "${LDP_HOME}/temp/logpack/${current_date}/" ${currentIP} "-" ${dest_path}
                done	
	
	find ${PACK_HOME}/${current_date} -type f -name "*.tar.gz" -exec tar -xzf {} -C ${PACK_HOME}/${current_date} \;
	rm -rf ${PACK_HOME}/${current_date}/*.tar.gz;
  cd ${LDP_HOME}/temp/logpack;
  tar zcvf lighthouse-summary-${current_date}.tar.gz ${current_date}
  echo "Already packed log file:${LDP_HOME}/temp/logpack/lighthouse-summary-${current_date}.tar.gz"
}	

main $@;
