package com.dtstep.lighthouse.core.preparing.pipe.stage;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
