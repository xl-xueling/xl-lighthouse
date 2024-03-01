package com.dtstep.lighthouse.core.rowkey.impl;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.hash.HashUtil;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.sbr.StringBuilderHolder;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.rowkey.KeyGenerator;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DefaultKeyGenerator implements KeyGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DefaultKeyGenerator.class);

    private final static Cache<String, String> resultKeyCache = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .maximumSize(5000)
            .softValues()
            .build();

    @Override
    public String resultKey(Stat stat, int functionIndex, String dimensValue, long batchTime) {
        String cacheKey = stat.getRandomId() + "_" + functionIndex + "_" + dimensValue + "_" + batchTime;
        String rowKey = resultKeyCache.get(cacheKey, k -> generateBatchKey(stat,functionIndex,dimensValue,batchTime));
        return Objects.requireNonNull(rowKey);
    }

    public static String generateBatchKey(Stat stat, int functionIndex, String dimens, long batchTime) {
        String key = null;
        try{
            long baseTime;
            String delta;
            String timeparam = stat.getTimeparam();
            String [] arr = timeparam.split("-");
            int interval = Integer.parseInt(arr[0]);
            String timeUnitStr = arr[1];
            TimeUnit timeUnit;
            switch (timeUnitStr) {
                case "minute":
                    baseTime = DateUtil.getHourStartTime(batchTime);
                    timeUnit = TimeUnit.MINUTES;
                    break;
                case "hour":
                    baseTime = DateUtil.getDayStartTime(batchTime);
                    timeUnit = TimeUnit.HOURS;
                    break;
                case "day":
                    baseTime = DateUtil.getMonthStartTime(batchTime);
                    timeUnit = TimeUnit.DAYS;
                    break;
                case "second":
                    baseTime = DateUtil.getHourStartTime(batchTime);
                    timeUnit = TimeUnit.SECONDS;
                    break;
                default:
                    throw new Exception();
            }
            long duration = timeUnit.toMillis(interval);
            String baseKey = generateBatchBaseKey(stat.getRandomId(), stat.getDataVersion(), dimens, baseTime,functionIndex);
            delta = Long.toHexString((batchTime - baseTime) / duration);
            key = StringBuilderHolder.Smaller.getStringBuilder().append(baseKey).append(";").append(delta).toString();
        }catch (Exception ex){
            logger.error("generate batch key error!",ex);
        }
        return key;
    }

    public static String generateBatchBaseKey(String randomId,int dataVersion,String dimens,long baseTime,int functionIndex) {
        String time = DateUtil.formatTimeStamp(baseTime, "yyyyMMddHHmmss");
        String origin = Md5Util.getMD5(randomId + "_" + dataVersion + "_" + time + "_" + dimens + "_" + functionIndex);
        String prefix = getPrePartitionPrefix(origin,dimens);
        return prefix + origin;
    }

    private static String getPrePartitionPrefix(String origin, String dimens){
        int index;
        if(StringUtil.isEmpty(dimens)){
            index = Math.abs((int) (HashUtil.BKDRHash(origin) % SysConst._DATA_STORAGE_PRE_PARTITIONS_SIZE));
        }else{
            index = Math.abs((int) (HashUtil.BKDRHash(origin + "_" + dimens) % SysConst._DATA_STORAGE_PRE_PARTITIONS_SIZE));
        }
        return SysConst._DBKeyPrefixArray[index];
    }

    @Override
    public String dimensKey(GroupExtEntity groupExtEntity, String dimens, String dimensValue) {
        return null;
    }
}
