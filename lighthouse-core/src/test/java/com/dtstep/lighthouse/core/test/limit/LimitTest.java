package com.dtstep.lighthouse.core.test.limit;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.redis.RedisHandler;
import org.junit.Test;
import redis.clients.jedis.Tuple;

import java.util.Set;

public class LimitTest {

    static {
        try{
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void test() throws Exception {
        String key = "limitN_18|54318339062610a25ea2d793aa9c7464_0";
        Set<Tuple> tuples = RedisHandler.getInstance().zrange(key,0,100);
        for(Tuple tuple : tuples){
            System.out.println("tuples is:" + tuple.getElement() + ",score:" + tuple.getScore());
        }

    }
}
