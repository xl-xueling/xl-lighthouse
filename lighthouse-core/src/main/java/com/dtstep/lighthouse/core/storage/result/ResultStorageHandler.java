package com.dtstep.lighthouse.core.storage.result;

import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;

import java.util.LinkedHashMap;
import java.util.List;

public interface ResultStorageHandler<W,R> {

    void increment(List<W> list) throws Exception;

    void put(List<W> list) throws Exception;

    void maxPut(List<W> list) throws Exception;

    void minPut(List<W> list) throws Exception;

    R query(StatExtEntity statExtEntity, String dimensValue, long batchTime) throws Exception;

    List<R> query(StatExtEntity statExtEntity, List<String> dimensValueList, List<Long> batchTimeList) throws Exception;

}
