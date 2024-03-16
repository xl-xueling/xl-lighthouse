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
import redis.clients.jedis.Tuple;

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
}
