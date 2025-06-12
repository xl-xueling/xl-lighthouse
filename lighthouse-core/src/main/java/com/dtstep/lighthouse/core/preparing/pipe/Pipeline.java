package com.dtstep.lighthouse.core.preparing.pipe;
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
import com.dtstep.lighthouse.core.preparing.pipe.stage.PreparingStage;

import java.util.List;

public class Pipeline<IN, OUT>
{
    private final List<PreparingStage<?, ?>> stages;

    Pipeline(List<PreparingStage<?, ?>> stages)
    {
        this.stages = stages;
    }

    @SuppressWarnings("unchecked")
    public List<OUT> process(IN inputData) throws Exception {
        List<?> result = null;
        for(int i=0;i<stages.size();i++){
            PreparingStage<?,?> stage = stages.get(i);
            if(i == 0){
                PreparingCollector collector = stage.generateCollector();
                apply(stage,new PreparingInput<>(inputData),collector);
                result = collector.getData();
            }else{
                PreparingCollector collector = stage.generateCollector();
                for(Object object : result){
                    apply(stage, new PreparingInput<>(object),collector);
                }
                result = collector.getData();
            }
        }
        return (List<OUT>)result;
    }

    private <I, O> void apply(PreparingStage<I, O> stage, PreparingInput<?> dataItem, PreparingCollector<O> outItem) throws Exception
    {
        @SuppressWarnings("unchecked")
        PreparingInput<I> typedDataItem = (PreparingInput<I>)dataItem;
        stage.process(typedDataItem,outItem);
    }
}
