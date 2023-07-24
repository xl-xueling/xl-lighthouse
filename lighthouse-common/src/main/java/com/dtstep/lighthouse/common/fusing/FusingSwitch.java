package com.dtstep.lighthouse.common.fusing;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import com.dtstep.lighthouse.common.counter.CycleCounterAdvisor;
import com.dtstep.lighthouse.common.enums.fusing.FusingRules;
import com.dtstep.lighthouse.common.exception.BlockException;
import com.dtstep.lighthouse.common.util.DateUtil;

public final class FusingSwitch {

    public static FusingToken entry(FusingRules fusingRule) throws BlockException {
        long windowTime = DateUtil.batchTime(fusingRule.getDuration(), fusingRule.getTimeUnit(),System.currentTimeMillis());
        long failSize = CycleCounterAdvisor.getValue(fusingRule.getFusingName(),windowTime);
        if(failSize >= fusingRule.getThreshold()){
            return null;
        }else{
            return new FusingToken(fusingRule,windowTime,failSize);
        }
    }

    public static void track(FusingToken token) throws BlockException{
        if(token == null){
            return;
        }
        String fusingName = token.getFusingRule().getFusingName();
        long windowTime = token.getWindowTime();
        CycleCounterAdvisor.increment(fusingName,windowTime);
    }
}
