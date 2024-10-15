package com.dtstep.lighthouse.core.test.utils;

import org.apache.lucene.util.RamUsageEstimator;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class JolUtilTest {

    @Test
    public void testJolLayOut() throws Exception {
        List<String> list = new ArrayList<>();
        System.out.println(System.getProperty("java.vm.name"));

        list.add("asdgasg");
        long s = RamUsageEstimator.sizeOf(1);
        System.out.println("s is:" + s);
    }
}
