package com.dtstep.lighthouse.core.redis.standalone;

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
        String slaverIp = servers[1].split(":")[0];
        String slaverPort = servers[1].split(":")[1];
        masterJedis = new Jedis(masterIp,Integer.parseInt(masterPort));
        masterJedis.auth(password);
//        slaverJedis = new Jedis(slaverIp,Integer.parseInt(slaverPort));
//        slaverJedis.auth(password);
    }

    @Override
    public Object evalsha(String sha1, int keyCount, String... params) {
        return masterJedis.evalsha(sha1, keyCount, params);
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
