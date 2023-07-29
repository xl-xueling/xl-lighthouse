#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

function remoteCompileRoaring(){
	checkCMake;
        local IPArray=($(getServiceIPS 'redis'))
	local exec_dir=${LDP_HOME}/plugins
        for ip in "${IPArray[@]}"
                do
                        remoteExecute ${CUR_DIR}/compile/compile_roaring.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME}
                done
	checkRoaring;
	log_info "Program progress,compile roaring complete!"
}

function remoteCompileRedis(){
        local IPArray=($(getServiceIPS 'redis'))
	local exec_dir=${LDP_HOME}/plugins
        for ip in "${IPArray[@]}"
                do
                        remoteExecute ${CUR_DIR}/compile/compile_redis.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} ${LDP_HOME}
		done
	log_info "Program progress,compile redis complete!"
}

function localCompileRedis() {
    local path=${1};
    cd ${path} && make;
    make test;
    make PREFIX=${path} install
}


function compile(){
  source ~/.bashrc;
	if [[ "${SERVICES[@]}" =~ "redis" ]];then
		remoteCompileRoaring;
		remoteCompileRedis;
	fi
	authorization;
}

