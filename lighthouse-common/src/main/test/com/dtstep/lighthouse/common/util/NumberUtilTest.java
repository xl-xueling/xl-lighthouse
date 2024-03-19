package com.dtstep.lighthouse.common.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtilTest {

    @Test
    public void test1() throws Exception {
        Double d = 12002579690694.587;
//        double d3 = 12002579690694.587d;
//        String s = "12002579690694.587";
//        String s2 = String.valueOf(d3);
//        String s4 = Double.toString(d);
//        String s5 = String.format("%.3f",d);
//        System.out.println("s2:" + s2);
//        System.out.println("s5:" + s5);
//        System.out.println("d2:" + new BigDecimal(s).toPlainString());
//        DecimalFormat decimalFormat = new DecimalFormat("#.####");
//        System.out.println(decimalFormat.format(d)); // 0.0005

        String s = d + "";
        System.out.println("s:" + s);
    }
}
