package com.dtstep.lighthouse.core.test.limit;

import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.redis.RedisHandler;
import com.dtstep.lighthouse.core.storage.limit.LimitStorageSelector;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.junit.Test;
import redis.clients.jedis.resps.Tuple;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
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
        long batchTime = DateUtil.parseDate("2024-03-16 11:40:00","yyyy-MM-dd HH:mm:ss");
        System.out.println("batchTime is:" + batchTime);
        int statId = 1100522;
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        List<LimitValue> list = LimitStorageSelector.query(statExtEntity,batchTime);
        for(LimitValue limitValue : list){
            System.out.println("limitValue:" + JsonUtil.toJSONString(limitValue));
        }
    }

    @Test
    public void test2() throws Exception {
        String key = "limitN_0ac242bfb01d04a4799bcdee78d74228_2";
        List<Tuple> tuples = RedisHandler.getInstance().zrange(key,0,100);
        for(Tuple tuple : tuples){
            System.out.println("tuple is:" + tuple.getElement() + ",score:"
                    + BigDecimal.valueOf(tuple.getScore()));
        }
    }

    @Test
    public void test3() throws Exception {
        LinkedHashMap<String,String> memberMap = new LinkedHashMap<>();
        RedisHandler.getInstance().batchPutTopN("test2",memberMap,100,1000000000);
        List<Tuple> tuples = RedisHandler.getInstance().zrange("test2",0,100);
        Thread.sleep(1000);
        for(Tuple tuple : tuples){
            BigDecimal d = BigDecimal.valueOf(1000000);
            System.out.println("r1 is:" + BigDecimal.valueOf(tuple.getScore()).stripTrailingZeros().toPlainString());
            System.out.println("tuple is:" + tuple.getElement() + ",score:" + new BigDecimal(Double.toString(tuple.getScore())).multiply(d).stripTrailingZeros().toPlainString());
        }
    }
}
