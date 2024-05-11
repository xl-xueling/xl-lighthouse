package com.dtstep.lighthouse.client.rpc.standalone;

import com.dtstep.lighthouse.client.rpc.RPCClient;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.rpc.netty.provider.StandRemoteLightServerPrx;

import java.util.List;
import java.util.Map;

public class StandaloneClientImpl implements RPCClient {

    @Override
    public boolean init(String configuration) throws Exception {
        return false;
    }

    @Override
    public void reconnect() throws Exception {

    }

    @Override
    public void send(String text) throws Exception {

    }

    @Override
    public GroupVerifyEntity queryGroup(String token) throws Exception {
        StandRemoteLightServerPrx standaloneRemoteService = StandaloneHandler.getRemoteProxy();
        return standaloneRemoteService.queryGroupInfo(token);
    }

    @Override
    public StatVerifyEntity queryStat(int id) throws Exception {
        return null;
    }

    @Override
    public List<StatValue> dataQuery(int statId, String dimensValue, List<Long> batchList) throws Exception {
        return null;
    }

    @Override
    public List<StatValue> dataDurationQuery(int statId, String dimensValue, long startTime, long endTime) throws Exception {
        return null;
    }

    @Override
    public Map<String, List<StatValue>> dataQueryWithDimensList(int statId, List<String> dimensValueList, List<Long> batchList) throws Exception {
        return null;
    }

    @Override
    public Map<String, List<StatValue>> dataDurationQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime) throws Exception {
        return null;
    }

    @Override
    public List<LimitValue> limitQuery(int statId, long batchTime) throws Exception {
        return null;
    }
}
