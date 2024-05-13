package com.dtstep.lighthouse.core.test.preparing;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.enums.result.MessageCaptchaEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.MessageHelper;
import com.dtstep.lighthouse.core.preparing.pipe.Pipeline;
import com.dtstep.lighthouse.core.preparing.pipe.PipelineBuilder;
import com.dtstep.lighthouse.core.preparing.pipe.stage.PreparingStage;
import com.dtstep.lighthouse.core.preparing.pipe.stage.TranslateStageImpl;
import com.dtstep.lighthouse.core.preparing.pipe.stage.ValidStageImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPipeline {

    @Test
    public void testPipeline() throws Exception {
        PreparingStage<byte[],LightMessage> stage1 = new TranslateStageImpl();
        PreparingStage<LightMessage, Pair<MessageCaptchaEnum,LightMessage>> stage2 = new ValidStageImpl();
        Pipeline<byte[],Pair<MessageCaptchaEnum,LightMessage>> pipeline = PipelineBuilder.create(stage1).add(stage2).build();
        LightMessage lightMessage = new LightMessage();
        lightMessage.setGroupId(101);
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("province","101");
        lightMessage.setParamMap(paramMap);
        lightMessage.setTime(System.currentTimeMillis());
        String text = MessageHelper.serializeOfText(lightMessage);
        System.out.println("text:" + text);
        String fullText = text + StatConst.SEPARATOR_LEVEL_0 + text;
        List<Pair<MessageCaptchaEnum,LightMessage>> responseData = pipeline.process(fullText.getBytes());
        System.out.println("pari is:" + JsonUtil.toJSONString(responseData));
    }
}
