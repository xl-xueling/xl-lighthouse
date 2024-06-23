#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

source ~/.bashrc;
eval "$(awk '/^export /,0' ~/.bashrc)"
CMD_PATH=$(cd "$(dirname "$0")";pwd)
CUR_DIR=$(dirname $(dirname "$CMD_PATH"))
LOCKFILE=/tmp/lighthouse_snapshot.lock
source "${CUR_DIR}/common/lib.sh"
source "${CUR_DIR}/prepare/prepare.sh"
LOCAL_PATH=${LDP_DATA_DIR}/lighthouse/snapshot
CLUSTER_ID=`cat ${CUR_DIR}/config/cluster.id`
BATCH=`date +%Y%m%d%H%M00`;
SNAPSHOT_DIR=${LOCAL_PATH}/ldp-snapshot-${CLUSTER_ID}_${BATCH};

function snapshotHBaseTable(){
	local table_name="$1";
	local temporaryPath="$2";
	local full_table_name="cluster_${CLUSTER_ID}_ldp_warehouse:${table_name}";
	echo "Waiting for backup hbase table[${full_table_name}] ... ";
	local snapshot_name="${table_name}_snapshot"
	echo "delete_snapshot '$snapshot_name' " | $HBASE_HOME/bin/hbase shell -n >/dev/null 2>&1
	echo "snapshot '$full_table_name','$snapshot_name'" | $HBASE_HOME/bin/hbase shell -n >/dev/null 2>&1
	if [ $? != 0 ]; then
        	echo "create snapshot ["${snapshot_name}"] failed!"
	        exit -1;
	fi
  `$HBASE_HOME/bin/hbase org.apache.hadoop.hbase.snapshot.ExportSnapshot -snapshot "$snapshot_name" -copy-to "$temporaryPath" -mappers 5 -bandwidth 20 -overwrite >/dev/null 2>&1`
	echo "hbase table[${full_table_name}] backup completed!"
}

