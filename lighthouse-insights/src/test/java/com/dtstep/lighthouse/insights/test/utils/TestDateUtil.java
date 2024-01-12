package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestDateUtil {

    @Test
    public void test() throws Exception {
        long t = TimeUnit.DAYS.toSeconds(7);
        System.out.println("t:" + t);
    }

    @Test
    public void testBatchTime() throws Exception {
        long t1 = 1704902400000L;
        long t2 = 1704988800000L;
        List<Long> list = BatchAdapter.queryBatchTimeList("30-minute",t1,t2);
        for(int i=0;i<list.size();i++){
            System.out.println("list:" + DateUtil.formatTimeStamp(list.get(i),"yyyy-MM-dd HH:mm:ss"));
        }
    }
}
