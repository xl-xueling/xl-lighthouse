package com.dtstep.lighthouse.core.redis;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

public final class RedisHandler {

    private static final Logger logger = LoggerFactory.getLogger(RedisHandler.class);

    private static RedisClient client;

    static {
        try{
            String redisCluster = LDPConfig.getVal(LDPConfig.KEY_REDIS_CLUSTER);
            assert redisCluster != null;
            String[] servers = redisCluster.split(",");
            client = new RedisClient();
            String password = LDPConfig.getVal(LDPConfig.KEY_REDIS_CLUSTER_PASSWORD);
            client.init(servers,password);
        }catch (Exception ex){
            logger.error("init redis cluster error!",ex);
        }
    }

    public static RedisClient getInstance(){
        return client;
    }

    public static String scriptLoad(String script){
        JedisCluster jedisCluster = client.getJedisCluster();
        return jedisCluster.scriptLoad(script);
    }


}
