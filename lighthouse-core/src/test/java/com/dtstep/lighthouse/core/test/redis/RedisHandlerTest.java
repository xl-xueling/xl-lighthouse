package com.dtstep.lighthouse.core.test.redis;

import com.dtstep.lighthouse.core.redis.RedisClient;
import com.dtstep.lighthouse.core.redis.RedisOperator;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisHandlerTest extends CoreBaseTest {

    @Test
    public void test() throws Exception {
        String key = "abc";
        RedisOperator redisOperator = RedisClient.getInstance().getRedisOperator();
        redisOperator.setex(key,5000,"sssss");
        String value = redisOperator.get(key);
        System.out.println("value:" + value);

    }

    @Test
    public void testQuery() throws Exception {
        String key = "0f465f26dc09f1e9_2{0f465f26dc09f1e9}";
        RedisOperator redisOperator = RedisClient.getInstance().getRedisOperator();
        List<String> s = redisOperator.lrange(key,1,100);
        System.out.println("value:" + s);
    }
}
