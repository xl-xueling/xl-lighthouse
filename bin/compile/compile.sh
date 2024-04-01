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

function localCompileNginx() {
  local path=${1};
  cd ${path};
  ./configure --prefix=${path} --with-http_ssl_module --conf-path=${path}/conf.bak/nginx.conf
  make
  make install
}

function localCompileWebapps(){
  local lsb=($(getLSBName));
  if [[ $lsb == "CentOS" ]] || [[ $lsb == "Rocky" ]] || [[ $lsb == "Alma" ]] || [[ $lsb == "RHEL" ]];then
    curl -sL https://rpm.nodesource.com/setup_18.x | sudo bash -
    sudo yum install -y nodejs
  elif [[ $lsb == "Debian" ]] || [[ $lsb == "Ubuntu" ]] ;then
     curl -sL https://deb.nodesource.com/setup_18.x | sudo -E bash -
     sudo apt-get install -y nodejs
  fi
  npm install -g yarn;
  local yarnCmd=`which yarn`;
  local yarnCmdDir=$(dirname "$yarnCmd")
  ln -s $yarnCmd ${yarnCmdDir}/yarnpkg
  local path=${1};
  cd ${path};
	rm -rf yarn.lock package-lock.json
	yarnpkg config set network-timeout 600000 -g
	yarnpkg config set strict-ssl false
	yarnpkg config set registry https://registry.npmjs.org/
	yarnpkg install;
	yarnpkg install;
	yarnpkg install;
	yarnpkg run build;
	if [ ! -d "${path}/build/static" ]; then
      yarnpkg config set registry https://registry.npm.taobao.org/
      yarnpkg install;
	    yarnpkg run build;
  fi
  if [ ! -d "${path}/build/static" ]; then
      echo "Compile lighthouse-insights failed,process exit!"
      exit -1;
  fi
}

function compile(){
  source ~/.bashrc;
	if [[ "${SERVICES[@]}" =~ "redis" ]];then
		remoteCompileRoaring;
		remoteCompileRedis;
	fi
	authorization;
}

