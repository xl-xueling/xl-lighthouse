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
HADOOP_HOME=${LDP_HOME}/dependency/hadoop

restoreMySQLCMDB(){
	log_info "Start restoring Mysql[ldp-cmdb] data."
	local clusterId=${1};
	local sqlfile="${2}/mysql/ldp_db.sql";
	echo "Cluster is:${clusterId},snapshot file:${sqlfile}"
	if [ ! -f "$sqlfile" ]; then
    		echo "snapshot file:${sqlfile} not exit,process exist!"
		exit -1;
	fi
	local hostName=($(getVal 'ldp_mysql_master'))
	local port="3906"
	local dbUser=($(getVal 'ldp_mysql_operate_user'))
	local dbPwd=($(getVal 'ldp_mysql_operate_user_passwd'))
	local dbName="cluster_${clusterId}_ldp_cmdb";
	export MYSQL_PWD=$dbPwd;
	mysql -h $hostName -P $port -u$dbUser -e "create database if not exists ${dbName};"
	local tables=$(mysql -h $hostName -P $port -u$dbUser -e "use ${dbName};show tables;" | awk '{if(NR>1)print $0}')
	for table in $tables
	do
		echo "Drop Table:"${table}
		mysql -h $hostName -P $port -u$dbUser -e "use ${dbName};DROP TABLE ${table};"
	done
	echo "start to export..."
	mysql -h $hostName -P $port -u$dbUser -e "use ${dbName};source ${sqlfile};"
	log_info "Restoring mysql[ldp-cmdb] data completed!"
}

checkNamespaceExists() {
    local namespace=${1};
    echo "list_namespace" | $HBASE_HOME/bin/hbase shell  | grep "^$namespace$"
    return $?
}


restoreHBaseWarehouse(){
	echo "Start restoring HBase snapshot data."
	local clusterId=${1};
	local snapshotDir=${2}/hbase;
	if [ ! -d "$snapshotDir" ]; then
                echo "snapshot dir:${snapshotDir} not exit,process exist!"
                exit -1;
        fi
	sudo -u work ${HADOOP_HOME}/bin/hadoop fs -mkdir -p /hbase/archive/data
	sudo -u work ${HADOOP_HOME}/bin/hadoop fs -rm -r -f /hbase/archive/data/*
	sudo -u work ${HADOOP_HOME}/bin/hadoop fs -rm -r -f /hbase/.hbase-snapshot/*
	sudo -u work ${HADOOP_HOME}/bin/hadoop fs -put -f ${snapshotDir}/archive/data/* /hbase/archive/data/
	sudo -u work ${HADOOP_HOME}/bin/hadoop fs -put -f ${snapshotDir}/.hbase-snapshot /hbase/
	if ! checkNamespaceExists "cluster_${clusterId}_ldp_warehouse" ; then
		echo "create_namespace 'cluster_${clusterId}_ldp_warehouse'" | $HBASE_HOME/bin/hbase shell >/dev/null 2>&1
	fi
	local lists=`echo "list_snapshots" | $HBASE_HOME/bin/hbase shell | grep -o  '\[.*\]'`
        local snapshots=$(echo ${lists} |jq .[] | jq -r values)
        for snapshot in ${snapshots[@]}
	do
		local tableName=$(echo "$snapshot" | sed 's/_snapshot$//')
		local fullTableName="cluster_${clusterId}_ldp_warehouse:${tableName}";
		echo "Waiting for restore snapshot of table[${fullTableName}] ...";
       		`echo "disable '${fullTableName}'" | $HBASE_HOME/bin/hbase shell` >/dev/null 2>&1
		`echo "drop '${fullTableName}'" | $HBASE_HOME/bin/hbase shell` >/dev/null 2>&1
		`echo "clone_snapshot '${snapshot}','${fullTableName}'" | $HBASE_HOME/bin/hbase shell` >/dev/null 2>&1
		`echo "enable '${fullTableName}'" | $HBASE_HOME/bin/hbase shell`>/dev/null 2>&1
		echo "Restore snapshot of table[$fullTableName] success!"
	done
	echo "Restoring hbase data completed!"
}

main(){
  if [ ${USER} != "root" ];then
       log_error "The operation is prohibited, only the \"root\" user is allowed to execute!"
       exit -1;
  fi
	if [ -f "${LDP_HOME}/bin/config/cluster.mode" ]; then
		cp -f ${LDP_HOME}/bin/config/cluster.mode ${LDP_HOME}/bin/config/running.mode
	fi
	if [ -f "${LDP_HOME}/bin/config/config.json" ]; then
		cp -f ${LDP_HOME}/bin/config/config.json ${LDP_HOME}/bin/config/cluster-config.json
	fi
	if [ -f "${LDP_HOME}/bin/config/deploy.json" ]; then
		cp -f ${LDP_HOME}/bin/config/deploy.json ${LDP_HOME}/bin/config/cluster-deploy.json
	fi
	if [ -d "$LDP_HOME" ]; then
                cp -rf $LDP_HOME/bin/config ${UPGRADE_HOME}/bin
        fi
	prepare;
	local origin=${1};
	if [ -z "$origin" ]; then
    log_error "Please set the snapshot file path!";
    exit -1;
  fi
  if [ ! -f "$origin" ]; then
    log_error "snapshot file:$origin not exit,process exist!"
		exit -1;
	fi
	local dirname=$(echo "$(basename "$origin")" | cut -d. -f1)
  local clusterId=`cat ${LDP_HOME}/bin/config/cluster.id`
	local temporaryDir=${LDP_HOME}/temp/snapshot;
	mkdir -p ${temporaryDir} && rm -rf ${temporaryDir}/*
  tar zxvf ${origin} -C ${temporaryDir} >/dev/null 2>&1;
	restoreMySQLCMDB ${clusterId} ${temporaryDir}/${dirname};
	restoreHBaseWarehouse ${clusterId} ${temporaryDir}/${dirname};

  sudo -u work ${LDP_HOME}/bin/stop-all.sh
	sh ${UPGRADE_HOME}/upgrade.sh
}

main $@
