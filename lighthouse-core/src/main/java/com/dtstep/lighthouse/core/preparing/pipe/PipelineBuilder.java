package com.dtstep.lighthouse.core.preparing.pipe;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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

import java.util.ArrayList;
import java.util.List;

public class PipelineBuilder<IN, OUT>
{
    private final List<PreparingStage<?, ?>> stages;

    public static <IN, OUT> PipelineBuilder<IN, OUT> create(PreparingStage<IN, OUT> stage)
    {
        return new PipelineBuilder<IN, OUT>(stage);
    }

    private PipelineBuilder(PreparingStage<IN, OUT> stage)
    {
        stages = new ArrayList<>();
        stages.add(stage);
    }

    public <NextOutput> PipelineBuilder<IN, NextOutput> add(PreparingStage<OUT, NextOutput> stage)
    {
        stages.add(stage);
        @SuppressWarnings("unchecked")
        PipelineBuilder<IN, NextOutput> result =
                (PipelineBuilder<IN, NextOutput>) this;
        return result;
    }

    public Pipeline<IN, OUT> build()
    {
        return new Pipeline<>(stages);
    }
}
