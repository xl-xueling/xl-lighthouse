package com.dtstep.lighthouse.core.test.limit;

import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.redis.RedisHandler;
import com.dtstep.lighthouse.core.storage.limit.LimitStorageSelector;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import redis.clients.jedis.resps.Tuple;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
        memberMap.put("a",new BigDecimal("111").toPlainString());
        memberMap.put("b",new BigDecimal("1946044683225899.999").toPlainString());
        memberMap.put("c",new BigDecimal("1946044683225899.198").toPlainString());
        RedisHandler.getInstance().batchPutTopN("test3",memberMap,100,1000000000);
        List<Tuple> tuples = RedisHandler.getInstance().zrevrange("test3",0,100);
        Thread.sleep(1000);
        for(Tuple tuple : tuples){
            System.out.println("tuple is:" + tuple.getElement() + ",r1 is:" + BigDecimal.valueOf(tuple.getScore()).stripTrailingZeros().toPlainString());
        }
    }

    @Test
    public void test34() throws Exception {
        String key = "limitN_c86c43af1b9c4151b91f7a5ca6434bfc_2";
        List<Tuple> tuples = RedisHandler.getInstance().zrevrange(key,0,100);
        for(int i=0;i<tuples.size();i++){
            Tuple tuple = tuples.get(i);
            System.out.println("tuple:" + tuple.getElement() + ",score:" + new BigDecimal(tuple.getScore() + ""));
        }
    }

    @Test
    public void test39() throws Exception{
        String a = "河南省";
        String b = "河北省";
        System.out.println("1:" + Bytes.toBytes(a));
        System.out.println("2:" + Bytes.toBytes(b));
    }

    @Test
    public void test40() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        List<String> list2 = list.subList(0,list.size());
        System.out.println("list2 size:" + list2.size());
    }
}
