#!/bin/bash

source ~/.bashrc;
eval "$(awk '/^export /,0' ~/.bashrc)"
CMD_PATH=$(cd "$(dirname "$0")";pwd)
CUR_DIR=$(dirname $(dirname "$CMD_PATH"))
source "${CUR_DIR}/common/lib.sh"
source "${CUR_DIR}/prepare/prepare.sh"
LOCKFILE=/tmp/lighthouse_restore.lock
LOG_FILE="${CUR_DIR}/log/restore.log"

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
	hadoop fs -mkdir -p /hbase/archive/data
	hadoop fs -rm -r -f /hbase/archive/data/*
	hadoop fs -rm -r -f /hbase/.hbase-snapshot/*
	hadoop fs -put -f ${snapshotDir}/archive/data/* /hbase/archive/data/
	hadoop fs -put -f ${snapshotDir}/.hbase-snapshot /hbase/
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

restoreMySQLCMDB(){
	log_info "Start restoring Mysql[ldp-cmdb] data."
	local clusterId=${1};
	local sqlfile="${2}/mysql/ldp_cmdb.sql";
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
	 mysql -h $hostName -P $port -u$dbUser -e "use ${dbName};source ${sqlfile};"
	log_info "Restoring mysql[ldp-cmdb] data completed!"
}

restoreMySQLWarehouse(){
	log_info "Start restoring Mysql[ldp-warehouse] data."
	local clusterId=${1};
	local sqlfile="${2}/mysql/ldp_warehouse.sql";
	echo "Cluster is:${clusterId},snapshot file:${sqlfile}"
	if [ ! -f "$sqlfile" ]; then
    		echo "snapshot file:${sqlfile} not exit,process exist!"
		exit -1;
	fi
	local hostName=($(getVal 'ldp_mysql_master'))
	local port="3906"
	local dbUser=($(getVal 'ldp_mysql_operate_user'))
	local dbPwd=($(getVal 'ldp_mysql_operate_user_passwd'))
	local dbName="cluster_${clusterId}_ldp_warehouse";
	export MYSQL_PWD=$dbPwd;
	mysql -h $hostName -P $port -u$dbUser -e "create database if not exists ${dbName};"
	local tables=$(mysql -h $hostName -P $port -u$dbUser -e "use ${dbName};show tables;" | awk '{if(NR>1)print $0}')
	for table in $tables
	do
		echo "Drop Table:"${table}
		mysql -h $hostName -P $port -u$dbUser -e "use ${dbName};DROP TABLE ${table};"
	done
	 mysql -h $hostName -P $port -u$dbUser -e "use ${dbName};source ${sqlfile};"
	log_info "Restoring mysql[ldp-warehouse] data completed!"
}

daemon(){
	trap "rm -f ${LOCKFILE}; exit" INT TERM EXIT
  echo $$ > ${LOCKFILE}
	local origin=${1}
	local dirname=$(echo "$(basename "$origin")" | cut -d. -f1)
  local clusterId=`cat ${CUR_DIR}/config/cluster.id`
  local temporaryDir=${LDP_HOME}/temp/snapshot
  mkdir -p ${temporaryDir}
  tar zxvf ${origin} -C ${temporaryDir} >/dev/null 2>&1;
  if [[ ${RUNNING_MODE} == "standalone" ]];then
		restoreMySQLCMDB ${clusterId} ${temporaryDir}/${dirname};
    restoreMySQLWarehouse ${clusterId} ${temporaryDir}/${dirname};
	else
		restoreMySQLCMDB ${clusterId} ${temporaryDir}/${dirname};
    restoreHBaseWarehouse ${clusterId} ${temporaryDir}/${dirname};
	fi
  rm -f ${LOCKFILE};
	echo "Restoring completed,Service will be restarted."
  ${LDP_HOME}/bin/restart-all.sh;

}

main(){
 	[ -e ${LOCKFILE} ] && `cat ${LOCKFILE} | xargs --no-run-if-empty kill -9 >/dev/null 2>&1`
	prepare >/dev/null 2>&1;
	if [[ ${USER} != ${DEPLOY_USER} ]];then
    log_error "The operation is prohibited, only user[\"${DEPLOY_USER}\"] is allowed to execute!"
    exit -1;
  fi
	local origin=${1};
	if [ ! -f "$origin" ]; then
    log_error "Snapshot file:${origin} does not exist,process exit!"
    exit -1;
  fi
  rm -f ${LOG_FILE}
  daemon ${origin} > ${LOG_FILE} 2>&1 &
  log_info "The restore tasks has been started, and the log is being output to the [${LOG_FILE}] file."
}

main $@;
