package com.dtstep.lighthouse.core.preparing.handler.valid;

import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.enums.result.MessageCaptchaEnum;
import org.apache.commons.lang3.tuple.Pair;

public interface ValidHandler {

    Pair<MessageCaptchaEnum, LightMessage> valid(LightMessage lightMessage) throws Exception;
}
