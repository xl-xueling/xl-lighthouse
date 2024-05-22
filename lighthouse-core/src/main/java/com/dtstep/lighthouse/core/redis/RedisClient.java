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
import com.dtstep.lighthouse.common.enums.RunningMode;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.redis.cluster.RedisClusterOperator;
import com.dtstep.lighthouse.core.redis.standalone.RedisStandaloneOperator;
import com.google.common.base.Joiner;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisNoScriptException;
import redis.clients.jedis.resps.Tuple;

import java.util.*;


public final class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    private static final RedisClient client = new RedisClient();

    private static RedisOperator redisOperator = null;

    private static final String LIMIT_SET_LUA  =
                    "redis.call('lpush',KEYS[1],ARGV[1])\t\n" +
                    "redis.call('ltrim',KEYS[1],0,tonumber(ARGV[2]))\t\n" +
                    "redis.call('expire',KEYS[1],tonumber(ARGV[3]))\t\n" +
                    "return 1";

    private static String sha_limit_set = null;

    static {
        try{
            String redisServers = LDPConfig.getVal(LDPConfig.KEY_REDIS_CLUSTER);
            assert redisServers != null;
            String[] servers = redisServers.split(",");
            String password = LDPConfig.getVal(LDPConfig.KEY_REDIS_CLUSTER_PASSWORD);
            RunningMode runningMode = LDPConfig.getRunningMode();
            if(runningMode == RunningMode.CLUSTER){
                redisOperator = new RedisClusterOperator();
            }else if(runningMode == RunningMode.STANDALONE){
                redisOperator = new RedisStandaloneOperator();
            }
            redisOperator.init(servers,password);
        }catch (Exception ex){
            logger.error("init redis cluster error!",ex);
        }
    }

    public static RedisClient getInstance(){
        return client;
    }

    public static String scriptLoad(String script){
        return redisOperator.scriptLoad(script);
    }

    public static String scriptLoad(String script,String key){
        return redisOperator.scriptLoad(script,key);
    }

    public RedisOperator getRedisOperator() {
        return redisOperator;
    }

    public void limitSet(String key,String value,int limit,final int expireSeconds){
        try {
            if (StringUtil.isEmpty(sha_limit_set) || !redisOperator.scriptExists(sha_limit_set, key)) {
                sha_limit_set = redisOperator.scriptLoad(LIMIT_SET_LUA, key);
            }
            redisOperator.evalsha(sha_limit_set, 1, key, value, String.valueOf(limit), String.valueOf(expireSeconds));
        }catch (JedisNoScriptException ex){
            sha_limit_set = redisOperator.scriptLoad(LIMIT_SET_LUA, key);
            limitSet(key, value, limit,expireSeconds);
        }catch (Exception ex){
            logger.error("redis limit set error!",ex);
        }
    }

    public void del(String key){
        redisOperator.del(key);
    }


    private static final String LUA_PUT_TOP_N = "local function Split(fullStr, separator)\n" +
            "local startIndex = 1\n" +
            "local splitIndex = 1\n" +
            "local splitArray = {}\n" +
            "while true do\n" +
            "   local lastIndex = string.find(fullStr, separator, startIndex)\n" +
            "   if not lastIndex then\n" +
            "    splitArray[splitIndex] = string.sub(fullStr, startIndex, string.len(fullStr))\n" +
            "    break\n" +
            "   end\n" +
            "   splitArray[splitIndex] = string.sub(fullStr, startIndex, lastIndex - 1)\n" +
            "   startIndex = lastIndex + string.len(separator)\n" +
            "   splitIndex = splitIndex + 1\n" +
            "end\n" +
            "return splitArray\n" +
            "end\n" +
            "local scores = Split(ARGV[1], ',')\n" +
            "local members = Split(ARGV[2], ',')\n" +
            "local tableArray = {}\n" +
            "for i = 1, #members do\n" +
            "redis.call('zadd',KEYS[1],scores[i],members[i])\n" +
            "end\n" +
            "redis.call('zremrangeByRank',KEYS[1],0,-(tonumber(ARGV[3]) + 1))\t\n" +
            "redis.call('expire',KEYS[1],tonumber(ARGV[4]))\t\n" +
            "return 1\n";


    private static String sha_topN = null;

    public void batchPutTopN(String key, LinkedHashMap<String,String> memberMap, int limit, int expireSeconds){
        String members = Joiner.on(StatConst.MULTI_PAIR_SEPARATOR).join(memberMap.keySet());
        String scores = Joiner.on(StatConst.MULTI_PAIR_SEPARATOR).join(memberMap.values());
        try{
            if(StringUtil.isEmpty(sha_topN)){
                sha_topN = redisOperator.scriptLoad(LUA_PUT_TOP_N,key);
            }
            redisOperator.evalsha(sha_topN, 1, key, scores,members,String.valueOf(limit), String.valueOf(expireSeconds));
        }catch (JedisNoScriptException ex){
            sha_topN = redisOperator.scriptLoad(LUA_PUT_TOP_N, key);
            batchPutTopN(key,memberMap,limit,expireSeconds);
        }catch (Exception ex){
            logger.error("redis batch put error,key:{},memberMap:{},limit:{}",key, memberMap,limit,ex);
        }
    }

    private static final String LUA_PUT_LAST_N = "local function Split(fullStr, separator)\n" +
            "local startIndex = 1\n" +
            "local splitIndex = 1\n" +
            "local splitArray = {}\n" +
            "while true do\n" +
            "   local lastIndex = string.find(fullStr, separator, startIndex)\n" +
            "   if not lastIndex then\n" +
            "    splitArray[splitIndex] = string.sub(fullStr, startIndex, string.len(fullStr))\n" +
            "    break\n" +
            "   end\n" +
            "   splitArray[splitIndex] = string.sub(fullStr, startIndex, lastIndex - 1)\n" +
            "   startIndex = lastIndex + string.len(separator)\n" +
            "   splitIndex = splitIndex + 1\n" +
            "end\n" +
            "return splitArray\n" +
            "end\n" +
            "local scores = Split(ARGV[1], ',')\n" +
            "local members = Split(ARGV[2], ',')\n" +
            "local tableArray = {}\n" +
            "for i = 1, #members do\n" +
            "redis.call('zadd',KEYS[1],scores[i],members[i])\n" +
            "end\n" +
            "redis.call('zremrangeByRank',KEYS[1],tonumber(ARGV[3]),-1)\t\n" +
            "redis.call('expire',KEYS[1],tonumber(ARGV[4]))\t\n" +
            "return 1\n";

    private static String sha_lastN = null;

    public void batchPutLastN(String key, LinkedHashMap<String,String> memberMap, int limit, int expireSeconds){
        String members = Joiner.on(StatConst.MULTI_PAIR_SEPARATOR).join(memberMap.keySet());
        String scores = Joiner.on(StatConst.MULTI_PAIR_SEPARATOR).join(memberMap.values());
        try{
            if(StringUtil.isEmpty(sha_lastN)){
                sha_lastN = redisOperator.scriptLoad(LUA_PUT_LAST_N,key);
            }
            redisOperator.evalsha(sha_lastN, 1, key, scores,members,String.valueOf(limit), String.valueOf(expireSeconds));
        }catch (JedisNoScriptException ex){
            sha_lastN = redisOperator.scriptLoad(LUA_PUT_LAST_N, key);
            batchPutLastN(key,memberMap,limit,expireSeconds);
        }catch (Exception ex){
            logger.error("redis batch put error,key:{},memberMap:{},limit:{}",key, memberMap,limit,ex);
        }
    }

    public List<Tuple> zrevrange(String key, int start, int end){
        return redisOperator.zrevrangeWithScores(key, start, end);
    }

    public List<Tuple> zrange(String key, int start, int end){
        return redisOperator.zrangeWithScores(key, start, end);
    }

    public List<String> lrange(String key,int start,int end){
        return redisOperator.lrange(key, start, end);
    }

    public void set(final String key,String value,final int expireSeconds) {
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(value)){
            return;
        }
        redisOperator.setex(key, expireSeconds, value);
    }

    public void setBytes(final String key,byte[] value,final int expireSeconds) {
        if(StringUtil.isEmpty(key) || value == null){
            return;
        }
        redisOperator.setex(key.getBytes(),expireSeconds,value);
    }

    public byte[] getBytes(final String key) {
        if(StringUtil.isEmpty(key)){
            return null;
        }
        return redisOperator.get(key.getBytes());
    }

    public String get(final String key) {
        return redisOperator.get(key);
    }

    public boolean exists(final String key) {
        return redisOperator.exists(key);
    }

    public void expire(final String key,int second) throws Exception{
        redisOperator.expire(key,second);
    }

    public void incrBy(final String key,int step) throws Exception{
        redisOperator.incrBy(key,step);
    }
}
