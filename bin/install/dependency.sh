#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

source_dir="${LDP_HOME}/temp/source"

function setComponentsEnv(){
        local service=${1}
        local ip=${2}
	      local dist_dir=${3}
        local homename=''
        if [ ${service} == 'jdk' ];then
                homename='JAVA_HOME'
        else
                homename="`echo ${service} | tr a-z A-Z`_HOME"
        fi
        remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "sed -i '/${homename}/d' /home/${DEPLOY_USER}/.bashrc"
        remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "echo export ${homename}=${dist_dir}/${service} >> /home/${DEPLOY_USER}/.bashrc"
        remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "echo PATH='$'${homename}/bin:'$'PATH >> /home/${DEPLOY_USER}/.bashrc"
        remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "sed -i '/export PATH/d' /home/${DEPLOY_USER}/.bashrc"
        remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "echo export PATH >> /home/${DEPLOY_USER}/.bashrc"
        remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "sed -i '/${homename}/d' /root/.bashrc"
        remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "echo export ${homename}=${dist_dir}/${service} >> /root/.bashrc"
        remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "echo PATH='$'${homename}/bin:'$'PATH >> /root/.bashrc"
        remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "sed -i '/export PATH/d' /root/.bashrc"
        remoteExecute ${CUR_DIR}/common/exec.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} "echo export PATH >> /root/.bashrc"
}

function syncComponents() {
        local service=${1}
        local ip=${2}
        local source_dir=${3}
        local dirname=${4}
	      local dist_dir=${5}
        remoteExecute ${CUR_DIR}/common/delete.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} ${dist_dir}/${service}
        remoteExecute ${CUR_DIR}/common/delete.exp ${CUR_USER} ${ip} ${NODES_MAP[${ip}]} ${dist_dir}/${dirname}
        remoteExecute ${CUR_DIR}/common/sync.exp ${DEPLOY_USER} ${source_dir}/${service}/${dirname} ${ip} ${DEPLOY_PASSWD} ${dist_dir}
        remoteExecute ${CUR_DIR}/common/exec.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} "ln -s ${dist_dir}/${dirname} ${dist_dir}/${service}"
  	    setComponentsEnv ${service} ${ip} ${dist_dir}
        echo "send components("${service}") to node("${ip}") success!"
}


function downloads() {
	local temp_dir=${LDP_HOME}/temp
	for service in "${SERVICES[@]}"; do
			local archive_dir=${LDP_HOME}/package/${service}
			local source_dir=${temp_dir}/source/${service}
			mkdir -p ${archive_dir}  ${source_dir}
			local archives=$(find ${archive_dir} -maxdepth 1 -mindepth 1 -type f | grep ${service})
			local archive_path=""
			if [[ ! -n "${archives}" ]]; then
				wget ${DOWNS_MAP[$service]} -P ${archive_dir}
				archive_path=$(find ${archive_dir} -maxdepth 1 -mindepth 1 -type f | grep ${service} | head -n 1)
			elif [[ ${#archives[@]} -gt 1 ]]; then
				rm -rf ${archive_dir}/*
				wget ${DOWNS_MAP[$service]} -P ${archive_dir}
				archive_path=$(find ${archive_dir} -maxdepth 1 -mindepth 1 -type f | grep ${service} | head -n 1)
			else
				archive_path=${archives[0]}
			fi
			find ${source_dir} -maxdepth 1 -mindepth 1 -type d | grep ${service} | xargs rm -rf
			local archive_filename=${archive_path##*/}
			log_info "start extracting files:${archive_filename}"
			if [ ${archive_filename##*.} == 'gz' ]; then
				local temp=${archive_filename%.*}
				if [ ${temp##*.} == 'tar' ]; then
					tar -zxvf "${archive_path}" -C ${source_dir} >/dev/null 2>&1
				fi
			elif [ ${archive_filename##*.} == 'tgz' ]; then
				tar -zxvf "${archive_path}" -C ${source_dir} >/dev/null 2>&1
			elif [ ${archive_filename##*.} == 'xz' ]; then
				local temp=${archive_filename%.*}
				if [ ${temp##*.} == 'tar' ]; then
					tar -xvf "${archive_path}" -C ${source_dir} >/dev/null 2>&1
				fi
			fi
	done
}


function dependencyInstall() {
  	downloads;
	for service in "${SERVICES[@]}"; do
		local path=$(find ${source_dir}/${service} -maxdepth 1 -mindepth 1 -type d | grep ${service} | head -n 1)
		if [ ! -n "${path}" ]; then
			echo "service (${service}) package not exit!"
			exit -1
		fi
		local dirname=${path##*/}
		local IPArray=($(getServiceIPS ${service}))
		for ip in ${IPArray[@]}; do
			syncComponents ${service} ${ip} ${source_dir} ${dirname} "${LDP_HOME}/dependency"
		done
		log_info "Program progress,dependency[$service] install complete!"
	done
	
	local webIPArray=($(getServiceIPS 'lighthouse_web'))
	for service in "mysql" "hadoop";do
		local path=$(find ${source_dir}/${service} -maxdepth 1 -mindepth 1 -type d | grep ${service} | head -n 1)
                if [ ! -n "${path}" ]; then
                        echo "service (${service}) package not exit!"
                        exit -1
                fi
		local IPArray=($(getServiceIPS ${service}))
                for ip in ${webIPArray[@]}; do
                        if [[ ! "${IPArray[@]}" =~ ${ip} ]];then
				syncComponents ${service} ${ip} ${source_dir} ${dirname} "${LDP_HOME}/proxy"
                	fi
		done
	done	


}



