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

import java.util.*;


public final class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    private static JedisCluster jedisCluster = null;

    public void init(String[] servers) {
        init(servers, null);
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    private static final String LIMIT_SET_LUA  =
                    "redis.call('lpush',KEYS[1],ARGV[1])\t\n" +
                    "redis.call('ltrim',KEYS[1],0,tonumber(ARGV[2]))\t\n" +
                    "redis.call('expire',KEYS[1],tonumber(ARGV[3]))\t\n" +
                    "return 1";

    private static String sha_limit_set = null;

    public void limitSet(String key,String value,int limit,final int expireSeconds){
        final JedisCluster jedisCluster = RedisHandler.getInstance().getJedisCluster();
        try {
            if (StringUtil.isEmpty(sha_limit_set) || !jedisCluster.scriptExists(sha_limit_set, key)) {
                sha_limit_set = jedisCluster.scriptLoad(LIMIT_SET_LUA, key);
            }
            jedisCluster.evalsha(sha_limit_set, 1, key, value, String.valueOf(limit), String.valueOf(expireSeconds));
        }catch (JedisNoScriptException ex){
            sha_limit_set = jedisCluster.scriptLoad(LIMIT_SET_LUA, key);
            limitSet(key, value, limit,expireSeconds);
        }catch (Exception ex){
            logger.error("redis limit set error!",ex);
        }
    }

    public void del(String key){
        jedisCluster.del(key);
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

    public void batchPutTopN(String key, LinkedHashMap<String,Double> memberMap, int limit, int expireSeconds){
        String members = Joiner.on(StatConst.MULTI_PAIR_SEPARATOR).join(memberMap.keySet());
        String scores = Joiner.on(StatConst.MULTI_PAIR_SEPARATOR).join(memberMap.values());
        final JedisCluster jedisCluster = RedisHandler.getInstance().getJedisCluster();
        try{
            if(StringUtil.isEmpty(sha_topN)){
                sha_topN = jedisCluster.scriptLoad(LUA_PUT_TOP_N,key);
            }
            jedisCluster.evalsha(sha_topN, 1, key, scores,members,String.valueOf(limit), String.valueOf(expireSeconds));
        }catch (JedisNoScriptException ex){
            sha_topN = jedisCluster.scriptLoad(LUA_PUT_TOP_N, key);
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

    public void batchPutLastN(String key, LinkedHashMap<String,Double> memberMap, int limit, int expireSeconds){
        String members = Joiner.on(StatConst.MULTI_PAIR_SEPARATOR).join(memberMap.keySet());
        String scores = Joiner.on(StatConst.MULTI_PAIR_SEPARATOR).join(memberMap.values());
        final JedisCluster jedisCluster = RedisHandler.getInstance().getJedisCluster();
        try{
            if(StringUtil.isEmpty(sha_lastN)){
                sha_lastN = jedisCluster.scriptLoad(LUA_PUT_LAST_N,key);
            }
            jedisCluster.evalsha(sha_lastN, 1, key, scores,members,String.valueOf(limit), String.valueOf(expireSeconds));
        }catch (JedisNoScriptException ex){
            sha_lastN = jedisCluster.scriptLoad(LUA_PUT_LAST_N, key);
            batchPutLastN(key,memberMap,limit,expireSeconds);
        }catch (Exception ex){
            logger.error("redis batch put error,key:{},memberMap:{},limit:{}",key, memberMap,limit,ex);
        }
    }

    public Set<Tuple> zrevrange(String key, int start, int end){
        return jedisCluster.zrevrangeWithScores(key, start, end);
    }

    public Set<Tuple> zrange(String key,int start,int end){
        return jedisCluster.zrangeWithScores(key, start, end);
    }

    public List<String> lrange(String key,int start,int end){
        return jedisCluster.lrange(key, start, end);
    }

    public synchronized void init(String[] servers, String password) {
        GenericObjectPoolConfig<Jedis> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(1000);
        config.setMinIdle(200);
        config.setMaxWaitMillis(20000);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(true);
        config.setTestOnCreate(false);
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        if (ArrayUtils.isNotEmpty(servers)) {
            for (String sever : servers) {
                jedisClusterNodes.add(new HostAndPort(sever.split(":")[0], Integer.parseInt(sever.split(":")[1])));
            }
        }
        int timeout = 20000;
        int maxAttempts = 10;
        int soTimeout = 10000;
        if (password == null || password.length() == 0) {
            jedisCluster = new JedisCluster(jedisClusterNodes, timeout, maxAttempts, config);
        } else {
            jedisCluster = new JedisCluster(jedisClusterNodes, timeout, soTimeout, maxAttempts, password, config);
        }
    }

    public void set(final String key,String value,final int expireSeconds) {
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(value)){
            return;
        }
        jedisCluster.setex(key, expireSeconds, value);
    }

    public void setBytes(final String key,byte[] value,final int expireSeconds) {
        if(StringUtil.isEmpty(key) || value == null){
            return;
        }
        jedisCluster.setex(key.getBytes(),expireSeconds,value);
    }

    public byte[] getBytes(final String key) {
        if(StringUtil.isEmpty(key)){
            return null;
        }
        return jedisCluster.get(key.getBytes());
    }

    public String get(final String key) {
        return jedisCluster.get(key);
    }

    public boolean exist(final String key) {
        return jedisCluster.exists(key);
    }

    public void expire(final String key,int second) throws Exception{
        jedisCluster.expire(key,second);
    }

    public void incrBy(final String key,int step) throws Exception{
        jedisCluster.incrBy(key,step);
    }

    @Deprecated
    public void batchDelete(String hashTag,String prefix){
        ScanParams scanParams = new ScanParams().match(hashTag.concat(prefix).concat("*")).count(200);
        String cur = ScanParams.SCAN_POINTER_START;
        boolean hasNext = true;
        while (hasNext) {
            ScanResult<String> scanResult = jedisCluster.scan(cur, scanParams);
            List<String> keys = scanResult.getResult();
            for (String key : keys) {
                jedisCluster.del(key);
            }
            cur = scanResult.getCursor();
            if (StringUtils.equals("0", cur)) {
                hasNext = false;
            }
        }
    }
}
