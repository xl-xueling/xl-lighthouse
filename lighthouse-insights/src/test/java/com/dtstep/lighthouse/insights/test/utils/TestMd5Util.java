package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.lru.Cache;
import com.dtstep.lighthouse.common.lru.LRU;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TestMd5Util {

    private static final Cache<String, String> cacheHolder = LRU.newBuilder().maximumSize(5000).expireAfterWrite(2, TimeUnit.MINUTES).softValues().build();

    @Test
    public void test() throws Exception {
        String md5 = Md5Util.getMD5(null);
        System.out.println(md5);
    }
}
