package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import org.junit.Test;

public class TestAviator {

    @Test
    public void testFormula() throws Exception {
        String formula = "bbbcontains";
        Object obj = AviatorHandler.execute(formula);
        System.out.println(obj);
    }
}
