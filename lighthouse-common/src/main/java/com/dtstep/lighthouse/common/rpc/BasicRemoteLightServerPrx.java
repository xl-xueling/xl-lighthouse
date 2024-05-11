package com.dtstep.lighthouse.common.rpc;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.ice.LightRpcException;

import java.util.List;
import java.util.Map;

public interface BasicRemoteLightServerPrx {

    GroupVerifyEntity queryGroupInfo(String token) throws LightRpcException;

    StatVerifyEntity queryStatInfo(int id) throws LightRpcException;

    void process(byte[] bytes) throws LightRpcException;

    List<StatValue> dataQuery(int statId, String dimensValue, List<Long> batchTime) throws LightRpcException;

    List<StatValue> dataDurationQuery(int statId, String dimensValue, long startTime, long endTime) throws LightRpcException;

    Map<String,List<StatValue>> dataQueryWithDimensList(int statId,List<String> dimensValueList,List<Long> batchTime) throws LightRpcException;

    Map<String,List<StatValue>> dataDurationQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime) throws LightRpcException;

    List<LimitValue> limitQuery(int statId, long batchTime) throws LightRpcException;
}
