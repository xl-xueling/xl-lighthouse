package com.dtstep.lighthouse.core.storage.result;

import com.dtstep.lighthouse.common.entity.calculate.MicroBucket;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;

import java.util.LinkedHashMap;
import java.util.List;

public class DefaultResultStorageHandler implements ResultStorageHandler<MicroBucket, StatValue>{

    @Override
    public void increment(List<MicroBucket> list) throws Exception {

    }

    @Override
    public void maxPut(List<MicroBucket> list) throws Exception {

    }

    @Override
    public void minPut(List<MicroBucket> list) throws Exception {

    }

    @Override
    public void put(List<MicroBucket> list) throws Exception {

    }

    @Override
    public StatValue queryWithDimens(StatExtEntity statExtEntity, String dimensValue, long batchTime) throws Exception {
        return null;
    }

    @Override
    public List<StatValue> queryWithDimens(StatExtEntity statExtEntity, String dimensValue, List<Long> batchTimeList) throws Exception {
        return null;
    }

    @Override
    public List<StatValue> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, List<Long> batchTimeList) throws Exception {
        return null;
    }

    @Override
    public LinkedHashMap<String, StatValue> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, long batchTime) throws Exception {
        return null;
    }
}
