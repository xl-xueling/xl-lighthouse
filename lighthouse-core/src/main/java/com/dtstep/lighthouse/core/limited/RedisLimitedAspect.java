package com.dtstep.lighthouse.core.limited;
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
import com.dtstep.lighthouse.core.redis.RedisHandler;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.util.concurrent.Striped;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisNoScriptException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;


public final class RedisLimitedAspect {

    private static final Logger logger = LoggerFactory.getLogger(RedisLimitedAspect.class);

    private final static Cache<String, Optional<Boolean>> limitedCache = Caffeine.newBuilder()
            .expireAfterWrite(StatConst.LIMIT_BATCH_INTERVAL * 2, TimeUnit.SECONDS)
            .maximumSize(1000000)
            .softValues()
            .build();

    private final static Cache<String, Optional<Integer>> remainCache = Caffeine.newBuilder()
            .expireAfterWrite(StatConst.LIMIT_BATCH_INTERVAL * 2, TimeUnit.SECONDS)
            .maximumSize(1000000)
            .softValues()
            .build();

    private static final RedisLimitedAspect instance = new RedisLimitedAspect();

    private String sha;

    private static final String LUA_LIMIT_SCRIPT =
            " local key = KEYS[1]\n" +
            " local expireTime = tonumber(ARGV[1]) \n" +
            " local step = tonumber(ARGV[2]) \n" +
            " local limitValue = tonumber(ARGV[3]) \n" +
            " local num = redis.call('INCRBY',key,step) \n" +
            " redis.call('expire',key,expireTime) \n" +
            " local result = 0\n" +
            " if tonumber(num) > limitValue + step then" +
            " result = 0" +
            " elseif tonumber(num) < limitValue then" +
            " result = step" +
            " else " +
            " result = step - (num - limitValue) \n" +
            " end\n" +
            " return result";

    private static final Striped<Lock> stripedLock = Striped.lazyWeakLock(80);

    private RedisLimitedAspect(){
        try{
            this.sha = RedisHandler.scriptLoad(LUA_LIMIT_SCRIPT);
        }catch (Exception ex){
            logger.error("script load error",ex);
        }
    }

    public static RedisLimitedAspect getInstance(){
        return instance;
    }

    private int getRemainSize(String limitKey, int step, long limitValue,long expireSeconds) {
        final JedisCluster jedisCluster = RedisHandler.getInstance().getJedisCluster();
        try {
            if (StringUtil.isEmpty(sha) || !jedisCluster.scriptExists(sha, limitKey)) {
                sha = jedisCluster.scriptLoad(LUA_LIMIT_SCRIPT, limitKey);
            }
            Object result = jedisCluster.evalsha(sha, 1, Md5Util.get16MD5(limitKey), String.valueOf(expireSeconds), String.valueOf(step), String.valueOf(limitValue));
            return Integer.parseInt(String.valueOf(result));
        }catch (JedisNoScriptException ex){
            sha = jedisCluster.scriptLoad(LUA_LIMIT_SCRIPT, limitKey);
            return getRemainSize(limitKey, step, limitValue,expireSeconds);
        }catch (Exception ex){
            logger.error("redis try acquire error",ex);
        }
        return 0;
    }

    public boolean tryAcquire(String limitKey,int step,long limitValue,long expireSeconds,int acquireSize) {
        Optional<Boolean> optional = limitedCache.getIfPresent(limitKey);
        if(optional != null && optional.isPresent() && !optional.get()){
            return false;
        }
        Lock lock = stripedLock.get(limitKey);
        lock.lock();
        try{
            Optional<Integer> cacheOptional = remainCache.getIfPresent(limitKey);
            int cacheValue = 0;
            if(cacheOptional != null && cacheOptional.isPresent()){
                cacheValue = cacheOptional.get();
            }
            if(cacheValue > acquireSize){
                remainCache.put(limitKey,Optional.of(cacheValue - acquireSize));
                return true;
            }else{
                int curAcquireSize = acquireSize - cacheValue;
                if(step < curAcquireSize){
                    step = curAcquireSize;
                }
                int remainSize = getRemainSize(limitKey,step,limitValue,expireSeconds);
                if(remainSize < curAcquireSize){
                    limitedCache.put(limitKey,Optional.of(false));
                    return false;
                }else{
                    remainCache.put(limitKey,Optional.of(remainSize - curAcquireSize));
                    return true;
                }
            }
        }catch (Exception ex){
            logger.error("redis limited error!",ex);
            return true;
        }finally {
            lock.unlock();
        }
    }
}
