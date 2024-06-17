package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.dto.StatRenderConfigParam;
import org.junit.Test;

public class TestChartTypes {

    @Test
    public void testChartsTypes() throws Exception{
        String s = "{\"id\":1100583,\"charts\":[{\"functionIndex\":0,\"title\":\"sum(score,behavior_type == '1')\",\"chartType\":3},{\"functionIndex\":1,\"title\":\"count(behavior_type == '1')\",\"chartType\":1}]}\n";
        StatRenderConfigParam renderConfig = JsonUtil.toJavaObject(s,StatRenderConfigParam.class);
        System.out.println("resss:" + renderConfig.getCharts().get(0).getChartType());
        System.out.println("renderConfig:" + JsonUtil.toJSONString(renderConfig));
    }
}
