#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

db_upgrade(){
  local userPasswd=($(getUserPassword ${DEPLOY_USER}));
	local IPArray=($(getServiceIPS 'mysql'))
	local rootPwd=($(getVal 'ldp_mysql_root_passwd'))
	local operateUser=($(getVal 'ldp_mysql_operate_user'))
	local operateUserPwd=($(getVal 'ldp_mysql_operate_user_passwd'))
  local home=${LDP_HOME}/dependency/mysql
  local sqlPath=${LDP_HOME}/bin/upgrade/ldp_upgrade.sql
  for ip in "${IPArray[@]}"
		do
			remoteExecute ${CUR_DIR}/common/exec_sql.exp ${DEPLOY_USER} ${ip} ${userPasswd} ${LDP_HOME} ${rootPwd} ${sqlPath}
		done
	log_info "Upgrade cmdb-database complete!"
}