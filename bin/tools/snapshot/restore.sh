#!/bin/bash

source ~/.bashrc;
eval "$(cat ~/.bashrc|tail -n +10)"
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

restoreHBase(){
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
	if ! checkNamespaceExists "cluster_${clusterId}_ldp_hbasedb" ; then
		echo "create_namespace 'cluster_${clusterId}_ldp_hbasedb'" | $HBASE_HOME/bin/hbase shell >/dev/null 2>&1
	fi
	local lists=`echo "list_snapshots" | $HBASE_HOME/bin/hbase shell | grep -o  '\[.*\]'`
        local snapshots=$(echo ${lists} |jq .[] | jq -r values)
        for snapshot in ${snapshots[@]}
	do
		local tableName=$(echo "$snapshot" | sed 's/_snapshot$//')
		local fullTableName="cluster_${clusterId}_ldp_hbasedb:${tableName}";
		echo "Waiting for restore snapshot of table[${fullTableName}] ...";
       		`echo "disable '${fullTableName}'" | $HBASE_HOME/bin/hbase shell` >/dev/null 2>&1
		`echo "drop '${fullTableName}'" | $HBASE_HOME/bin/hbase shell` >/dev/null 2>&1
		`echo "clone_snapshot '${snapshot}','${fullTableName}'" | $HBASE_HOME/bin/hbase shell` >/dev/null 2>&1
		`echo "enable '${fullTableName}'" | $HBASE_HOME/bin/hbase shell`>/dev/null 2>&1
		echo "Restore snapshot of table[$fullTableName] success!"
	done
	echo "Restoring hbase data completed!"
}

restoreMySQL(){
	echo "Start restoring Mysql snapshot data."
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
	local dbName="cluster_${clusterId}_ldp_mysqldb";
	export MYSQL_PWD=$dbPwd;
	mysql -h $hostName -P $port -u$dbUser -e "create database if not exists ${dbName};"
	local tables=$(mysql -h $hostName -P $port -u$dbUser -e "use ${dbName};show tables;" | awk '{if(NR>1)print $0}')
	for table in $tables
	do
		echo "Drop Table:"${table}
		mysql -h $hostName -P $port -u$dbUser -e "use ${dbName};DROP TABLE ${table};"
	done
	 mysql -h $hostName -P $port -u$dbUser -e "use ${dbName};source ${sqlfile};"
	echo "Restoring mysql data completed!"
}

daemon(){
	local origin=${1}
	local dirname=$(echo "$(basename "$origin")" | cut -d. -f1)
        local clusterId=`cat ${CUR_DIR}/config/cluster.id`
        local temporaryDir=${LDP_HOME}/temp/snapshot
        mkdir -p ${temporaryDir}
        tar zxvf ${origin} -C ${temporaryDir} >/dev/null 2>&1;
        restoreMySQL ${clusterId} ${temporaryDir}/${dirname};
        restoreHBase ${clusterId} ${temporaryDir}/${dirname};
        rm -f ${LOCKFILE};	
}

main(){
 	[ -e ${LOCKFILE} ] && `cat ${LOCKFILE} | xargs --no-run-if-empty kill -9 >/dev/null 2>&1`
	trap "rm -f ${LOCKFILE}; exit" INT TERM EXIT
        echo $$ > ${LOCKFILE}
	loadScriptConfig >/dev/null 2>&1;
	if [[ ${USER} != ${DEPLOY_USER} ]];then
                echo "The operation is prohibited, only user[\"${DEPLOY_USER}\"] is allowed to execute!"
                exit -1;
        fi
	local origin=${1};
	if [ ! -f "$origin" ]; then
                echo "snapshot file:${origin} not exit,process exist!"
                exit -1;
        fi
	daemon ${origin} > ${LOG_FILE} 2>&1 &
}

main $@;
