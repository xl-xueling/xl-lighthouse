package com.dtstep.lighthouse.core.test.preparing;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.enums.result.MessageCaptchaEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.MessageHelper;
import com.dtstep.lighthouse.core.consumer.RealTimeProcessor;
import com.dtstep.lighthouse.core.preparing.handler.expand.ExpandEvent;
import com.dtstep.lighthouse.core.preparing.pipe.Pipeline;
import com.dtstep.lighthouse.core.preparing.pipe.PipelineBuilder;
import com.dtstep.lighthouse.core.preparing.pipe.stage.ExpandStageImpl;
import com.dtstep.lighthouse.core.preparing.pipe.stage.PreparingStage;
import com.dtstep.lighthouse.core.preparing.pipe.stage.TranslateStageImpl;
import com.dtstep.lighthouse.core.preparing.pipe.stage.ValidStageImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPipeline {

    @Test
    public void testPipeline() throws Exception {
        PreparingStage<byte[],LightMessage> stage1 = new TranslateStageImpl();
        PreparingStage<LightMessage, Pair<MessageCaptchaEnum,LightMessage>> stage2 = new ValidStageImpl();
        PreparingStage<Pair<MessageCaptchaEnum,LightMessage>, ExpandEvent> stage3 = new ExpandStageImpl();
        Pipeline<byte[],ExpandEvent> pipeline = PipelineBuilder.create(stage1)
                .add(stage2).add(stage3).build();
        LightMessage lightMessage = new LightMessage();
        lightMessage.setGroupId(101);
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("province","101");
        lightMessage.setParamMap(paramMap);
        lightMessage.setRepeat(1);
        lightMessage.setTime(System.currentTimeMillis());
        String text = MessageHelper.serializeOfText(lightMessage);
        System.out.println("text:" + text);
        String fullText = text;
        List<ExpandEvent> responseData = pipeline.process(fullText.getBytes());
        System.out.println("pari is:" + JsonUtil.toJSONString(responseData));

    }
}
