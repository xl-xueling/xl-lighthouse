package com.dtstep.lighthouse.core.preparing.handler.translate;

import com.dtstep.lighthouse.common.entity.message.LightMessage;

import java.util.List;

public interface TranslateHandler {

    List<LightMessage> translate(byte[] bytes) throws Exception;
}
