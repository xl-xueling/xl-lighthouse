#!/bin/bash

#-----------------------------------------
# Author:XueLing.雪灵
# Email:better_xueling@126.com
#-----------------------------------------

function checkSSH(){
	log_info "start to verify SSH Key-Based authentication status."
	sleep 3;
	local managerIP=${NODES[0]};
	for ip in ${NODES[@]:1}
                do
			su ${DEPLOY_USER} -c "ssh -o 'StrictHostKeyChecking no' -t ${DEPLOY_USER}@${ip} 'pwd'"
                	if [ $? != '0' ];then
				log_error "configure SSH Key-Based authentication failed,process exit!";
				exit -1;
			fi
			expect ${CUR_DIR}/common/exec.exp ${DEPLOY_USER} ${ip} ${DEPLOY_PASSWD} "ssh -o 'StrictHostKeyChecking no' -t ${DEPLOY_USER}@${managerIP};exit"
			if [ $? != '0' ];then
                                log_error "configure SSH Key-Based authentication failed,process exit!";
                                exit -1;
                        fi
		done
	log_info "SSH Key-Based authentication status verification completed!"
}

function checkBaseInstall(){
	if [ $(yum list installed | grep 'ice-all' | wc -l) -le 0 ];then
		log_error "install zeroc-ice component failed,process exit!";
		exit -1;
	fi
}

function existByJPS(){
        local ip=$1
        local process=$2
	local ret=''
	source ~/.bashrc
	if [ $CUR_USER == $DEPLOY_USER ];then
		ret=`ssh -o 'StrictHostKeyChecking no' ${DEPLOY_USER}@$ip ${JAVA_HOME}/bin/jps -l|grep ${process}|grep -v Jps |wc -l|tr -d '\n\r'`
	else
		ret=`su - ${DEPLOY_USER} -lc "ssh -o 'StrictHostKeyChecking no' ${DEPLOY_USER}@$ip ${JAVA_HOME}/bin/jps -l|grep ${process}|grep -v Jps |wc -l|tr -d '\n\r'"`
	fi
	local code=0
        if [ "${ret}" == "0" ];then
               	code=1;
        fi
	return ${code}
}

function existByPS(){
	local ip=$1
        local process=$2
	local ret=''
	if [ ${CUR_USER} == ${DEPLOY_USER} ];then
		ret=`ssh -o 'StrictHostKeyChecking no' -t ${DEPLOY_USER}@${ip} ps -ef | grep ${process} | grep -v grep | wc -l | tr -d '\n\r'`
	else
		ret=`su - ${DEPLOY_USER} -lc "ssh -o 'StrictHostKeyChecking no' ${DEPLOY_USER}@$ip ps -ef|grep ${process}|grep -v grep |wc -l|tr -d '\n\r'"`
	fi
	local code=0
        if [ "${ret}" == "0" ];then
                code=1;
        fi
        return ${code}
}

function existByFile(){
        local ip=$1
        local file=$2
	if su ${DEPLOY_USER} -c "ssh -o 'StrictHostKeyChecking no' -t ${DEPLOY_USER}@$ip test -e ${file}" ;then
		return 0;
	else
		return 1;
	fi
}


function checkPort(){
        local ip=$1
	local port=$2
	local result=1
        while ((i <= 90))
                do
                        log_info "Waiting for verification ip:${ip} port:${port}."
                        nc -v -z -w 5 ${ip} ${port} >/dev/null 2>&1
                        if [ $? == '0' ];then
                                result=0;
                                break;
                        fi
                        ((i++))
                        sleep 1
                done
        return $result;
}

function checkPortExist(){
        local ip=$1
        local port=$2
        nc -v -z -w 5 ${ip} ${port} >/dev/null 2>&1
        return $?;
}

function version_compare() { test "$(echo "$@" | tr " " "\n" | sort -V | head -n 1)" == "$1"; }

function checkCMakeVersion(){
	local ip=$1
	local version=$(su - ${DEPLOY_USER} -lc "ssh -o 'StrictHostKeyChecking no' -t ${DEPLOY_USER}@$ip echo $(source ~/.bashrc && cmake -version 2>&1 |awk 'NR==1{gsub(/"/,"");print $3}')")
	if version_compare $version "3.0";then
		return 1;
	else
		return 0;
	fi
}

