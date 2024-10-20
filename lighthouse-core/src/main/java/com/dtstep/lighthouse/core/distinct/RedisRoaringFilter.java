package com.dtstep.lighthouse.core.distinct;
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

import com.dtstep.lighthouse.common.util.BinaryUtil;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.redis.RedisClient;
import com.dtstep.lighthouse.core.redis.RedisOperator;
import com.dtstep.lighthouse.core.rowkey.KeyGenerator;
import com.dtstep.lighthouse.core.rowkey.impl.DefaultKeyGenerator;
import com.google.common.collect.Lists;
import com.dtstep.lighthouse.common.constant.RedisConst;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.exceptions.JedisNoScriptException;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public final class RedisRoaringFilter<T> {

    private static final Logger logger = LoggerFactory.getLogger(RedisRoaringFilter.class);

    private static final KeyGenerator keyGenerator = new DefaultKeyGenerator();

    public static final String BLOOM_LUA_SCRIPT_LOCAL_HASH =
            "local function split(fullStr, separator) \n" +
                    "    local startIndex = 1 \n" +
                    "    local splitIndex = 1 \n" +
                    "    local splitArray = {} \n" +
                    "    while true do \n" +
                    "        local lastIndex = string.find(fullStr, separator, startIndex) \n" +
                    "        if not lastIndex then \n" +
                    "            splitArray[splitIndex] = string.sub(fullStr, startIndex, string.len(fullStr)) \n" +
                    "            break \n" +
                    "        end \n" +
                    "        splitArray[splitIndex] = string.sub(fullStr, startIndex, lastIndex - 1) \n" +
                    "        startIndex = lastIndex + string.len(separator) \n" +
                    "        splitIndex = splitIndex + 1 \n" +
                    "    end \n" +
                    "    return splitArray \n" +
                    "end \n" +
                    "\n" +
                    "local list = split(ARGV[1], \";\") \n" +
                    "local array = {} \n" +
                    "for i = 1, #list do \n" +
                    "    local temp = list[i] \n" +
                    "    local tempArray = split(temp, \",\") \n" +
                    "    for j = 1, #tempArray do \n" +
                    "        local key = KEYS[1] .. '_' .. j .. '{' .. KEYS[1] .. '}' \n" +
                    "        local m = redis.call('r.getbit', key, math.abs(tonumber(tempArray[j], 36))) \n" +
                    "        if(m == 0) then \n" +
                    "            array[i] = 0 \n" +
                    "            break \n" +
                    "        else \n" +
                    "            array[i] = 1 \n" +
                    "        end \n" +
                    "    end \n" +
                    "end \n" +
                    "\n" +
                    "local notExistSize = 0 \n" +
                    "for i = 1, #array do \n" +
                    "    local temp = array[i] \n" +
                    "    if(temp == 0) then \n" +
                    "        notExistSize = notExistSize + 1 \n" +
                    "        local tempArray = split(list[i], \",\") \n" +
                    "        for j = 1, #tempArray do \n" +
                    "            local key = KEYS[1] .. '_' .. j .. '{' .. KEYS[1] .. '}' \n" +
                    "            redis.call('r.setbit', key, math.abs(tonumber(tempArray[j], 36)), 1) \n" +
                    "            if(tonumber(ARGV[2]) ~= -1) then \n" +
                    "                redis.call('expire', key, ARGV[2]) \n" +
                    "            end \n" +
                    "        end \n" +
                    "    end \n" +
                    "end \n" +
                    "\n" +
                    "return notExistSize\n";


    private String sha;

    public RedisRoaringFilter(){
        try{
            this.sha = RedisClient.scriptLoad(BLOOM_LUA_SCRIPT_LOCAL_HASH);
        }catch (Exception ex){
            logger.error("redis script load error",ex);
        }
    }

    private static final RedisRoaringFilter<CharSequence> instance = new RedisRoaringFilter<>();

    public static RedisRoaringFilter<CharSequence> getInstance(){
        return instance;
    }


    public long filterWithRoaringMap(String key, List<T> valueList, long expireSeconds,int part){
        if(CollectionUtils.isEmpty(valueList)){
            return 0;
        }
        final RedisOperator redisOperator = RedisClient.getInstance().getRedisOperator();
        try {
            if (StringUtil.isEmpty(sha)) {
                sha = redisOperator.scriptLoad(BLOOM_LUA_SCRIPT_LOCAL_HASH, key);
            }
            Map<Integer,List<T>> distinctMap = valueList.parallelStream().collect(Collectors.groupingBy(x -> Math.abs(x.hashCode()) % part));
            return distinctMap.entrySet().parallelStream().map(x -> {
                int partIndex = x.getKey();
                List<T> list = x.getValue();
                List<List<T>> parts = Lists.partition(list, 200);
                return parts.parallelStream().map(subList -> {
                    List<String> offsetStrArray = Lists.newArrayList();
                    for (T t : subList) {
                        long hash = Long.parseLong(t.toString(),36);
                        int low = (int) (hash);
                        int high = (int) (hash >>> 32);
                        if(low < 0){low = ~low;}
                        if(high < 0){high = ~high;}
                        offsetStrArray.add(BinaryUtil.translateTenTo36(low) + "," + BinaryUtil.translateTenTo36(high));
                    }
                    String str = StringUtils.join(offsetStrArray, ";");
                    Object result = redisOperator.evalsha(sha, 1, Md5Util.get16MD5(key + "_" + partIndex), str, String.valueOf(expireSeconds));
                    if(result != null && StringUtil.isNumber(result.toString())){
                        return CollectionUtils.isNotEmpty(subList) ? Long.parseLong(result.toString()) : 0L;
                    }else{
                        logger.error("redis roaring check exist error,result:" + result);
                        return 0L;
                    }
                }).mapToLong(z -> z).sum();
            }).mapToLong(x -> x).sum();
        }catch (JedisNoScriptException ex){
            IntStream.range(0, part).forEach((x) ->{
                sha = redisOperator.scriptLoad(BLOOM_LUA_SCRIPT_LOCAL_HASH, Md5Util.get16MD5(key + "_" + x));
            });
            return filterWithRoaringMap(key,valueList,expireSeconds,part);
        }catch (Exception ex){
            logger.error("redis roaring check exist error!",ex);
        }
        return 0;
    }


    public String concatDistinctValue(String distinctValue,String dimensValue,long batchTime){
        if(StringUtil.isEmpty(dimensValue)){
            return distinctValue + "_" + batchTime;
        }else{
            return distinctValue + "_" + dimensValue + "_" + batchTime;
        }
    }

    public String getBloomFilterKey(StatExtEntity statExtEntity, String dimensValue, int functionIndex, long batchTime){
        if(StringUtil.isEmpty(statExtEntity.getTemplateEntity().getDimens())){
            long aggregateTime = DateUtil.batchTime(10, TimeUnit.MINUTES,batchTime);
            String aggregateKey = keyGenerator.resultKey(statExtEntity,functionIndex,null,aggregateTime);
            return RedisConst.RT_BLOOM_DISTINCT_PREFIX + aggregateKey;
        }else{
            long aggregateTime = DateUtil.batchTime(10, TimeUnit.MINUTES,batchTime);
            String aggregateKey = keyGenerator.resultKey(statExtEntity,functionIndex,null,aggregateTime);
            return RedisConst.RT_BLOOM_DISTINCT_PREFIX + aggregateKey + "_" + Math.abs(dimensValue.hashCode()) % 3;
        }
    }

}

