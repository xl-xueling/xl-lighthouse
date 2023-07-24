package com.dtstep.lighthouse.common.sbr;

import org.junit.Test;

public class StringBuilderHolderTest {

    @Test
    public void test01() throws Exception{
        StringBuilder sbr = StringBuilderHolder.Bigger.getStringBuilder();
        System.out.println("sbr1:" + sbr);
    }
}
