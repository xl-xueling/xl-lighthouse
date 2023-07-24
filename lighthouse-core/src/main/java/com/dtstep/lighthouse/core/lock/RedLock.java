package com.dtstep.lighthouse.core.lock;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.dtstep.lighthouse.core.config.LDPConfig;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public final class RedLock {

    private static final Logger logger = LoggerFactory.getLogger(RedLock.class);

    private static RedissonClient redissonClient;

    static {
        init();
    }

    private synchronized static void init(){
        Config config = new Config();
        try{
            String redisCluster = LDPConfig.getVal(LDPConfig.KEY_REDIS_CLUSTER);
            String password = LDPConfig.getVal(LDPConfig.KEY_REDIS_CLUSTER_PASSWORD);
            assert redisCluster != null;
            String[] servers = redisCluster.split(",");
            List<String> nodeAddress = new ArrayList<>();
            for (String server : servers) {
                nodeAddress.add("redis://" + server);
            }
            config.useClusterServers().setPassword(password).setScanInterval(5000)
                    .setIdleConnectionTimeout(480000)
                    .setConnectTimeout(480000)
                    .setRetryAttempts(6)
                    .setRetryInterval(6000)
                    .setKeepAlive(false)
                    .setMasterConnectionPoolSize(64)
                    .setCheckSlotsCoverage(false)
                    .setTimeout(480000)
                    .setNodeAddresses(nodeAddress);
            redissonClient = Redisson.create(config);
        }catch (Exception ex){
            logger.error("init redlock error,process exit!",ex);
            System.exit(-1);
        }
    }


    public static boolean tryLock(String key,long waitTime,long leastTime,TimeUnit timeUnit){
        try{
            RLock rLock = redissonClient.getLock(key);
            if (Objects.isNull(rLock)) {
                logger.error("No redisson lock operation instance was obtained,rLock:{},key:{}",rLock,key);
                return false;
            }
            return rLock.tryLock(waitTime,leastTime,timeUnit);
        }catch (Exception ex){
            logger.error("redisson try lock failed,key:{}!",key,ex);
            return false;
        }
    }

    public static void unLock(String key) throws Exception{
        RLock rLock = redissonClient.getLock(key);
        if(rLock.isLocked() && rLock.isHeldByCurrentThread()){
            rLock.unlock();;
        }
    }
}
