package com.dtstep.lighthouse.core.builtin;

import com.dtstep.lighthouse.client.LightHouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class CallerStat {

    private static final Logger logger = LoggerFactory.getLogger(CallerStat.class);

    public static void stat(int callerId, String function, int status,int from, Long inBytes, Long outBytes) {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("callerId",callerId);
        paramMap.put("function",function);
        paramMap.put("status",status);
        paramMap.put("from",from);
        paramMap.put("in_bytes",inBytes);
        paramMap.put("out_bytes",outBytes);
        try{
            LightHouse.stat("_builtin_caller_stat","XjElwfhjXKB7dNzcIHsQ767fPXkTLKsB",paramMap,System.currentTimeMillis());
        }catch (Exception ex){
            logger.error("caller stat error!",ex);
        }
    }
}
