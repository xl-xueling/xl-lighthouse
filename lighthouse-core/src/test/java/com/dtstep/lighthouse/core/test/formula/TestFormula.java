package com.dtstep.lighthouse.core.test.formula;

import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.List;

public class TestFormula {

    @Test
    public void testFormula() throws Exception {
        String formula = "sum(1)";
        Object obj = AviatorHandler.execute(formula);
        System.out.println("obj is:" + obj);
        Pair<String, List<StatState>> pair = FormulaTranslate.translate(formula);
        System.out.println("pair:" + JsonUtil.toJSONString(pair));
    }
}
