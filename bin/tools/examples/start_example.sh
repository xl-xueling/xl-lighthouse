source ~/.bashrc;
eval "$(awk '/^export /,0' ~/.bashrc)"

LOCKFILE=/tmp/lighthouse_example.lock
CMD_PATH=$(cd "$(dirname "$0")";pwd)
CUR_DIR=$(dirname $(dirname "$CMD_PATH"))
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
LOG_FILE="${CUR_DIR}/log/example.log"

main(){
	[ -e ${LOCKFILE} ] && `cat ${LOCKFILE} | xargs --no-run-if-empty kill -9 >/dev/null 2>&1`;
    	trap "rm -f ${LOCKFILE}; exit" INT TERM EXIT
	echo $$ > ${LOCKFILE}
	prepare;
	if [[ ${USER} != ${DEPLOY_USER} ]];then
        	log_error "The operation is prohibited, only user[\"${DEPLOY_USER}\"] is allowed to execute!"
        	exit -1;
  	fi
	if jps -l | grep -q 'LDPFlowTestInstance'; then
		log_error "The statistical example task is already running, please stop it first!"
		exit -1;
	fi	
	sleep 5;
	java -Xmx300m -Xms300m -cp ${LDP_HOME}/lib/*:${LDP_HOME}/lib/lighthouse-test-*.jar com.dtstep.lighthouse.test.example.StartExample
	if [ $? != '0' ];then
		log_error "Create statistic examples failed,process exit!"
		exit -1;
	fi
	log_info "Waiting for the statistics task to start..."
	sleep 5;
	nohup java -Xmx300m -Xms300m -cp ${LDP_HOME}/lib/*:${LDP_HOME}/lib/lighthouse-test-*.jar com.dtstep.lighthouse.test.LDPFlowTestInstance 100 > ${LOG_FILE} 2>&1 &
	log_info "The statistical example task has been started, and the log is being output to the file[${LOG_FILE}]."
	rm -f ${LOCKFILE}
}

main $@;
