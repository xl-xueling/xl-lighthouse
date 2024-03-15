package com.dtstep.lighthouse.core.test.track;

import com.dtstep.lighthouse.common.constant.RedisConst;
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import com.dtstep.lighthouse.core.redis.RedisClient;
import com.dtstep.lighthouse.core.redis.RedisHandler;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackTest {

    static {
        try{
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testScanMessage() throws Exception {
        int groupId = 100288;
        int statId = 1100520;

    }
}
