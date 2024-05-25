package com.dtstep.lighthouse.core.ipc.monitor;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.util.IpUtils;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClusterMonitorService {

    private static final Logger logger = LoggerFactory.getLogger(ClusterMonitorService.class);

    public static void start() throws Exception {
        boolean isPortUsed = IpUtils.isPortUsing(SysConst.CLUSTER_MONITOR_SERVICE_PORT);
        if(isPortUsed){
            logger.info("ldp monitor service not start,port:{} already used!",SysConst.CLUSTER_MONITOR_SERVICE_PORT);
        }else{
            HttpServer server = HttpServer.create(new InetSocketAddress(SysConst.CLUSTER_MONITOR_SERVICE_PORT), 0);
            ExecutorService exec = Executors.newFixedThreadPool(2);
            server.setExecutor(exec);
            server.createContext("/clusterInfo", new ClusterInfoHandler());
            server.start();
            logger.info("ldp monitor service start,listen:{}",SysConst.CLUSTER_MONITOR_SERVICE_PORT);
        }
    }
}
