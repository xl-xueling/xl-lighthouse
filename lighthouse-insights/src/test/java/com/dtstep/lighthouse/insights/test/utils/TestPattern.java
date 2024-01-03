package com.dtstep.lighthouse.insights.test.utils;

import org.junit.Test;

import java.util.regex.Pattern;

public class TestPattern {

    @Test
    public void testPattern() {
        String s = "^[\u4E00-\u9FA5A-Za-z0-9_\\-( )【】（）\\]\\[]+$";
        String str = "swwes";
        boolean is = Pattern.compile(s).matcher(str).matches();
        System.out.println("is:" + is);
    }
}
