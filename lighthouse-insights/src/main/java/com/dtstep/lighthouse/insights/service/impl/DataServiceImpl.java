package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.insights.dto_bak.StatDataObject;
import com.dtstep.lighthouse.insights.vo.StatVO;
import com.dtstep.lighthouse.insights.dto_bak.StatValue;
import com.dtstep.lighthouse.insights.service.DataService;
import com.dtstep.lighthouse.insights.service.StatService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private StatService statService;

    @Override
    public List<StatDataObject> dataQuery(Integer statId, LocalDateTime startTime, LocalDateTime endTime, List<String> dimens) {
        return null;
    }

    @Override
    public List<StatDataObject> testDataQuery(Integer statId, LocalDateTime startTime, LocalDateTime endTime, List<String> dimensList) {
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
            statDataObject.setDimens(dimens);
            statDataObject.setDimensValue(dimens + "-"+ThreadLocalRandom.current().nextInt(10));
            statDataObject.setValuesList(statValues);
            objectList.add(statDataObject);
        }
        return objectList;
    }
}
