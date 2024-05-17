package com.dtstep.lighthouse.core.test.data;

import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import com.dtstep.lighthouse.core.rowkey.KeyGenerator;
import com.dtstep.lighthouse.core.rowkey.impl.DefaultKeyGenerator;
import com.dtstep.lighthouse.core.storage.result.ResultStorageSelector;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DataQueryTest extends CoreBaseTest {

    private static final KeyGenerator keyGenerator = new DefaultKeyGenerator();

    @Test
    public void countDataQuery() throws Exception {
        long batchTime = DateUtil.batchTime(1, TimeUnit.DAYS,System.currentTimeMillis());
        System.out.println("batchTime:" + DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"));
        int statId = 1100518;
        Stat stat = StatDBWrapper.queryById(statId);
        System.out.println("stat:" + JsonUtil.toJSONString(stat));
        String aggregateKey = keyGenerator.resultKey(stat,0,null,batchTime);
        System.out.println("aggregateKey is:" + aggregateKey);
    }

    @Test
    public void constGroupMessageMonitor() throws Exception {
        List<Long> batchTimeList = BatchAdapter.queryBatchTimeList("1-minute",DateUtil.getDayStartTime(System.currentTimeMillis()),DateUtil.getDayEndTime(System.currentTimeMillis()));
        StatExtEntity statExtEntity = BuiltinLoader.getBuiltinStat(1014);
        List<Long> batchTimeList2 = List.of(1710227760000L);
        List<StatValue> values = ResultStorageSelector.query(statExtEntity,"100285",batchTimeList);
        for(StatValue statValue : values){
            System.out.println("statValue:" + JsonUtil.toJSONString(statValue));
        }
    }

    @Test
    public void testDecimal() throws Exception {
        BigDecimal bigDecimal = BigDecimal.valueOf(5070.000).stripTrailingZeros();
        Map<String,Object> map = new HashMap<>();
        map.put("a",bigDecimal.toPlainString());
        map.put("b",2);
        Object o = AviatorHandler.execute("a+b",map);
        System.out.println("o is:" + o);
        System.out.println("bigDecimal:" + bigDecimal.toPlainString());
    }

    @Test
    public void testDataQuery() throws Exception {
        int id = 1100577;
        StatExtEntity statExtEntity = StatDBWrapper.queryById(id);
        List<Long> batchTimeList = List.of(1715911200000L);
        List<StatValue> values = ResultStorageSelector.query(statExtEntity,null,batchTimeList);
        for(StatValue statValue : values){
            System.out.println("statValue:" + JsonUtil.toJSONString(statValue));
        }
    }

}