snapshotHBaseWarehouse(){
  local defaultFS=`$HADOOP_HOME/bin/hdfs getconf -confKey fs.defaultFS`
  local temporaryPath=${defaultFS}/snapshot/temporary
	local lists=`echo "list" | $HBASE_HOME/bin/hbase shell | grep -o  '\[.*\]'`
  local tables=$(echo ${lists} |jq .[] | jq -r values)
  for table in ${tables[@]};do
          echo "Get hbase table:"${table}
  done
	hadoop fs -test -e ${temporaryPath}
  	if [ $? -eq 0 ] ;then
        	hadoop fs -rm -r ${temporaryPath} >/dev/null 2>&1;
  	fi
	hadoop fs -mkdir -p ${temporaryPath}
	for table_name in $(echo "$tables" | grep "cluster_${CLUSTER_ID}_ldp_warehouse" | awk -F':' '{print $2}');
		do
      snapshotHBaseTable $table_name $temporaryPath;
    done
	local exportPath=${SNAPSHOT_DIR}/hbase;
  mkdir -p $exportPath;
  hadoop fs -get $temporaryPath/* $exportPath
}

function snapshotMySqlCMDB(){
	local hostName=($(getVal 'ldp_mysql_master'))
	local port="3906"
	local dbUser=($(getVal 'ldp_mysql_operate_user'))
	local dbPwd=($(getVal 'ldp_mysql_operate_user_passwd'))
	export MYSQL_PWD=$dbPwd;
	local databases=$(mysql -h $hostName -P $port -u$dbUser -e "SHOW DATABASES;" | grep "_ldp_cmdb")
	local dbName="cluster_${CLUSTER_ID}_ldp_cmdb"
	if  [[ ! "${databases[@]}" =~ ${dbName} ]];then
		log_error "db[${dbName}] does not exist,process exist!"
		exit -1;
	fi
  local exportPath=${SNAPSHOT_DIR}/mysql;
  mkdir -p $exportPath;
  $MYSQL_HOME/bin/mysqldump -h $hostName -P $port -u$dbUser $dbName > $exportPath/ldp_cmdb.sql
}

function snapshotMySqlWarehouse(){
  local hostName=($(getVal 'ldp_mysql_master'))
  local port="3906"
  local dbUser=($(getVal 'ldp_mysql_operate_user'))
  local dbPwd=($(getVal 'ldp_mysql_operate_user_passwd'))
  export MYSQL_PWD=$dbPwd;
  local databases=$(mysql -h $hostName -P $port -u$dbUser -e "SHOW DATABASES;" | grep "_ldp_warehouse")
  local dbName="cluster_${CLUSTER_ID}_ldp_warehouse"
  if  [[ ! "${databases[@]}" =~ ${dbName} ]];then
          log_error "db[${dbName}] does not exist,process exist!"
          exit -1;
  fi
  local exportPath=${SNAPSHOT_DIR}/mysql;
  mkdir -p $exportPath;
  $MYSQL_HOME/bin/mysqldump -h $hostName -P $port -u$dbUser $dbName > $exportPath/ldp_warehouse.sql
}

function standaloneSnapshot(){
    log_info "Start backing up mysql[ldp-cmdb] data!"
    snapshotMySqlCMDB;
    log_info "Backup mysql[ldp-cmdb] data completed!"
    log_info "Start backing up mysql[ldp-warehouse] data!"
    snapshotMySqlWarehouse;
    log_info "Backup mysql[ldp-warehouse] data completed!"
    if [[ ! -d "${SNAPSHOT_DIR}/mysql" ]] && [[ "$(isFolderEmpty "${SNAPSHOT_DIR}/mysql")" == true ]]; then
            log_error "Mysql data snapshot error, program exits!"
            exit -1;
    else
            local usage=$(getFolderUsage "${SNAPSHOT_DIR}/mysql")
            log_info "Mysql snapshot dir usage:${usage} K."
    fi
}

function clusterSnapshot(){
	log_info "Start backing up mysql[ldp-cmdb] data!"
  snapshotMySqlCMDB;
  log_info "Backup mysql[ldp-cmdb] data completed!"
  log_info "Start backing up hbase[ldp-warehouse] data!"
  snapshotHBaseWarehouse;
  log_info "Backup hbase[ldp-cmdb] data completed!"
  if [[ ! -d "${SNAPSHOT_DIR}/hbase" ]] && [[ "$(isFolderEmpty "${SNAPSHOT_DIR}/hbase")" == true ]]; then
          log_error "HBase data snapshot error, program exits!"
          exit -1;
  else
          local usage=$(getFolderUsage "${SNAPSHOT_DIR}/hbase")
          log_info "HBase snapshot dir usage:${usage} K."
  fi
  if [[ ! -d "${SNAPSHOT_DIR}/mysql" ]] && [[ "$(isFolderEmpty "${SNAPSHOT_DIR}/mysql")" == true ]]; then
          log_error "Mysql data snapshot error, program exits!"
          exit -1;
  else
          local usage=$(getFolderUsage "${SNAPSHOT_DIR}/mysql")
          log_info "Mysql snapshot dir usage:${usage} K."
  fi
}


main(){
	[ -e ${LOCKFILE} ] && `cat ${LOCKFILE} | xargs --no-run-if-empty kill -9 >/dev/null 2>&1`
	trap "rm -f ${LOCKFILE}; exit" INT TERM EXIT
	echo $$ > ${LOCKFILE}
	local start_time=$(date +%s%N)

	prepare;
	if [[ ${USER} != ${DEPLOY_USER} ]];then
                echo "The operation is prohibited, only user[\"${DEPLOY_USER}\"] is allowed to execute!"
                exit -1;
        fi
	log_info "Current Running Mode is:"${RUNNING_MODE}
	mkdir -p ${SNAPSHOT_DIR} && rm -rf ${SNAPSHOT_DIR}/*
	if [[ ${RUNNING_MODE} == "standalone" ]];then
		standaloneSnapshot $SnapshotDir;
	else
		clusterSnapshot $SnapshotDir;
	fi
  log_info "Waiting for snapshot files to be packed!"
  cd ${LOCAL_PATH};
  local dirName=ldp-snapshot-${CLUSTER_ID}_${BATCH};
  rm -rf ${dirName}.tar.gz;
  tar zcvf ${dirName}.tar.gz ${dirName} >/dev/null 2>&1;
  rm -rf ${dirName};
  local packageFile=${LOCAL_PATH}/${dirName}.tar.gz;
  if [ ! -f "${LOCAL_PATH}/${dirName}.tar.gz" ]; then
    log_info "The snapshot compressed package file is not exported normally,file:${packageFile},process exist!"
    exit -1;
  fi
  local currentIP=($(getLocalIP));
  for (( i = 0;i < "${#NODES[@]}" && i < 3; i++))
                do
      local ip=${NODES[i]};
      if [ "$ip" == "$currentIP" ]; then
              continue;
          fi
      log_info "Sync snapshot files to node:${ip}"
      remoteExecute ${CUR_DIR}/common/sync.exp ${DEPLOY_USER} ${LOCAL_PATH}/${dirName}.tar.gz ${ip} "-" ${LOCAL_PATH}
    done
  log_info "Snapshot files[${LOCAL_PATH}/${dirName}.tar.gz] export completed!"
	local end_time=$(date +%s%N)
	log_info "Program execution completed. Total cost: $(((end_time - start_time) / 1000000))ms."
}

main $@;
