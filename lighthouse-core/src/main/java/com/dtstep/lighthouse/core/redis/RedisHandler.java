package com.dtstep.lighthouse.core.redis;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.util.JedisClusterCRC16;

import java.lang.reflect.Field;
import java.util.Map;


public final class RedisHandler {

    private static final Logger logger = LoggerFactory.getLogger(RedisHandler.class);

    private static RedisClient client;

    private static JedisClusterInfoCache clusterInfoCache;

    private static final Field FIELD_CONNECTION_HANDLER;

    private static final Field FIELD_CACHE;

    static {
        FIELD_CONNECTION_HANDLER = getField(BinaryJedisCluster.class, "connectionHandler");
        FIELD_CACHE = getField(JedisClusterConnectionHandler.class, "cache");
        try{
            String redisCluster = LDPConfig.getVal(LDPConfig.KEY_REDIS_CLUSTER);
            assert redisCluster != null;
            String[] servers = redisCluster.split(",");
            client = new RedisClient();
            String password = LDPConfig.getVal(LDPConfig.KEY_REDIS_CLUSTER_PASSWORD);
            client.init(servers,password);
            JedisSlotBasedConnectionHandler connectionHandler = getValue(client.getJedisCluster(), FIELD_CONNECTION_HANDLER);
            clusterInfoCache = getValue(connectionHandler, FIELD_CACHE);
        }catch (Exception ex){
            logger.error("init redis cluster error,process exit!",ex);
            System.exit(-1);
        }
    }

    public static RedisClient getInstance(){
        return client;
    }

    public static String scriptLoad(String script){
        JedisCluster jedisCluster = client.getJedisCluster();
        Map<String, JedisPool> poolMap = jedisCluster.getClusterNodes();
        String sha = null;
        for(JedisPool jedisPool : poolMap.values()){
            sha = jedisPool.getResource().scriptLoad(script);
        }
        return sha;
    }

    public static Jedis getClient(String key){
        int slot = JedisClusterCRC16.getSlot(key);
        JedisPool pool = clusterInfoCache.getSlotPool(slot);
        return pool.getResource();
    }

    private static Field getField(Class<?> cls, String fieldName) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException | SecurityException e) {
            throw new RuntimeException("cannot find or access field '" + fieldName + "' from " + cls.getName(), e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private static <T> T getValue(Object obj, Field field) {
        try {
            return (T) field.get(obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
