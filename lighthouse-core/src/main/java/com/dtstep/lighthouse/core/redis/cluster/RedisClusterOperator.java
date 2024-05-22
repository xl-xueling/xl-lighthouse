package com.dtstep.lighthouse.core.redis.cluster;

import com.dtstep.lighthouse.core.redis.RedisOperator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Connection;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.resps.Tuple;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RedisClusterOperator implements RedisOperator {

    private static JedisCluster jedisCluster = null;

    @Override
    public synchronized void init(String[] servers, String password) {
        GenericObjectPoolConfig<Connection> config = new GenericObjectPoolConfig<>();
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

    @Override
    public Boolean scriptExists(String sha1, String sampleKey) {
        return jedisCluster.scriptExists(sha1, sampleKey);
    }

    @Override
    public String scriptLoad(String script, String sampleKey) {
        return jedisCluster.scriptLoad(script,sampleKey);
    }

    @Override
    public List<Tuple> zrangeWithScores(String key, long start, long stop) {
        return jedisCluster.zrevrangeWithScores(key, start, stop);
    }

    @Override
    public String scriptLoad(String script) {
        return jedisCluster.scriptLoad(script);
    }

    @Override
    public Object evalsha(String sha1, int keyCount, String... params) {
        return jedisCluster.evalsha(sha1,keyCount,params);
    }

    @Override
    public void del(String key) {
        jedisCluster.del(key);
    }

    @Override
    public List<Tuple> zrevrangeWithScores(String key, long start, long stop) {
        return jedisCluster.zrevrangeWithScores(key, start, stop);
    }

    @Override
    public List<String> lrange(String key, long start, long stop) {
        return jedisCluster.lrange(key,start,stop);
    }

    @Override
    public String setex(byte[] key, long seconds, byte[] value) {
        return jedisCluster.setex(key, seconds, value);
    }

    @Override
    public String setex(String key, long seconds, String value) {
        return jedisCluster.setex(key, seconds, value);
    }

    @Override
    public boolean exists(String var1) {
        return jedisCluster.exists(var1);
    }

    @Override
    public byte[] get(byte[] key) {
        return jedisCluster.get(key);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public long expire(String key, long seconds) {
        return jedisCluster.expire(key, seconds);
    }

    @Override
    public long incrBy(String key, long increment) {
        return jedisCluster.incrBy(key,increment);
    }
}
