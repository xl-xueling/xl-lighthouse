package com.dtstep.lighthouse.common.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtilTest {

    @Test
    public void test1() throws Exception {
        String s = "10983325328076.90733";
        BigDecimal bigDecimal = new BigDecimal(s);
        System.out.println("bigDecimal:" + bigDecimal);
        String a = new BigDecimal(s).setScale(3,RoundingMode.HALF_UP).toPlainString();
        System.out.println("a is:" + a);
    }
}
