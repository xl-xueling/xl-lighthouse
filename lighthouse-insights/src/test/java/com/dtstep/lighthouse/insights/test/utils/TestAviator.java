package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestAviator {

    @Test
    public void testFormula() throws Exception {
        String formula = "'userId'";
        Map<String,Object> envMap = new HashMap<>();
        envMap.put("userId", UUID.randomUUID().toString());
        Object obj = AviatorHandler.execute(formula,envMap);
        System.out.println("obj:" + obj);
    }
}


