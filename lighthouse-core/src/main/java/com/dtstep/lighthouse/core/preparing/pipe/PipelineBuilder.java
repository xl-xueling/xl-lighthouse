package com.dtstep.lighthouse.core.preparing.pipe;

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
