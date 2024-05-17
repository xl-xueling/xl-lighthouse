package com.dtstep.lighthouse.core.preparing.pipe.stage;

import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.enums.result.MessageCaptchaEnum;
import com.dtstep.lighthouse.core.preparing.handler.expand.DefaultExpandHandler;
import com.dtstep.lighthouse.core.preparing.handler.expand.ExpandEvent;
import com.dtstep.lighthouse.core.preparing.handler.expand.ExpandHandler;
import com.dtstep.lighthouse.core.preparing.pipe.PreparingCollector;
import com.dtstep.lighthouse.core.preparing.pipe.PreparingInput;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class ExpandStageImpl extends PreparingStage<Pair<MessageCaptchaEnum, LightMessage>, ExpandEvent>{

    private static final Logger logger = LoggerFactory.getLogger(ExpandStageImpl.class);

    private static final ExpandHandler expandHandler = new DefaultExpandHandler();

    @Override
    public void process(PreparingInput<Pair<MessageCaptchaEnum, LightMessage>> input, PreparingCollector<ExpandEvent> preparingCollector) throws Exception {
        if(input == null || input.data == null){
            return;
        }
        List<ExpandEvent> eventList = expandHandler.expand(input.data.getLeft(),input.data.getRight());
        preparingCollector.addAll(eventList);
    }
}
