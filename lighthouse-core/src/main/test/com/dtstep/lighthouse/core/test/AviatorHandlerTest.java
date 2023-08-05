package com.dtstep.lighthouse.core.test;

import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import org.junit.Test;

import java.util.HashMap;

public class AviatorHandlerTest {

    @Test
    public void testFormula() throws Exception {
        String formula = "split('sasg#asdgag','#','0')";
        Object object = AviatorHandler.execute(formula);
        System.out.println("object:" + object);

    }
}
