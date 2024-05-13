package com.dtstep.lighthouse.core.preparing.pipe;

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
