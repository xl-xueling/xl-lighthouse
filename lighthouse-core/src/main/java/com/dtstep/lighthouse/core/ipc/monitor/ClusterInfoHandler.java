package com.dtstep.lighthouse.core.ipc.monitor;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.common.entity.monitor.ClusterInfo;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;

public class ClusterInfoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ClusterInfo clusterInfo = new ClusterInfo();
        clusterInfo.setRunningMode(LDPConfig.getRunningMode());
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        clusterInfo.setStartTime(time);
        clusterInfo.setRunningTime(System.currentTimeMillis() - time);
        exchange.sendResponseHeaders(200, 0);
        OutputStream output = exchange.getResponseBody();
        String text = JsonUtil.toJSONString(clusterInfo);
        output.write(text.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
        exchange.close();
    }
}
