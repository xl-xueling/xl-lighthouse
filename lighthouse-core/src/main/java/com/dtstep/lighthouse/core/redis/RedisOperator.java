package com.dtstep.lighthouse.core.redis;

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
