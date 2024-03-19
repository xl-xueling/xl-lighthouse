package com.dtstep.lighthouse.insights.service.impl;

import com.clearspring.analytics.util.Lists;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.modal.LimitDataObject;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.common.modal.StatDataObject;
import com.dtstep.lighthouse.core.storage.limit.LimitStorageSelector;
import com.dtstep.lighthouse.core.storage.result.ResultStorageSelector;
import com.dtstep.lighthouse.insights.service.DataService;
import com.dtstep.lighthouse.insights.service.StatService;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

    @Override
    public List<String> dimensArrangement(StatExtEntity statExtEntity, LinkedHashMap<String, String[]> dimensParams) throws Exception {
        String[] dimensArray = statExtEntity.getTemplateEntity().getDimensArray();
        if(ArrayUtils.isEmpty(dimensArray)){
            return null;
        }
        if(MapUtils.isEmpty(dimensParams)){
            return null;
        }
        List<String> destSortedList = Arrays.asList(dimensArray);
        List<String> originSortedList = new ArrayList<>();
        for(String dimens : dimensParams.keySet()){
            if(!dimens.contains(";")){
                originSortedList.add(dimens);
            }else{
                String [] arr = dimens.split(";");
                originSortedList.addAll(Arrays.asList(arr));
            }
        }
        String[][] valuesArray = dimensParams.values().toArray(new String[0][0]);
        List<String> unSortedList = arrangement(valuesArray);
        return unSortedList.stream().map(z -> {
            String[] arr = z.split(";");
            DimensEntity[] entity = new DimensEntity[arr.length];
            for(int i=0;i<arr.length;i++){
                DimensEntity pair = new DimensEntity(originSortedList.get(i), arr[i]);
                entity[i] = pair;
            }
            List<DimensEntity> sortedList = Arrays.stream(entity).sorted(new CustomComparator(destSortedList)).collect(Collectors.toList());
            return sortedList.stream()
                    .map(DimensEntity::getValue)
                    .collect(Collectors.joining(";"));
        }).collect(Collectors.toList());
    }

    private static class DimensEntity {

        private final String dimens;

        private final String value;

        public DimensEntity(String dimens, String value){
            this.dimens = dimens;
            this.value = value;
        }
        public String getDimens() {
            return dimens;
        }

        public String getValue() {
            return value;
        }
    }

    private static class CustomComparator implements Comparator<DimensEntity> {

        private final List<String> dimensSortList;

        public CustomComparator(List<String> dimensSortList){
            this.dimensSortList = dimensSortList;
        }

        @Override
        public int compare(DimensEntity o1, DimensEntity o2) {
            return dimensSortList.indexOf(o1.getDimens()) - dimensSortList.indexOf(o2.getDimens());
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
                dataObject.setDisplayDimensValue(dimensValue);
                dataObject.setValuesList(valueList);
                dataObject.setStatId(statExtEntity.getId());
                dataObjects.add(dataObject);
            }
        }
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
        List<String> dimensLists = List.of("province");
        for(String dimens:dimensLists){
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

    @Override
    public List<LimitDataObject> limitQuery(StatExtEntity statExtEntity, List<Long> batchTimeList) throws Exception {
        List<LimitDataObject> resultList = Lists.newArrayList();
        if(CollectionUtils.isEmpty(batchTimeList)){
            return resultList;
        }
        for(Long batchTime : batchTimeList){
            List<LimitValue> valueList = LimitStorageSelector.query(statExtEntity,batchTime);
            LimitDataObject dataObject = new LimitDataObject();
            dataObject.setValues(valueList);
            dataObject.setBatchTime(batchTime);
            dataObject.setDisplayBatchTime(BatchAdapter.dateTimeFormat(statExtEntity.getTimeparam(),batchTime));
            resultList.add(dataObject);
        }
        return resultList;
    }
}
