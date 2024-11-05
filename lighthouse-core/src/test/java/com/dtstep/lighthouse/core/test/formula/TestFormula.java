package com.dtstep.lighthouse.core.test.formula;

import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFormula {

    @Test
    public void testFormula() throws Exception {
        String s = "{\"template\":\"<stat-item  title=\"sasdgasgss \" stat=\"count(asdgadg==''s)\"  />\",\"groupId\":100310,\"projectId\":11137,\"expired\":15552000,\"timeparam\":\"1-hour\",\"desc\":\"SSS\"}";
        System.out.println(s);
        String formula = "asdgadg==''s";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("asdgadg","s");
        Object obj = AviatorHandler.execute(formula,paramMap);
        System.out.println("obj is:" + obj);
    }
}
