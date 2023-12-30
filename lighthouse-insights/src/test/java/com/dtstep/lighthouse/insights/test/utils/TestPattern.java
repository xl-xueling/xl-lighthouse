package com.dtstep.lighthouse.insights.test.utils;

import org.junit.Test;

import java.util.regex.Pattern;

public class TestPattern {

    @Test
    public void testPattern() {
        String s = "^[a-zA-Z0-9_][a-zA-Z0-9_,.#!$%]{5,24}$";
        String str = "123456,.23653092#=";
        boolean is = Pattern.compile(s).matcher(str).matches();
        System.out.println("is:" + is);
    }
}
