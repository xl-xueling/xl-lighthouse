package com.dtstep.lighthouse.core.test;

import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import org.junit.Test;

import java.util.HashMap;

public class AviatorHandlerTest {

    @Test
    public void testFormula() throws Exception {
        String formula = "a = 'abc'";
        HashMap<String,Object> envMap = new HashMap<>();
        envMap.put("a","abc");
        Object obj = AviatorHandler.execute(formula,envMap);
        System.out.println("obj:" + obj);
    }
}
