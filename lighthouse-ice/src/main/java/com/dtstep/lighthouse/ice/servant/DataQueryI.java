package com.dtstep.lighthouse.ice.servant;

import Ice.Current;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.ice._DataQueryInterfaceDisp;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.storage.proxy.ResultStorageProxy;
import com.dtstep.lighthouse.core.wrapper.DimensDBWrapper;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class DataQueryI extends _DataQueryInterfaceDisp {

    private static final long serialVersionUID = -7591484503430548570L;

    private static final Logger logger = LoggerFactory.getLogger(DataQueryI.class);

    DataQueryI() {}

    @Override
    public String dataQuery(int statId, String dimens, long startTime, long endTime, Current __current) {
        String result = null;
        try{
            StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
            if(statExtEntity == null){
                return null;
            }
            GroupExtEntity groupExtEntity = GroupDBWrapper.queryById(statExtEntity.getGroupId());
            if(groupExtEntity == null){
                return null;
            }
            List<Long> batchTimeList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeParam(),startTime,endTime);
            if(CollectionUtils.isEmpty(batchTimeList)){
                return null;
            }
            if(batchTimeList.size() > 10000){
                batchTimeList = batchTimeList.subList(0,10000);
            }
            List<StatValue> values = ResultStorageProxy.queryWithDimens(statExtEntity,dimens,batchTimeList);
            if(CollectionUtils.isNotEmpty(values)){
                result = JsonUtil.toJSONString(values);
            }
            if(logger.isDebugEnabled()){
                logger.debug("data query with batch list,statId:{},dimens:{},result:{}",statId,dimens,result);
            }
        }catch (Exception ex){
            logger.error("data query error",ex);
        }
        return result;
    }

    @Override
    public String dataQueryWithBatchList(int statId, String dimens, List<Long> batchTimeList, Current __current) {
        String result = null;
        try{
            StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
            if(statExtEntity == null){
                return null;
            }
            GroupExtEntity groupExtEntity = GroupDBWrapper.queryById(statExtEntity.getGroupId());
            if(groupExtEntity == null){
                return null;
            }
            if(CollectionUtils.isEmpty(batchTimeList)){
                return null;
            }
            if(batchTimeList.size() > 10000){
                batchTimeList = batchTimeList.subList(0,10000);
            }
            List<StatValue> values = ResultStorageProxy.queryWithDimens(statExtEntity, dimens,batchTimeList);
            if(CollectionUtils.isNotEmpty(values)){
                result = JsonUtil.toJSONString(values);
            }
            if(logger.isDebugEnabled()){
                logger.debug("data query with batch list,statId:{},dimens:{},result:{}",statId,dimens,result);
            }
        }catch (Exception ex){
            logger.error("dataQueryWithBatchList error!",ex);
            return "System Error!";
        }
        return result;
    }

    @Override
    public String dataQueryWithDimensList(int statId, List<String> dimensList, long batchTime, Current __current) {
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        if(statExtEntity == null){
            return null;
        }
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryById(statExtEntity.getGroupId());
        if(groupExtEntity == null){
            return null;
        }
        if(CollectionUtils.isEmpty(dimensList)){
            return null;
        }
        if(dimensList.size() > 10000){
            dimensList = dimensList.subList(0,10000);
        }
        Map<String, StatValue> values;
        try{
            values = ResultStorageProxy.queryWithDimensList(statExtEntity, dimensList,batchTime);
        }catch (Exception ex){
            logger.error("dataQueryWithDimensList error!",ex);
            return "System Error!";
        }
        String result = null;
        if(MapUtils.isNotEmpty(values)){
            result = JsonUtil.toJSONString(values.keySet().stream().map(values::get).collect(Collectors.toList()));
        }
        if(logger.isDebugEnabled()){
            logger.debug("data query with dimensList list,statId:{},dimens size:{},result:{}",statId,dimensList.size(),result);
        }
        return result;
    }

    @Override
    public List<String> queryDimens(String token, String dimens,String lastDimens, int limit, Current __current) {
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryByToken(token);
        if(groupExtEntity == null){
            return null;
        }
        if(limit < 0){
            return null;
        }else if(limit > StatConst.DIMENS_THRESHOLD_LIMIT_COUNT){
            limit = StatConst.DIMENS_THRESHOLD_LIMIT_COUNT;
        }
        return DimensDBWrapper.loadDimension(token,dimens,lastDimens,limit);
    }
}