function checkCMake(){
	local IPArray=($(getServiceIPS 'redis'))
        for ip in "${IPArray[@]}"
                do
			checkCMakeVersion ${ip}
			if [ $? != '0' ];then
                                log_error "[${ip}]cmake version check failed(ip:${ip}),process exit!";
                                exit -1;
                        fi
		done
}

function checkZookeeper(){
	log_info "start to verify zookeeper startup status."
	local IPArray=($(getServiceIPS 'zookeeper'))
        for ip in "${IPArray[@]}"
                do
			checkPort ${ip} '2181'
			if [ $? != '0' ];then
                                log_error "[${ip}]port(2181) listener does not exist,zookeeper service startup failed!";
                                exit -1;
                        fi
			existByJPS ${ip} 'org.apache.zookeeper.server.quorum.QuorumPeerMain'
			if [ $? != '0' ];then
                                log_error "[${ip}]process(org.apache.zookeeper.server.quorum.QuorumPeerMain) does not exist,zookeeper service startup failed!";
                                exit -1;
                        fi
		done
	log_info "zookeeper startup status verification completed!"
}

function checkKafka(){
	log_info "start to verify kafka startup status."
	local IPArray=($(getServiceIPS 'kafka'))
        for ip in "${IPArray[@]}"
                do
                        checkPort ${ip} '9092'
                        if [ $? != '0' ];then
                                log_error "[${ip}]port(9092) listener does not exist,kafka service startup failed!";
                                exit -1;
                        fi
                        existByJPS ${ip} 'kafka.Kafka'
                        if [ $? != '0' ];then
                                log_error "[${ip}]process(kafka.Kafka) does not exist,kafka service startup failed!";
                                exit -1;
                        fi
                done
	log_info "kafka startup status verification completed!"
}

function checkHadoop(){
	log_info "start to verify hadoop startup status."
	local IPArray=($(getServiceIPS 'hadoop'))
	local index=0;
        for ip in "${IPArray[@]}"
                do

			if [ $index == '0' ];then
				checkPort ${ip} '9000'
                        	if [ $? != '0' ];then
                                	log_error "[${ip}]port(9000) listener does not exist,hadoop service startup failed!";
                                	exit -1;
                        	fi
                        	existByJPS ${ip} 'org.apache.hadoop.hdfs.server.namenode.NameNode'
                        	if [ $? != '0' ];then
                                	log_error "[${ip}]process(org.apache.hadoop.hdfs.server.namenode.NameNode) does not exist,hadoop service startup failed!";
                                	exit -1;
                        	fi
			else
				existByJPS ${ip} 'org.apache.hadoop.hdfs.server.datanode.DataNode'
                                if [ $? != '0' ];then
                                        log_error "[${ip}]process(org.apache.hadoop.hdfs.server.datanode.DataNode) does not exist,hadoop service startup failed!";
                                        exit -1;
                                fi
			fi
			((index++))
		done
	log_info "hadoop startup status verification completed!"
}

function checkHBase(){
	log_info "start to verify hbase startup status."
	local IPArray=($(getServiceIPS 'hbase'))
        local index=0;
        for ip in "${IPArray[@]}"
                do
			if [ $index == '0' ];then
				checkPort ${ip} '16000'
                                if [ $? != '0' ];then
                                        log_error "[${ip}]port(16000) listener does not exist,hbase service startup failed!";
                                        exit -1;
                                fi
                                existByJPS ${ip} 'org.apache.hadoop.hbase.master.HMaster'
                                if [ $? != '0' ];then
                                        log_error "[${ip}]process(org.apache.hadoop.hbase.master.HMaster) does not exist,hbase service startup failed!";
                                        exit -1;
                                fi
			else
				checkPort ${ip} '16020'
                                if [ $? != '0' ];then
                                        log_error "[${ip}]port(16020) listener does not exist,hbase service startup failed!";
                                        exit -1;
                                fi
                                existByJPS ${ip} 'org.apache.hadoop.hbase.regionserver.HRegionServer'
                                if [ $? != '0' ];then
                                        log_error "[${ip}]process(org.apache.hadoop.hbase.regionserver.HRegionServer) does not exist,hbase service startup failed!";
                                        exit -1;
                                fi
			fi
			((index++))
		done
	log_info "hbase startup status verification completed!"
}

