package com.dtstep.lighthouse.core.preparing.pipe.stage;

import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.enums.result.MessageCaptchaEnum;
import com.dtstep.lighthouse.core.preparing.handler.valid.DefaultValidHandler;
import com.dtstep.lighthouse.core.preparing.handler.valid.ValidHandler;
import com.dtstep.lighthouse.core.preparing.pipe.PreparingCollector;
import com.dtstep.lighthouse.core.preparing.pipe.PreparingInput;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidStageImpl extends PreparingStage<LightMessage, Pair<MessageCaptchaEnum,LightMessage>> {

    private static final Logger logger = LoggerFactory.getLogger(ValidStageImpl.class);

    private static final ValidHandler validHandler = new DefaultValidHandler();

    @Override
    public void process(PreparingInput<LightMessage> input, PreparingCollector<Pair<MessageCaptchaEnum,LightMessage>> preparingCollector) throws Exception {
        Pair<MessageCaptchaEnum,LightMessage> pair = validHandler.valid(input.data);
        preparingCollector.add(pair);
    }
}
