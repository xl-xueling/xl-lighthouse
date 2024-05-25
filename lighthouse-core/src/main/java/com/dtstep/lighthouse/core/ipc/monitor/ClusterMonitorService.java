package com.dtstep.lighthouse.core.ipc.monitor;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClusterMonitorService {

    private static final Logger logger = LoggerFactory.getLogger(ClusterMonitorService.class);

    public static void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(SysConst.CLUSTER_MONITOR_SERVICE_PORT), 0);
        ExecutorService exec = Executors.newFixedThreadPool(2);
        server.setExecutor(exec);
        server.createContext("/clusterInfo", new ClusterInfoHandler());
        server.start();
        logger.info("ldp monitor service start,listen:{}",SysConst.CLUSTER_MONITOR_SERVICE_PORT);
    }
}
