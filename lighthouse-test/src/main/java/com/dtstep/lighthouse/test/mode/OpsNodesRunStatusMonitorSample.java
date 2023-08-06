package com.dtstep.lighthouse.test.mode;

import com.dtstep.lighthouse.common.key.RandomID;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class OpsNodesRunStatusMonitorSample implements SimulationModalSample<HashMap<String,Object>> {

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("idc_id", "idc_" + ThreadLocalRandom.current().nextInt(1,100));
        paramMap.put("region_id","region_" + ThreadLocalRandom.current().nextInt(1,100));
        paramMap.put("cabinet_id","cabinet_"+ThreadLocalRandom.current().nextInt(1,100));
        paramMap.put("mac", RandomID.id(10));
        paramMap.put("ip",getRandomIp());
        long memUsed = ThreadLocalRandom.current().nextInt(1,128000);
        paramMap.put("mem",ThreadLocalRandom.current().nextInt(1,10000));
        paramMap.put("mem_rate",(double)memUsed/(double) 128000);
        paramMap.put("cpu_rate",ThreadLocalRandom.current().nextDouble(0.1,0.99));
        long diskUsed = ThreadLocalRandom.current().nextInt(1000,1280000);
        paramMap.put("disk",diskUsed);
        paramMap.put("disk_rate",(double)diskUsed/(double) 1280000);
        paramMap.put("io_wait",ThreadLocalRandom.current().nextInt(100));
        paramMap.put("in_bytes",ThreadLocalRandom.current().nextInt(500));
        paramMap.put("out_bytes",ThreadLocalRandom.current().nextInt(500));
        paramMap.put("loadaverage",ThreadLocalRandom.current().nextInt(1,5));
        paramMap.put("r_ops",ThreadLocalRandom.current().nextInt(300));
        paramMap.put("w_ops",ThreadLocalRandom.current().nextInt(300));
        return paramMap;
    }


    private String getRandomIp(){
        int a = ThreadLocalRandom.current().nextInt(1,255);
        int b = ThreadLocalRandom.current().nextInt(1,255);
        int c = ThreadLocalRandom.current().nextInt(1,255);
        int d = ThreadLocalRandom.current().nextInt(1,255);
        return a + "." + b + "." + c + "." + d;
    }
}
