#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

source "${CUR_DIR}/install/dependency.sh"

function baseInstall(){
	for ip in "${NODES[@]}"
                do
                        remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "${CUR_DIR}/install/base_install.sh ${LDP_HOME}"
                done
	log_info "Program progress,base install complete!"
}

function authorization(){
	local user=${DEPLOY_USER}
	for ip in "${NODES[@]}"
		do
			remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "chown -R ${user}:${user} ${LDP_HOME}"
			remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "chown -R ${user}:${user} /home/${user}/.ssh"
			remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "chown -R ${user}:${user} ${LDP_DATA_DIR}"
		done
	log_info "Program progress,authorization complete!"
}


function installICE() {
    local IPArray=($(getServiceIPS 'lighthouse_ice'))
    for ip in "${IPArray[@]}"
      do
        remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "${CUR_DIR}/install/install_ice.sh ${LDP_HOME}"
      done
    checkICECommand;
}

install(){
	baseInstall;
	dependencyInstall;
	pluginsInstall;
	reloadClusterConfig;
	installICE;
	authorization;
}



