package com.dtstep.lighthouse.core.redis.standalone;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
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
import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.core.redis.RedisOperator;
import org.apache.commons.lang3.ArrayUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import java.util.List;

public class RedisStandaloneOperator implements RedisOperator {

    private static Jedis masterJedis;

    @Override
    public synchronized void init(String[] servers, String password) {
        if (ArrayUtils.isEmpty(servers) || servers.length != 2) {
            throw new InitializationException("redis init failed");
        }
        String masterIp = servers[0].split(":")[0];
        String masterPort = servers[0].split(":")[1];
        masterJedis = new Jedis(masterIp,Integer.parseInt(masterPort));
        masterJedis.auth(password);
    }

    @Override
    public Object evalsha(String sha1, int keyCount, String... params) {
        return masterJedis.evalsha(sha1, keyCount, params);
    }

    @Override
    public Object evalsha(String sha1,List<String> keys, List<String> params) {
        return masterJedis.evalsha(sha1,keys,params);
    }

    @Override
    public Boolean scriptExists(String sha1, String sampleKey) {
        return masterJedis.scriptExists(sha1);
    }

    @Override
    public String scriptLoad(String script, String sampleKey) {
        return masterJedis.scriptLoad(script);
    }

    @Override
    public List<Tuple> zrangeWithScores(String key, long start, long stop) {
        return masterJedis.zrevrangeWithScores(key, start, stop);
    }

    @Override
    public String scriptLoad(String script) {
        return masterJedis.scriptLoad(script);
    }

    @Override
    public void del(String key) {
        masterJedis.del(key);
    }

    @Override
    public List<Tuple> zrevrangeWithScores(String key, long start, long stop) {
        return masterJedis.zrevrangeWithScores(key, start, stop);
    }

    @Override
    public List<String> lrange(String key, long start, long stop) {
        return masterJedis.lrange(key, start, stop);
    }

    @Override
    public String setex(byte[] key, long seconds, byte[] value) {
        return masterJedis.setex(key, seconds, value);
    }

    @Override
    public boolean exists(String var1) {
        return masterJedis.exists(var1);
    }

    @Override
    public String setex(String key, long seconds, String value) {
        return masterJedis.setex(key, seconds, value);
    }

    @Override
    public byte[] get(byte[] key) {
        return masterJedis.get(key);
    }

    @Override
    public String get(String key) {
        return masterJedis.get(key);
    }

    @Override
    public long expire(String key, long seconds) {
        return masterJedis.expire(key, seconds);
    }

    @Override
    public long incrBy(String key, long increment) {
        return masterJedis.incrBy(key, increment);
    }
}
