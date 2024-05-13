package com.dtstep.lighthouse.core.preparing.pipe.stage;

import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.core.preparing.handler.translate.DefaultTranslateHandler;
import com.dtstep.lighthouse.core.preparing.handler.translate.TranslateHandler;
import com.dtstep.lighthouse.core.preparing.pipe.PreparingCollector;
import com.dtstep.lighthouse.core.preparing.pipe.PreparingInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class TranslateStageImpl extends PreparingStage<byte[], LightMessage> {

    private static final Logger logger = LoggerFactory.getLogger(TranslateStageImpl.class);

    private static final TranslateHandler translateHandler = new DefaultTranslateHandler();

    @Override
    public void process(PreparingInput<byte[]> input, PreparingCollector<LightMessage> preparingCollector) throws Exception {
        List<LightMessage> lightMessages = translateHandler.translate(input.data);
        preparingCollector.addAll(lightMessages);
    }
}
