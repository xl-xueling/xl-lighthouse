package com.dtstep.lighthouse.core.functions;

import com.google.common.base.Splitter;
import com.dtstep.lighthouse.common.aggregator.BlockingEventPool;
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.calculate.MicroBucket;
import com.dtstep.lighthouse.common.entity.calculate.MicroCalculateEnum;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.dtstep.lighthouse.common.constant.StatConst.ILLEGAL_VAL;
import static com.dtstep.lighthouse.common.constant.StatConst.NIL_VAL;

public class SeqStatProcess extends StatProcess<Pair<String,Long>> {

    private static final long serialVersionUID = 9020507116791463383L;

    private static final Logger logger = LoggerFactory.getLogger(SeqStatProcess.class);

    private static final int poolSlotSize = StatConst.DEFAULT_POOL_SLOT_SIZE;

    private static final EventPool<MicroBucket> eventPool = new BlockingEventPool<>("SeqStorageEventPool",poolSlotSize,300000);

    static {
        final int threadSize = 2;
        ScheduledExecutorService service = Executors.newScheduledThreadPool(threadSize,
                new BasicThreadFactory.Builder().namingPattern("seq-consumer-schedule-pool-%d").daemon(true).build());
        for (int i = 0; i < threadSize; i++) {
            service.scheduleWithFixedDelay(new ResultStorageThread(eventPool, batchSize), 0, 5, TimeUnit.SECONDS);
        }
    }

    public SeqStatProcess(StatExtEntity statExtEntity, String metaName, String aggregateKey, String dimensValue){
        this.statExtEntity = statExtEntity;
        this.metaName = metaName;
        this.aggregateKey = aggregateKey;
        int tempIndex = aggregateKey.indexOf(";");
        String rowKey = aggregateKey.substring(0,tempIndex);
        String delta = aggregateKey.substring(tempIndex + 1);
        this.rowKey = rowKey;
        this.delta = delta;
        this.dimensValue = dimensValue;
        this.ttl = getTTL(statExtEntity.getDataExpire());
    }

    @Override
    public void evaluate(StatState statState, List<Pair<String,Long>> messageList, long batchTime) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TemplateEntity templateEntity = statExtEntity.getTemplateEntity();
        long result = messageList.parallelStream().map(x ->{
            Map<String,Object> envMap;
            if(!StringUtil.isEmptyOrNullStr(x.getKey())) {
                Map<String,String> paramMap = Splitter.on(StatConst.SEPARATOR_LEVEL_2).withKeyValueSeparator(StatConst.SEPARATOR_LEVEL_3).split(x.getKey());
                envMap = new HashMap<>(paramMap);
            }else{
                envMap = new HashMap<>();
            }
            return FormulaCalculate.calculate(statState,envMap,batchTime); }).filter(x -> x != NIL_VAL && x != ILLEGAL_VAL).mapToLong(x->x).findFirst().orElse(0);
        if(result == 0L){
            return;
        }
        MicroBucket microBucket = new MicroBucket.Builder()
                .setStatId(statExtEntity.getId())
                .setBatchTime(batchTime)
                .setRowKey(rowKey)
                .setDimensValue(dimensValue)
                .setCalculateEnum(MicroCalculateEnum.SeqCalculate)
                .isLimit(templateEntity.isLimitFlag())
                .setMetaName(metaName)
                .setFunctionIndex(statState.getFunctionIndex())
                .setColumn(delta)
                .setTTL(ttl)
                .setValue(result).create();
        produce(eventPool,microBucket);
        if(logger.isDebugEnabled()){
            logger.debug("lighthouse debug,seq evaluate,stat:{},formula:{},dimens:{},rowKey:{},message size:{},cost:{}"
                    , statExtEntity.getId(),templateEntity.getStat(),dimensValue,rowKey,messageList.size(),stopWatch.getTime());
        }
    }
}
