package com.dtstep.lighthouse.insights.test.utils;

import org.junit.Test;

import java.util.regex.Pattern;

public class TestPattern {

    @Test
    public void testPattern() {
        String s = "^[\\u4E00-\\u9FA5a-zA-Z0-9_()（）【】 \\[\\]#\\s]+$";
        String str = "你好（）【体验部】用户增长中心";
        boolean is = Pattern.compile(s).matcher(str).matches();
        System.out.println("is:" + is);
    }
}
