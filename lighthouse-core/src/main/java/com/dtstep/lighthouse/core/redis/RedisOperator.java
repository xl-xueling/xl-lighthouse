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
import redis.clients.jedis.resps.Tuple;

import java.util.List;

public interface RedisOperator {

    void init(String[] servers, String password);

    Object evalsha(String sha1, int keyCount, String... params);

    void del(String key);

    List<Tuple> zrevrangeWithScores(String key, long start, long stop);

    List<Tuple> zrangeWithScores(String key, long start, long stop);

    List<String> lrange(String key, long start, long stop);

    String setex(byte[] key, long seconds, byte[] value);

    String setex(String key, long seconds, String value);

    boolean exists(String var1);

    byte[] get(byte[] key);

    String get(String key);

    long expire(String key, long seconds);

    long incrBy(String key, long increment);

    Boolean scriptExists(String sha1, String sampleKey);

    String scriptLoad(String script);

    String scriptLoad(String script,String key);
}
