package com.dtstep.lighthouse.core.preparing.pipe.stage;

import com.dtstep.lighthouse.core.preparing.pipe.PreparingCollector;
import com.dtstep.lighthouse.core.preparing.pipe.PreparingInput;

public abstract class PreparingStage<IN, OUT>
{
    public abstract void process(PreparingInput<IN> input, PreparingCollector<OUT> preparingCollector) throws Exception;

    public PreparingCollector<OUT> generateCollector(){
        return new PreparingCollector<>();
    }
}
