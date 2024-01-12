package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t1), ZoneId.systemDefault());
        System.out.println("localDateTime:" + localDateTime);
    }
}
