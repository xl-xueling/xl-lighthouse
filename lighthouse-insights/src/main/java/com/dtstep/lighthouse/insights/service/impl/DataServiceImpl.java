package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.common.modal.StatDataObject;
import com.dtstep.lighthouse.core.storage.result.ResultStorageSelector;
import com.dtstep.lighthouse.insights.service.DataService;
import com.dtstep.lighthouse.insights.service.StatService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

@Service
public class DataServiceImpl implements DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

    @Override
    public List<String> dimensArrangement(StatExtEntity statExtEntity, LinkedHashMap<String, String[]> dimensParams) throws Exception {
        String[] dimensArray = statExtEntity.getTemplateEntity().getDimensArray();
        if(ArrayUtils.isEmpty(dimensArray)){
            return null;
        }
        List<String> dimensSortList = Arrays.asList(dimensArray);
        List<Map.Entry<String, String[]>> list = new ArrayList<>(dimensParams.entrySet());
        list.sort(new CustomComparator(dimensSortList));
        LinkedHashMap<String, String[]> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        String[][] array = sortedMap.values().toArray(new String[0][0]);
        return arrangement(array);
    }

    private static class CustomComparator implements Comparator<Map.Entry<String, String[]>> {

        private final List<String> dimensSortList;

        public CustomComparator(List<String> dimensSortList){
            this.dimensSortList = dimensSortList;
        }

        @Override
        public int compare(Map.Entry<String, String[]> o1, Map.Entry<String, String[]> o2) {
            return dimensSortList.indexOf(o1.getKey()) - dimensSortList.indexOf(o2.getKey());
        }
    }

    public List<String> arrangement(String[]... datas) {
        List<String> result = new ArrayList<>();
        beArrangement(result, "", datas, 0);
        return result;
    }

    private void beArrangement(List<String> result, String current, String[][] lists, int index) {
        if (index == lists.length) {
            result.add(current);
            return;
        }
        for (String item : lists[index]) {
            beArrangement(result, current + (current.isEmpty() ? "" : ";") + item, lists, index + 1);
        }
    }

    @Override
    public List<StatDataObject> dataQuery(StatExtEntity statExtEntity, LocalDateTime startTime, LocalDateTime endTime, List<String> dimensList) throws Exception {
        long startTimeStamp = DateUtil.translateToTimeStamp(startTime);
        long endTimeStamp = DateUtil.translateToTimeStamp(endTime);
        List<Long> batchList = null;
        try{
            batchList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeparam(), startTimeStamp, endTimeStamp);
        }catch (Exception ex){
            logger.error("query batch time list error!",ex);
        }
        Validate.notNull(batchList);

        Map<String,List<StatValue>> valuesMap = ResultStorageSelector.queryWithDimensList(statExtEntity,dimensList,batchList);
        List<StatDataObject> dataObjects = new ArrayList<>();
        if(MapUtils.isNotEmpty(valuesMap)){
            for(String dimensValue : valuesMap.keySet()){
                StatDataObject dataObject = new StatDataObject();
                List<StatValue> valueList = valuesMap.get(dimensValue);
                dataObject.setDimensValue(dimensValue);
                dataObject.setValuesList(valueList);
                dataObject.setStatId(statExtEntity.getId());
                dataObjects.add(dataObject);
            }
        }
        System.out.println("dataObjects is:" + JsonUtil.toJSONString(dataObjects));
        return dataObjects;
    }

    @Override
    public List<StatDataObject> testDataQuery(StatExtEntity statExtEntity, LocalDateTime startTime, LocalDateTime endTime, List<String> dimensList) throws Exception{
        long startTimeStamp = DateUtil.translateToTimeStamp(startTime);
        long endTimeStamp = DateUtil.translateToTimeStamp(endTime);
        List<Long> batchList = null;
        try{
            batchList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeparam(), startTimeStamp, endTimeStamp);
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
            statDataObject.setStatId(statExtEntity.getId());
            statDataObject.setDimensValue(dimens + "-"+ThreadLocalRandom.current().nextInt(10));
            statDataObject.setValuesList(statValues);
            objectList.add(statDataObject);
        }
        return objectList;
    }
}
