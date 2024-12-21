#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

source "${CUR_DIR}/prepare/basic.sh"
source "${CUR_DIR}/compile/compile.sh"


pre(){
	local packageManager=($(getPackageManager));
  if [[ $packageManager == "yum" ]];then
    sudo yum install -y epel-release ${YUM_OPTS}
  elif [[ $packageManager == "apt-get" ]] ;then
    sudo apt-get install -y software-properties-common
  fi
  export PS1="[\u@\h \W]\$" && batch_install expect jq rsync*;
	for ip in "${NODES[@]}"
                do
			remoteExecute ${CUR_DIR}/common/verify.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]}
		done
	log_info "Program progress,verify cluster nodes passwd complete!"

  cat ${CUR_DIR}/config/nodelist | awk -F ';' '{print $1}' > ${CUR_DIR}/config/nodelist.new
  loadNodesConfig;
  loadScriptConfig;
  log_info "Program progress,load script config complete!"
	for ip in "${NODES[@]}"
		do
		remoteExecute ${CUR_DIR}/prepare/init_pre.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} ${DEPLOY_USER} ${DEPLOY_PASSWD};
		done
	log_info "Program progress,init pre complete!"
	if [ ${NET_MODE} == "offline" ];then
    for ip in "${NODES[@]:1}"
      do
        remoteExecute ${CUR_DIR}/common/sync.exp ${CUR_USER} ${LDP_HOME}/package ${ip} ${NODES_MAP[${ip}]} ${LDP_HOME}
        if [[ $packageManager == "yum" ]];then
          remoteExecute ${CUR_DIR}/common/sync.exp ${CUR_USER} /etc/yum.repos.d/xl-lighthouse.repo ${ip} ${NODES_MAP[${ip}]} /etc/yum.repos.d/
        elif [[ $packageManager == "apt-get" ]] ;then
          remoteExecute ${CUR_DIR}/common/sync.exp ${CUR_USER} /etc/apt/sources.list.d/xl-lighthouse.list ${ip} ${NODES_MAP[${ip}]} /etc/apt/sources.list.d/
          remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "apt-get update"
        fi
      done
  else
    for ip in "${NODES[@]:1}"
      do
        if [[ $packageManager == "yum" ]];then
          remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "rm -f /etc/yum.repos.d/xl-lighthouse.repo"
        elif [[ $packageManager == "apt-get" ]] ;then
          remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "rm -f /etc/apt/sources.list.d/xl-lighthouse.list"
        fi
      done
  fi
	for ip in "${NODES[@]}"
      do
        local packageManager=($(getPackageManager));
        if [[ $packageManager == "yum" ]];then
          remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "ps -ef | grep -E 'dnf|yum' | grep -v grep | awk '{print \$2}' | xargs -r kill -9"
          remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "sudo rm -f /var/run/yum.pid"
          remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "sudo rm -f /var/lib/rpm/.rpm.lock"
          remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "sudo rm -f /var/lib/rpm/__db*"
			    remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[$ip]} "sudo yum -y install rsync ${YUM_OPTS}"
        elif [[ $packageManager == "apt-get" ]] ;then
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
        touch ${CUR_DIR}/hosts;
        sed -i '1,$d' ${CUR_DIR}/hosts;
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
                        local upperHostName=`echo ${hostName}|tr a-z A-Z`
                        if [ ${hostName} != ${upperHostName} ];then
                                echo -e "${ip} ${upperHostName}" >> ${CUR_DIR}/hosts
                        fi
                        echo -e "${ip} ${hostName}" >> ${CUR_DIR}/hosts
                done
        for ip in "${NODES[@]:1}"
                do
                        remoteExecute ${CUR_DIR}/common/sync.exp ${CUR_USER} ${CUR_DIR}/hosts ${ip} ${NODES_MAP[${ip}]} ${CUR_DIR}
                done
        for ip in "${NODES[@]}"
                do
                        remoteExecute ${CUR_DIR}/prepare/init_hosts.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} ${LDP_HOME}
                done
        log_info "Program progress,init hosts complete!"
}


createLocalRepo() {
   if [ ${NET_MODE} == "offline" ]; then
       local baselibdir="${LDP_HOME}/package/baselib"
       local packageManager=($(getPackageManager))
       if [[ $packageManager == "yum" ]]; then
           REPO_FILE="/etc/yum.repos.d/xl-lighthouse.repo"
           cat > "$REPO_FILE" <<EOL
[xl-lighthouse-repo]
name=Local Repository
baseurl=file://$baselibdir
enabled=1
gpgcheck=0
EOL
       elif [[ $packageManager == "apt-get" ]]; then
           REPO_FILE="/etc/apt/sources.list.d/xl-lighthouse.list"
           cat > "$REPO_FILE" <<EOL
deb [trusted=yes lang=none acquire::CompressionTypes::Order::=gz] file://$baselibdir ./
EOL
           apt-get update
       fi
   else
       if [[ $packageManager == "yum" ]]; then
           [ -f "/etc/yum.repos.d/xl-lighthouse.repo" ] && rm -f "/etc/yum.repos.d/xl-lighthouse.repo"
       elif [[ $packageManager == "apt-get" ]]; then
           [ -f "/etc/apt/sources.list.d/xl-lighthouse.list" ] && rm -f "/etc/apt/sources.list.d/xl-lighthouse.list"
       fi
   fi
}

prepare_for_deploy(){
  local lsb=($(getLSBName));
  local major=($(getLSBMajorVersion));
  log_info "Current environment os:${lsb},version:${major}"
  createLocalRepo;
  pre;
  syncPackage;
  baseInit;
  sshInit;
  hostsInit;
  killAll;
}

prepare(){
  loadNodesConfig;
  loadScriptConfig;
}



