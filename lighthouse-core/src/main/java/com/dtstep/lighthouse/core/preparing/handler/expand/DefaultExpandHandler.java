package com.dtstep.lighthouse.core.preparing.handler.expand;

import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.result.MessageCaptchaEnum;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultExpandHandler implements ExpandHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExpandHandler.class);

    @Override
    public List<ExpandEvent> expand(MessageCaptchaEnum messageCaptchaEnum,LightMessage lightMessage) throws Exception {
        return null;
    }

}
