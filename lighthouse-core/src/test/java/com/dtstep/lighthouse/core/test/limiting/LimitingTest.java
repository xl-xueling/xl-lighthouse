package com.dtstep.lighthouse.core.test.limiting;

import com.dtstep.lighthouse.common.enums.LimitingStrategyEnum;
import com.dtstep.lighthouse.common.modal.GroupExtendConfig;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

import java.util.HashMap;

public class LimitingTest {

    @Test
    public void test1() throws Exception {
        HashMap<LimitingStrategyEnum,Integer> paramMap = new HashMap<>();
        paramMap.put(LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMITING,10);
        paramMap.put(LimitingStrategyEnum.STAT_RESULT_SIZE_LIMITING,100);
        System.out.println("paramMap is:" + JsonUtil.toJSONString(paramMap));

        GroupExtendConfig groupExtendConfig = new GroupExtendConfig();
        groupExtendConfig.setLimitingConfig(paramMap);
        String str = JsonUtil.toJSONString(groupExtendConfig);
        System.out.println("str is:" + str);

        GroupExtendConfig extendConfig2 = JsonUtil.toJavaObject(str,GroupExtendConfig.class);
        System.out.println("extendCOnfig2:" + JsonUtil.toJSONString(extendConfig2));

    }
}
