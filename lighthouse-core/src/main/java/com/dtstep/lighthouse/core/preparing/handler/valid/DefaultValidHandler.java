package com.dtstep.lighthouse.core.preparing.handler.valid;

import com.dtstep.lighthouse.common.constant.RedisConst;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.LimitingStrategyEnum;
import com.dtstep.lighthouse.common.enums.SwitchStateEnum;
import com.dtstep.lighthouse.common.enums.result.MessageCaptchaEnum;
import com.dtstep.lighthouse.common.modal.Column;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.limiting.LimitingContext;
import com.dtstep.lighthouse.core.limiting.RedisLimitingAspect;
import com.dtstep.lighthouse.core.redis.RedisClient;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class DefaultValidHandler implements ValidHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultValidHandler.class);

    @Override
    public Pair<MessageCaptchaEnum, LightMessage> valid(LightMessage message) throws Exception {
        GroupExtEntity groupEntity = GroupDBWrapper.queryById(message.getGroupId());
        if(groupEntity == null || groupEntity.getState() != GroupStateEnum.RUNNING) {
            return null;
        }
        int threshold = groupEntity.getExtendConfig().getLimitingConfig().getOrDefault(LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMITING,-1);
        if (!LimitingContext.getInstance().tryAcquire(groupEntity,threshold,message.getRepeat())) {
            logger.error("limited trigger strategy:GROUP_MESSAGE_SIZE_LIMIT,group id:{},threshold:{}",groupEntity.getId(),threshold * 60L);
        }
        if(groupEntity.getDebugMode() == SwitchStateEnum.OPEN){
            capture(groupEntity.getId(),message);
        }
        MessageCaptchaEnum resultCodeEnum = validMessage(message,groupEntity);
        if(logger.isTraceEnabled()){
            logger.trace("The group of message matching is:{},valid result:{}",groupEntity.getId(),resultCodeEnum);
        }
        return Pair.of(resultCodeEnum,message);
    }

    private MessageCaptchaEnum validMessage(LightMessage message, GroupExtEntity groupExtEntity) throws Exception {
        List<Column> columnList = groupExtEntity.getColumns();
        if(!MessageValid.valid(groupExtEntity.getId(),message,columnList)){
            return MessageCaptchaEnum.PARAM_CHECK_FAILED;
        } else{
            return MessageCaptchaEnum.SUCCESS;
        }
    }

    private void capture(int groupId, LightMessage message) {
        long batchTime = DateUtil.batchTime(1, TimeUnit.MINUTES, System.currentTimeMillis());
        String lockTrackKey = RedisConst.LOCK_TRACK_PREFIX + "_" + groupId  + "_" + batchTime;
        if(RedisLimitingAspect.getInstance().tryAcquire(lockTrackKey,5,50,TimeUnit.MINUTES.toSeconds(5),1)){
            String trackKey = RedisConst.TRACK_PREFIX + "_" + groupId;
            message.setSystemTime(System.currentTimeMillis());
            if(logger.isTraceEnabled()){
                logger.trace("group[{}] enable debug mode,capture message:{}",groupId,JsonUtil.toJSONString(message));
            }
            RedisClient.getInstance().limitSet(trackKey, JsonUtil.toJSONString(message), StatConst.GROUP_MESSAGE_MAX_CACHE_SIZE,2 * 3600);
        }
    }
}
