package com.dtstep.lighthouse.core.preparing.handler.expand;

import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.enums.result.MessageCaptchaEnum;

import java.util.List;

public interface ExpandHandler {

    List<ExpandEvent> expand(MessageCaptchaEnum messageCaptchaEnum, LightMessage lightMessage) throws Exception;
}
