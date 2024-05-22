package com.dtstep.lighthouse.core.test.redis;

import com.dtstep.lighthouse.core.redis.RedisClient;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisHandlerTest extends CoreBaseTest {

    @Test
    public void test() throws Exception {
        String key = "abc";
//        RedisHandler.getInstance().set(key,"ssss",6000000);
//        Jedis jedisMaster = new Jedis("10.206.6.35", 7101);
//        jedisMaster.auth("naJKcZEoGGeSSAad");
//        jedisMaster.set(key,"sasdgag");

        RedisClient.getInstance().getRedisOperator().setex(key,6000000,"ssss");
        String value = RedisClient.getInstance().getRedisOperator().get(key);
        System.out.println("value:" + value);


    }
}
