package com.dtstep.lighthouse.ice.servant.logic;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;

import java.util.List;
import java.util.Map;

public interface RPCServer {

    GroupVerifyEntity queryGroup(String token) throws Exception;

    StatVerifyEntity queryStat(int id) throws Exception;

    void process(byte[] bytes) throws Exception;

    List<StatValue> dataQuery(int statId,String dimensValue, long startTime, long endTime) throws Exception;

    List<StatValue> dataQuery(int statId,String dimensValue,List<Long> batchTime) throws Exception;

    Map<String,List<StatValue>> dataQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime) throws Exception;

    Map<String,List<StatValue>> dataQueryWithDimensList(int statId,List<String> dimensValueList,List<Long> batchTime) throws Exception;

    List<LimitValue> limitQuery(int statId, long batchTime) throws Exception;
}
