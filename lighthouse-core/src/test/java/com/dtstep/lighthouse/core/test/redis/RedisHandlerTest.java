package com.dtstep.lighthouse.core.test.redis;

import com.dtstep.lighthouse.core.redis.RedisClient;
import com.dtstep.lighthouse.core.redis.RedisOperator;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisHandlerTest extends CoreBaseTest {

    @Test
    public void test() throws Exception {
        String key = "abc";
        RedisOperator redisOperator = RedisClient.getInstance().getRedisOperator();
        redisOperator.setex(key,5000,"sssss");
        String value = redisOperator.get(key);
        System.out.println("value:" + value);

    }
}
