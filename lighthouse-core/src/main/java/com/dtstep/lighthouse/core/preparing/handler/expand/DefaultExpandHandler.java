package com.dtstep.lighthouse.core.preparing.handler.expand;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.LimitingStrategyEnum;
import com.dtstep.lighthouse.common.enums.result.MessageCaptchaEnum;
import com.dtstep.lighthouse.common.hash.HashUtil;
import com.dtstep.lighthouse.common.sbr.StringBuilderHolder;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.distinct.RedisRoaringFilter;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import com.dtstep.lighthouse.core.limiting.LimitingContext;
import com.dtstep.lighthouse.core.rowkey.KeyGenerator;
import com.dtstep.lighthouse.core.rowkey.impl.DefaultKeyGenerator;
import com.dtstep.lighthouse.core.wrapper.DimensDBWrapper;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash_snp.Hashing;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultExpandHandler implements ExpandHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExpandHandler.class);

    private static final KeyGenerator keyGenerator = new DefaultKeyGenerator();

    @Override
    public List<ExpandEvent> expand(MessageCaptchaEnum messageCaptchaEnum,LightMessage lightMessage) throws Exception {
        GroupExtEntity groupEntity = GroupDBWrapper.queryById(lightMessage.getGroupId());
        if(groupEntity == null || groupEntity.getState() != GroupStateEnum.RUNNING){
            return null;
        }
        List<ExpandEvent> eventList = new ArrayList<>();
        int captcha = messageCaptchaEnum.getCaptcha();
        if(!groupEntity.isBuiltIn()){
            Map<String,String> paramMap = ImmutableMap.of("groupId",String.valueOf(groupEntity.getId()),"captcha",String.valueOf(captcha));
            List<ExpandEvent> list = appendBuiltInMonitor(StatConst.BUILTIN_MSG_STAT, paramMap, lightMessage.getRepeat());
            if(CollectionUtils.isNotEmpty(list)){
                eventList.addAll(list);
            }
        }
        if(messageCaptchaEnum != MessageCaptchaEnum.SUCCESS){
            return eventList;
        }
        List<StatExtEntity> statList = StatDBWrapper.queryRunningListByGroupId(groupEntity.getId());
        if(CollectionUtils.isNotEmpty(statList)){
            if(logger.isTraceEnabled()){
                logger.trace("Group:{},valid statistical items size:{}",groupEntity.getId(),statList.size());
            }
            for(StatExtEntity statEntity : statList) {
                List<ExpandEvent> childList = append(statEntity, groupEntity, lightMessage);
                if(CollectionUtils.isNotEmpty(childList)){
                    eventList.addAll(childList);
                }
            }
        }
        return eventList;
    }

    private List<ExpandEvent> append(StatExtEntity statEntity,GroupExtEntity groupEntity,LightMessage message) throws Exception {
        TemplateEntity templateEntity = statEntity.getTemplateEntity();
        String dimensValue = null;
        long batchTime = DateUtil.batchTime(statEntity.getTimeParamInterval(), statEntity.getTimeUnit(), message.getTime());
        HashMap<String,Object> envMap = new HashMap<>(message.getParamMap());
        if (!StringUtil.isEmpty(templateEntity.getDimens())) {
            dimensValue = DimensDBWrapper.getDimensValue(envMap, templateEntity.getDimensArray(),batchTime);
            int threshold = groupEntity.getExtendConfig().getLimitingConfig().getOrDefault(LimitingStrategyEnum.STAT_RESULT_SIZE_LIMITING,-1);
            if (!LimitingContext.getInstance().tryAcquire(statEntity,threshold,1)) {
                logger.error("limiting trigger strategy:STAT_RESULT_SIZE_LIMIT,token:{},stat:{},threshold:{}"
                        ,groupEntity.getToken(),statEntity.getId(),threshold * 60L);
                return null;
            }
        }
        List<ExpandEvent> eventList = new ArrayList<>();
        List<StatState> statStateList = templateEntity.getStatStateList();
        for (StatState statState : statStateList) {
            try {
                boolean isDistinct = StatState.isBitCountState(statState);
                String distinctValue = null;
                if(isDistinct){
                    distinctValue = String.valueOf(FormulaCalculate.parseVariableEntity(StatState.getFirstUnit(statState), envMap, batchTime));
                    if(logger.isTraceEnabled()){
                        logger.trace("expand message,stat:{},envMap:{},batchTime:{},distinctValue:{}",statEntity.getId(), JsonUtil.toJSONString(envMap)
                        ,DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"),distinctValue);
                    }
                    distinctValue = RedisRoaringFilter.getInstance().concatDistinctValue(distinctValue,dimensValue,batchTime);
                    long hash = Hashing.murmur3_128().hashBytes(distinctValue.getBytes(StandardCharsets.UTF_8)).asLong();
                    distinctValue = java.lang.Long.toString(Math.abs(hash), 36);
                    envMap.put(StatConst.DISTINCT_COLUMN_NAME,distinctValue);
                }
                String aggregateKey = keyGenerator.resultKey(statEntity, statState.getFunctionIndex(), dimensValue, batchTime);
                if(!groupEntity.isBuiltIn()){
                    Map<String,String> paramMap = ImmutableMap.of("statId",String.valueOf(statEntity.getId()),"resultKey",aggregateKey);
                    List<ExpandEvent> builtInEvents = appendBuiltInMonitor(StatConst.BUILTIN_RESULT_STAT,paramMap, message.getRepeat());
                    if(CollectionUtils.isNotEmpty(builtInEvents)){
                        eventList.addAll(builtInEvents);
                    }
                }
                String data  = envMap.entrySet().stream().filter(x -> statState.getRelatedColumnSet().contains(x.getKey()) || x.getKey().equals(StatConst.DISTINCT_COLUMN_NAME))
                        .map(x -> x.getKey() + StatConst.SEPARATOR_LEVEL_3 + x.getValue()).collect(Collectors.joining(StatConst.SEPARATOR_LEVEL_2));
                String result = StringBuilderHolder.Smaller.getStringBuilder().append(aggregateKey)
                        .append(StatConst.SEPARATOR_LEVEL_1).append(statEntity.getId())
                        .append(StatConst.SEPARATOR_LEVEL_1).append(data)
                        .append(StatConst.SEPARATOR_LEVEL_1).append(dimensValue)
                        .append(StatConst.SEPARATOR_LEVEL_1).append(statState.getFunctionIndex())
                        .append(StatConst.SEPARATOR_LEVEL_1).append(batchTime)
                        .toString();
                if(logger.isTraceEnabled()){
                    logger.trace("deliver stat message,statId:{},distinct:{},evnMap:{},dimens:{},repeat:{},result:{},batch:{}",
                            statEntity.getId(),distinctValue,JsonUtil.toJSONString(envMap),dimensValue,message.getRepeat(),result,DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"));
                }
                ExpandEvent expandEvent = new ExpandEvent(getSlot(aggregateKey),result,message.getRepeat());
                eventList.add(expandEvent);
            } catch (Exception ex){
                logger.error("deliver message error,group:{}",groupEntity.getId(), ex);
            }
        }
        return eventList;
    }

    private List<ExpandEvent> appendBuiltInMonitor(String builtInToken, Map<String,String> paramMap, int repeat) throws Exception {
        GroupExtEntity monitorGroup = BuiltinLoader.getBuiltinGroup(builtInToken);
        if(monitorGroup == null){
            return null;
        }
        LightMessage monitorMessage = new LightMessage();
        monitorMessage.setParamMap(paramMap);
        monitorMessage.setGroupId(monitorGroup.getId());
        monitorMessage.setRepeat(repeat);
        monitorMessage.setTime(System.currentTimeMillis());
        List<StatExtEntity> monitorStats = StatDBWrapper.queryRunningListByGroupId(monitorGroup.getId());
        List<ExpandEvent> result = null;
        for(StatExtEntity monitorStat : monitorStats){
            result = append(monitorStat,monitorGroup,monitorMessage);
        }
        return result;
    }

    private int getSlot(String aggregateKey){
        return HashUtil.getHashIndex(aggregateKey,StatConst.DEFAULT_POOL_SLOT_SIZE);
    }

}
