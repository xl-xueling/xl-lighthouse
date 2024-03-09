package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.common.modal.StatDataObject;
import com.dtstep.lighthouse.core.storage.result.ResultStorageSelector;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.dtstep.lighthouse.insights.vo.StatVO;
import com.dtstep.lighthouse.insights.service.DataService;
import com.dtstep.lighthouse.insights.service.StatService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataServiceImpl implements DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

    @Autowired
    private StatService statService;

    @Override
    public List<StatDataObject> dataQuery(Integer statId, LocalDateTime startTime, LocalDateTime endTime, List<String> dimensList) throws Exception {
        Stat stat = statService.queryById(statId);
        long startTimeStamp = DateUtil.translateToTimeStamp(startTime);
        long endTimeStamp = DateUtil.translateToTimeStamp(endTime);
        List<Long> batchList = null;
        try{
            batchList = BatchAdapter.queryBatchTimeList(stat.getTimeparam(), startTimeStamp, endTimeStamp);
        }catch (Exception ex){
            logger.error("query batch time list error!",ex);
        }
        Validate.notNull(batchList);
        StatExtEntity statExtEntity = null;
        try{
            statExtEntity = StatDBWrapper.combineExtInfo(stat,false);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Map<String,List<StatValue>> valuesMap = ResultStorageSelector.queryWithDimensList(statExtEntity,dimensList,batchList);
        List<StatDataObject> dataObjects = new ArrayList<>();
        if(MapUtils.isNotEmpty(valuesMap)){
            for(String dimensValue : valuesMap.keySet()){
                StatDataObject dataObject = new StatDataObject();
                List<StatValue> valueList = valuesMap.get(dimensValue);
                dataObject.setDimensValue(dimensValue);
                dataObject.setValuesList(valueList);
                dataObject.setStatId(statId);
                dataObjects.add(dataObject);
            }
        }
        System.out.println("dataObjects is:" + JsonUtil.toJSONString(dataObjects));
        return dataObjects;
    }

    @Override
    public List<StatDataObject> testDataQuery(Integer statId, LocalDateTime startTime, LocalDateTime endTime, List<String> dimensList) throws Exception{
        StatVO statVO = statService.queryById(statId);
        long startTimeStamp = DateUtil.translateToTimeStamp(startTime);
        long endTimeStamp = DateUtil.translateToTimeStamp(endTime);
        List<Long> batchList = null;
        try{
            batchList = BatchAdapter.queryBatchTimeList(statVO.getTimeparam(), startTimeStamp, endTimeStamp);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Validate.notNull(batchList);
        List<StatValue> statValues = new ArrayList<>();
        List<StatDataObject> objectList = new ArrayList<>();
        List<String> dimenslists = List.of("province");
        for(String dimens:dimenslists){
            for(long batchTime:batchList){
                StatValue statValue = new StatValue();
                statValue.setValue(ThreadLocalRandom.current().nextInt(10000));
                statValue.setBatchTime(batchTime);
                statValue.setDisplayBatchTime(DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"));
                statValues.add(statValue);
            }
            StatDataObject statDataObject = new StatDataObject();
            statDataObject.setStatId(statId);
            statDataObject.setDimensValue(dimens + "-"+ThreadLocalRandom.current().nextInt(10));
            statDataObject.setValuesList(statValues);
            objectList.add(statDataObject);
        }
        return objectList;
    }
}
