#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

source "${LDP_HOME}/bin/prepare/basic.sh"


pre(){
	cat ${CUR_DIR}/config/nodelist | awk -F ';' '{print $1}' > ${CUR_DIR}/config/nodelist.new
  export PS1="[\u@\h \W]\$" && batch_install expect jq rsync*;
	loadScriptConfig;
	log_info "Program progress,load script config complete!"
	for ip in "${NODES[@]}"
                do
			remoteExecute ${CUR_DIR}/common/verify.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]}
		done
	log_info "Program progress,verify cluster nodes passwd complete!"

	for ip in "${NODES[@]}"
		do
		remoteExecute ${CUR_DIR}/prepare/init_pre.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} ${DEPLOY_USER} ${DEPLOY_PASSWD};
		done
	log_info "Program progress,init pre complete!"

	for ip in "${NODES[@]}"
      do
        local lsb=($(getLSBName));
        if [[ $lsb == "CentOS" ]] || [[ $lsb == "Rocky" ]] || [[ $lsb == "Alma" ]] || [[ $lsb == "RHEL" ]];then
          remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "sudo rm -f /var/run/yum.pid"
			    remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "sudo yum -y install rsync"
        elif [[ $lsb == "Debian" ]] || [[ $lsb == "Ubuntu" ]] ;then
          remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "sudo rm -f /var/lib/dpkg/lock-frontend"
			    remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "sudo rm -f /var/cache/apt/archives/lock"
			    remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "sudo rm -f /var/lib/dpkg/lock"
          remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "sudo apt-get -y install rsync*"
	      fi
      done
      log_info "Program progress,install rsync lib complete!"
}

syncPackage(){
	find ${LDP_HOME}/bin -name "*.sh"|xargs chmod +x
	for ip in "${NODES[@]:1}"
                do
      remoteExecute ${CUR_DIR}/common/exclude_sync.exp ${CUR_USER} "nodelist" ${LDP_HOME}/bin ${ip} ${NODES_MAP[$ip]} ${LDP_HOME}
      remoteExecute ${CUR_DIR}/check/check_file_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} ${LDP_HOME}/bin
			remoteExecute ${CUR_DIR}/common/exclude_sync.exp ${CUR_USER} "" ${LDP_HOME}/plugins ${ip} ${NODES_MAP[$ip]} ${LDP_HOME}
      remoteExecute ${CUR_DIR}/check/check_file_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} ${LDP_HOME}/plugins
			remoteExecute ${CUR_DIR}/common/exclude_sync.exp ${CUR_USER} "" ${LDP_HOME}/lib ${ip} ${NODES_MAP[$ip]} ${LDP_HOME}
			remoteExecute ${CUR_DIR}/check/check_file_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} ${LDP_HOME}/lib
      remoteExecute ${CUR_DIR}/common/exclude_sync.exp ${CUR_USER} "" ${LDP_HOME}/light-webapps ${ip} ${NODES_MAP[$ip]} ${LDP_HOME}
			remoteExecute ${CUR_DIR}/check/check_file_exist.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} ${LDP_HOME}/light-webapps
		done
		log_info "Program progress,sync package complete!"
}

baseInit(){
	for ip in "${NODES[@]}"
		do
			remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "${CUR_DIR}/prepare/init_path.sh ${DEPLOY_USER} ${LDP_HOME} ${LDP_DATA_DIR}"
		done
	log_info "Program progress,init path complete!"
	for ip in "${NODES[@]}"
		do
			remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "${CUR_DIR}/prepare/init_system.sh"
		done
  log_info "Program progress,init system complete!"
}

sshInit(){
	local managerIP=${NODES[0]}
	for ip in "${NODES[@]}"
		do
			remoteExecute ${CUR_DIR}/prepare/ssh_keygen.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${managerIP} 
		done
	local workdir=/home/${DEPLOY_USER}/.ssh
	cat ${workdir}/*.pub > ${workdir}/authorized_keys
	for ip in ${NODES[@]:1}
		do
			remoteExecute ${CUR_DIR}/common/delete.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} "${workdir}/authorized_keys"
			remoteExecute ${CUR_DIR}/common/sync.exp ${CUR_USER} ${workdir}/authorized_keys ${ip} ${NODES_MAP[${ip}]} ${workdir}
		done
	authorization;
	checkSSH;
	log_info "Program progress,init ssh complete!"
}

hostsInit(){
        touch ${CUR_DIR}/hosts && sed -i '1,$d' ${CUR_DIR}/hosts;
        for ip in ${NODES[@]}
                do
                        local hostName=`su ${DEPLOY_USER} -c "ssh -o 'StrictHostKeyChecking no' -t ${DEPLOY_USER}@${ip} 'hostname && exit'"`
                        if [ ! -n $hostName ];then
                                log_error "The program cannot get the hostname,ip:${ip},process exit!";
                        fi
                        local lowerHostName=`echo ${hostName}|tr A-Z a-z`
                        if [ ${hostName} != ${lowerHostName} ];then
                                echo -e "${ip} ${lowerHostName}" >> ${CUR_DIR}/hosts
                        fi
                        echo -e "${ip} ${hostName}" >> ${CUR_DIR}/hosts
                done
        for ip in "${NODES[@]:1}"
                do
                        remoteExecute ${CUR_DIR}/common/sync.exp ${CUR_USER} ${CUR_DIR}/hosts ${ip} ${NODES_MAP[${ip}]} ${CUR_DIR}
                done
        for ip in "${NODES[@]}"
                do
                        remoteExecute ${CUR_DIR}/prepare/init_hosts.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]}
                done
        log_info "Program progress,init hosts complete!"
}


prepare_for_deploy(){
  local lsb=($(getLSBName));
  local major=($(getLSBMajorVersion));
  log_info "Current environment os:${lsb},version:${major}"
  if [[ ${CHECK_OS_VERSION} == "true" ]];then
  	checkOSVersion $lsb $major;
  fi
  pre;
	syncPackage;
	baseInit;
	sshInit;
	hostsInit;
	killAll;
}