function checkSpark(){
	log_info "start to verify spark startup status."
	local IPArray=($(getServiceIPS 'spark'))
        local index=0;
        for ip in "${IPArray[@]}"
                do
			if [ $index == '0' ];then
                                checkPort ${ip} '7077'
                                if [ $? != '0' ];then
                                        log_error "[${ip}]port(7077) listener does not exist,spark service startup failed!";
                                        exit -1;
                                fi
                                existByJPS ${ip} 'org.apache.spark.deploy.master.Master'
                                if [ $? != '0' ];then
                                        log_error "[${ip}]process(org.apache.spark.deploy.master.Master) does not exist,spark service startup failed!";
                                        exit -1;
                                fi
                        else
                                existByJPS ${ip} 'org.apache.spark.deploy.worker.Worker'
                                if [ $? != '0' ];then
                                        log_error "[${ip}]process(org.apache.spark.deploy.worker.Worker) does not exist,spark service startup failed!";
                                        exit -1;
                                fi
                        fi
                        ((index++))
		done
	log_info "spark startup status verification completed!"
}


function checkMysql(){
	log_info "start to verify mysql startup status."
	local IPArray=($(getServiceIPS 'mysql'))
	for ip in "${IPArray[@]}"
                do
			checkPort ${ip} '3906'
                        if [ $? != '0' ];then
                        	log_error "[${ip}]port(3906) listener does not exist,mysql service startup failed!";
                        	exit -1;
                       	fi
                        existByPS ${ip} '/bin/mysqld_safe'
                        if [ $? != '0' ];then
                        	log_error "[${ip}]process(mysql) does not exist,mysql service startup failed!";
                        	exit -1;
                        fi
		done
	log_info "mysql startup status verification completed!"
}

function checkRoaring(){
	log_info "start to verify redis-roaring plugin compile status."
	sleep 5;
        local IPArray=($(getServiceIPS 'redis'))
        local index=0;
        for ip in "${IPArray[@]}"
                do
			existByFile ${ip} "${LDP_HOME}/plugins/redis-roaring/build/libredis-roaring.so"
			if [ $? != '0' ];then
                                log_error "[${ip}]plugin(roaring) does not exist,compile roaring plugin failed!";
                                exit -1;
                        fi
		done
	log_info "compile redis-roaring plugin verification completed!"
}


function checkRedis(){
	log_info "start to verify redis startup status."
	local IPArray=($(getServiceIPS 'redis'))
        local index=0;
        for ip in "${IPArray[@]}"
                do
			for ((a=1;a<=${_REDIS_NUM_PIDS_PER_NODE};a++))
                                do
                                        local port=$[7100+${a}]
					checkPort ${ip} ${port}
                        		if [ $? != '0' ];then
                                		log_error "[${ip}]port(${port}) listener does not exist,redis service startup failed!";
                                		exit -1;
                        		fi
				done
			existByPS ${ip} '/bin/redis-server'
                        if [ $? != '0' ];then
                                log_error "[${ip}]process(redis) does not exist,redis service startup failed!";
                                exit -1;
                        fi
		done
	log_info "redis startup status verification completed!"
}

function checkLightHouseICE(){
	log_info "start to verify lighthouse-ice startup status."
	local IPArray=($(getServiceIPS 'lighthouse_ice'))
        local index=0;
        for ip in "${IPArray[@]}"
                do
			existByPS ${ip} 'icegridnode'
                       	if [ $? != '0' ];then
                        	log_error "[${ip}]process(zeroc-ice) does not exist,ice service startup failed!";
                        	exit -1;
                        fi
		done
	log_info "lighthouse-ice startup status verification completed!"
}

