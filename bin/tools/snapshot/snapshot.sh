#!/bin/bash

source ~/.bashrc;
CMD_PATH=$(cd "$(dirname "$0")";pwd)
CUR_DIR=$(dirname $(dirname "$CMD_PATH"))
LOCKFILE=/tmp/lighthouse_snapshot.lock
source "${CUR_DIR}/common/lib.sh"
source "${CUR_DIR}/prepare/prepare.sh"
DEFAULT_FS=`$HADOOP_HOME/bin/hdfs getconf -confKey fs.defaultFS`
TEMPORARY_PATH=${DEFAULT_FS}/snapshot/temporary
LOCAL_PATH=${LDP_DATA_DIR}/lighthouse/snapshot
SNAPSHOT_DIRS=()

function createSnapshot(){
	local cluster_id="$1";
	local table_name="$2";
	local full_table_name="cluster_${cluster_id}_ldp_hbasedb:${table_name}";
	local snapshot_name="${table_name}_snapshot"
	echo "delete_snapshot '$snapshot_name' " | $HBASE_HOME/bin/hbase shell -n >/dev/null 2>&1
	echo "snapshot '$full_table_name','$snapshot_name'" | $HBASE_HOME/bin/hbase shell -n >/dev/null 2>&1
	if [ $? != 0 ]; then
        	echo "create snapshot ["${snapshot_name}"] failed!"
	        exit -1;
	fi
  	`$HBASE_HOME/bin/hbase org.apache.hadoop.hbase.snapshot.ExportSnapshot -snapshot "$snapshot_name" -copy-to "$TEMPORARY_PATH" -mappers 5 -bandwidth 20 -overwrite >/dev/null 2>&1`
	echo "hbase table:${full_table_name} data backup completed!"
}


snapshotHBase(){
	local cluster_id=$1
        local batch=$2;
	local lists=`echo "list" | $HBASE_HOME/bin/hbase shell | grep -o  '\[.*\]'` 
        local tables=$(echo ${lists} |jq .[] | jq -r values)
        for table in ${tables[@]};do
                echo "Get hbase table:"${table}
        done
	hadoop fs -test -e ${TEMPORARY_PATH}
  	if [ $? -eq 0 ] ;then
        	hadoop fs -rm -r ${TEMPORARY_PATH} >/dev/null 2>&1;
  	fi
	hadoop fs -mkdir -p ${TEMPORARY_PATH}
	for table_name in $(echo "$tables" | grep "cluster_${cluster_id}" | awk -F':' '{print $2}'); 
		do
                        createSnapshot $cluster_id $table_name;
                done
	local snapshotDir="ldp-snapshot-${cluster_id}_$batch"
        local savePath=${LOCAL_PATH}/${snapshotDir}/hbase
        rm -rf $savePath && mkdir -p $savePath
        hadoop fs -get $TEMPORARY_PATH/* $savePath
}

function snapshotMySQL(){
	local cluster_id=$1;
	local batch=$2;
	local hostName=($(getVal 'ldp_mysql_master'))
	local port="3906"
	local dbUser=($(getVal 'ldp_mysql_operate_user'))
	local dbPwd=($(getVal 'ldp_mysql_operate_user_passwd'))
	export MYSQL_PWD=$dbPwd;
	local databases=$(mysql -h $hostName -P $port -u$dbUser -e "SHOW DATABASES;" | grep "_ldp_mysqldb")
	local dbName="cluster_${cluster_id}_ldp_mysqldb"
	if  [[ ! "${databases[@]}" =~ ${dbName} ]];then
		echo "db does not exist,db:${dbName},process exist!"
		exit -1;
	fi	
        local snapshotDir="ldp-snapshot-${cluster_id}_$batch";
        local savePath=${LOCAL_PATH}/${snapshotDir}/mysql
        rm -rf $savePath && mkdir -p $savePath
        $MYSQL_HOME/bin/mysqldump -h $hostName -P $port -u$dbUser $dbName > $savePath/ldp_db.sql
}

main(){
	[ -e ${LOCKFILE} ] && `cat ${LOCKFILE} | xargs --no-run-if-empty kill -9 >/dev/null 2>&1`
	trap "rm -f ${LOCKFILE}; exit" INT TERM EXIT
	echo $$ > ${LOCKFILE}
	local start_time=$(date +%s%N)
	local batch=`date +%Y%m%d%H%M00`;
	loadScriptConfig;
	if [[ ${USER} != ${DEPLOY_USER} ]];then
                echo "The operation is prohibited, only user[\"${DEPLOY_USER}\"] is allowed to execute!"
                exit -1;
        fi
	local cluster_id=`cat ${CUR_DIR}/config/cluster.id`
	echo "Start backing up mysql data...";
	snapshotMySQL ${cluster_id} $batch;
	echo "Backup mysql data completed!"
	echo "Start backing up hbase data...";
	snapshotHBase ${cluster_id} $batch;
	echo "Backup hbase data completed!"
	local dir="ldp-snapshot-${cluster_id}_$batch";
	echo "output directory:"${LOCAL_PATH}/${dir};
	if [[ ! -d "${LOCAL_PATH}/${dir}/hbase" ]] && [[ "$(isFolderEmpty "${LOCAL_PATH}/${dir}/hbase")" == true ]]; then
    				echo "hbase data snapshot error, program exits!"
				exit -1;
			else
				local usage=$(getFolderUsage "${LOCAL_PATH}/${dir}/hbase")
				echo "hbase snaptshot dir usage:${usage} K."	
			fi
			if [[ ! -d "${LOCAL_PATH}/${dir}/mysql" ]] && [[ "$(isFolderEmpty "${LOCAL_PATH}/${dir}/mysql")" == true ]]; then
                                echo "mysql data snapshot error, program exits!"
                                exit -1;
			else
				local usage=$(getFolderUsage "${LOCAL_PATH}/${dir}/mysql")
                                echo "mysql snapshot dir usage:${usage} K."
                        fi
			cd ${LOCAL_PATH};
			rm -rf ${dir}.tar.gz;
  			tar zcvf ${dir}.tar.gz ${dir} >/dev/null 2>&1;
			rm -rf ${dir};
			local currentIP=($(getLocalIP));
			for (( i = 0;i < "${#NODES[@]}" && i < 3; i++))
                		do
					local ip=${NODES[i]};
					if [ "$ip" == "$currentIP" ]; then
        					continue;
    					fi
					remoteExecute ${CUR_DIR}/common/sync.exp ${DEPLOY_USER} ${LOCAL_PATH}/ldp-snapshot-${dir}.tar.gz ${ip} "-" ${LOCAL_PATH}
				done	
			echo "snapshot[${dir}] export completed!"
	local end_time=$(date +%s%N)
	echo "Program execution completed. Total time consumed: $(((end_time - start_time) / 1000000))ms."	
}


main $@;
