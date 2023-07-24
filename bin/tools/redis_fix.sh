#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

CUR_USER=${USER}
CUR_DIR=${LDP_HOME}/bin
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
  loadScriptConfig;
  redisClusterFix;
  log_info "Fix Redis Complete!";
}

main $@;