function checkLightHouseWeb(){
	log_info "start to verify lighthouse-web startup status."
	local IPArray=($(getServiceIPS 'lighthouse_web'))
        local index=0;
        for ip in "${IPArray[@]}"
                do
			existByJPS ${ip} 'lighthouse-web'
                       	if [ $? != '0' ];then
                        	log_error "[${ip}]process(lighthouse-web) does not exist,web service startup failed!";
                        	exit -1;
                        fi
		done
	log_info "lighthouse-web startup status verification completed!"
}

function checkLightHouseTasks(){
  local master=($(getVal 'ldp_spark_master'))
  existByJPS ${master} 'org.apache.spark.deploy.SparkSubmit'
  if [ $? != '0' ];then
     log_error "[${ip}]process(lighthouse-tasks) does not exist,tasks service startup failed!";
     exit -1;
  fi
  log_info "lighthouse-tasks startup status verification completed!"
}


function check(){
	checkSSH;
	checkBaseInstall;
	checkZookeeper;
	checkKafka;
	checkHadoop;
	checkHBase;
	checkSpark;
	checkMysql;
}


function checkZookeeperExist(){
        local IPArray=($(getServiceIPS 'zookeeper'))
        for ip in "${IPArray[@]}"
                do
			checkPortExist ${ip} '2181'
                        if [ $? == '0' ];then
                                log_error "The zookeeper(ip:${ip},port:2181) is running, please stop it and execute again!";
                                exit -1;
                        fi
                        existByJPS ${ip} 'org.apache.zookeeper.server.quorum.QuorumPeerMain'
                        if [ $? == '0' ];then
                          log_error "The zookeeper(ip:${ip}) process is running, please stop it and execute again!";
                          exit -1;
                        fi
                done
}

function checkHadoopExist(){
        local IPArray=($(getServiceIPS 'hadoop'))
        local index=0;
        for ip in "${IPArray[@]}"
                do

                        if [ $index == '0' ];then
                                checkPortExist ${ip} '9000'
                                if [ $? == '0' ];then
                                        log_error "The hadoop(ip:${ip},port:9000) is running, please stop it and execute again!";
                                        exit -1;
                                fi
                                existByJPS ${ip} 'org.apache.hadoop.hdfs.server.namenode.NameNode'
                                if [ $? == '0' ];then
                                        log_error "The hadoop(ip:${ip}) process is running, please stop it and execute again!";
                                        exit -1;
                                fi
                        else
                                existByJPS ${ip} 'org.apache.hadoop.hdfs.server.datanode.DataNode'
                                if [ $? == '0' ];then
                                        log_error "The hadoop(ip:${ip}) process is running, please stop it and execute again!";
					exit -1;
                                fi
                        fi
                        ((index++))
                done
}

function checkKafkaExist(){
	local IPArray=($(getServiceIPS 'kafka'))
        for ip in "${IPArray[@]}"
                do
                        checkPortExist ${ip} '9092'
                        if [ $? == '0' ];then
                                log_error "The kafka(ip:${ip},port:9092) is running, please stop it and execute again!";
                                exit -1;
                        fi
                        existByJPS ${ip} 'kafka.Kafka'
                        if [ $? == '0' ];then
                                log_error "The kafka(ip:${ip}) process is running, please stop it and execute again!";
                                exit -1;
                        fi
                done
}

function checkHBaseExist(){
	local IPArray=($(getServiceIPS 'hbase'))
        local index=0;
        for ip in "${IPArray[@]}"
                do
			if [ $index == '0' ];then
				checkPortExist ${ip} '16000'
                                if [ $? == '0' ];then
                                        log_error "The hbase(ip:${ip},port:16000) is running, please stop it and execute again!";
                                        exit -1;
                                fi
                                existByJPS ${ip} 'org.apache.hadoop.hbase.master.HMaster'
                                if [ $? == '0' ];then
                                        log_error "The hbase(ip:${ip}) process is running, please stop it and execute again!";
                                        exit -1;
                                fi
			else
				checkPortExist ${ip} '16020'
                                if [ $? == '0' ];then
                                        log_error "The hbase(ip:${ip},port:16020) is running, please stop it and execute again!";
                                        exit -1;
                                fi
                                existByJPS ${ip} 'org.apache.hadoop.hbase.regionserver.HRegionServer'
                                if [ $? == '0' ];then
                                        log_error "The hbase(ip:${ip}) process is running, please stop it and execute again!";
                                        exit -1;
                                fi
			fi
			((index++))
		done
}

