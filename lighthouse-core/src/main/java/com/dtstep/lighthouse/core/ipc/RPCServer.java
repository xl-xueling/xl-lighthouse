package com.dtstep.lighthouse.core.ipc;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;

import java.util.List;
import java.util.Map;

public interface RPCServer {

    GroupVerifyEntity queryGroupInfo(String token) throws Exception;

    StatVerifyEntity queryStatInfo(int id) throws Exception;

    void process(byte[] bytes) throws Exception;

    List<StatValue> dataQuery(int statId, String dimensValue, List<Long> batchTime) throws Exception;

    List<StatValue> dataDurationQuery(int statId, String dimensValue, long startTime, long endTime) throws Exception;

    Map<String,List<StatValue>> dataQueryWithDimensList(int statId,List<String> dimensValueList,List<Long> batchTime) throws Exception;

    Map<String,List<StatValue>> dataDurationQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime) throws Exception;

    List<LimitValue> limitQuery(int statId, long batchTime) throws Exception;
}