function checkMysqlExist(){
	local IPArray=($(getServiceIPS 'mysql'))
	for ip in "${IPArray[@]}"
                do
			checkPortExist ${ip} '3906'
                        if [ $? == '0' ];then
                        	log_error "The mysql(ip:${ip},port:3906) is running, please stop it and execute again!";
                        	exit -1;
                       	fi
                        existByPS ${ip} '/bin/mysqld_safe'
                        if [ $? == '0' ];then
                        	log_error "The mysql(ip:${ip}) process is running, please stop it and execute again!";
                        	exit -1;
                        fi
		done
}

function checkRedisExist(){
	local IPArray=($(getServiceIPS 'redis'))
        local index=0;
        for ip in "${IPArray[@]}"
                do
			for ((a=1;a<=${_REDIS_NUM_PIDS_PER_NODE};a++))
                                do
                                        local port=$[7100+${a}]
					checkPortExist ${ip} ${port}
                        		if [ $? == '0' ];then
                                		log_error "The redis(ip:${ip},port:${port}) is running, please stop it and execute again!";
                                		exit -1;
                        		fi
				done
			existByPS ${ip} '/bin/redis-server'
                        if [ $? == '0' ];then
                                log_error "The redis(ip:${ip}) process is running, please stop it and execute again!";
                                exit -1;
                        fi
		done
}


function checkLightHouseICEExist(){
	local IPArray=($(getServiceIPS 'lighthouse_ice'))
        local index=0;
        for ip in "${IPArray[@]}"
                do
			existByPS ${ip} 'icegridnode'
                       	if [ $? == '0' ];then
                        	log_error "The lighthouse-ice(ip:${ip}) process is running, please stop it and execute again!";
                        	exit -1;
                        fi
		done
}

function checkLightHouseWebExist(){
	local IPArray=($(getServiceIPS 'lighthouse_web'))
        local index=0;
        for ip in "${IPArray[@]}"
                do
			existByJPS ${ip} 'lighthouse-web'
                      if [ $? == '0' ];then
                        log_error "The lighthouse-web(ip:${ip}) process is running, please stop it and execute again!";
                        exit -1;
                      fi
		done
}


function checkLightHouseTasksExist(){
  local master=($(getVal 'ldp_spark_master'))
	existByJPS ${master} 'org.apache.spark.deploy.SparkSubmit'
	if [ $? == '0' ];then
     		log_error "The lighthouse-tasks(ip:${ip}) process is running, please stop it and execute again!";
     		exit -1;
  	fi
}

function checkSparkExist(){
	local IPArray=($(getServiceIPS 'spark'))
        local index=0;
        for ip in "${IPArray[@]}"
                do
			if [ $index == '0' ];then
                                checkPortExist ${ip} '7077'
                                if [ $? == '0' ];then
                                        log_error "The spark(ip:${ip},port:${port}) is running, please stop it and execute again!";
                                        exit -1;
                                fi
                                existByJPS ${ip} 'org.apache.spark.deploy.master.Master'
                                if [ $? == '0' ];then
                                        log_error "The spark(ip:${ip}) process is running, please stop it and execute again!";
                                        exit -1;
                                fi
                        else
                                existByJPS ${ip} 'org.apache.spark.deploy.worker.Worker'
                                if [ $? == '0' ];then
                                        log_error "The spark(ip:${ip}) process is running, please stop it and execute again!";
                                        exit -1;
                                fi
                        fi
                        ((index++))
		done
}

function checkProcessExist(){
	checkZookeeperExist;
	checkHadoopExist;
	checkHBaseExist;
	checkRedisExist;
	checkMysqlExist;
	checkSparkExist;
	checkLightHouseWebExist;
	checkLightHouseICEExist;
	checkLightHouseTasksExist;
}

