package com.dtstep.lighthouse.standalone.rpc.provider;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.ice.LightRpcException;
import com.dtstep.lighthouse.core.ipc.RPCServer;
import com.dtstep.lighthouse.core.ipc.impl.RPCServerImpl;
import com.dtstep.lighthouse.common.rpc.netty.provider.StandRemoteLightServerPrx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class StandaloneRemoteServiceImpl implements StandRemoteLightServerPrx {

    private static final Logger logger = LoggerFactory.getLogger(StandaloneRemoteServiceImpl.class);

    private static final RPCServer rpc = new RPCServerImpl();

    @Override
    public void process(byte[] bytes) throws LightRpcException {
        try{
            rpc.process(bytes);
        }catch (Exception ex){
            logger.error("process message error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public GroupVerifyEntity queryGroupInfo(String token) throws LightRpcException {
        try{
            return rpc.queryGroupInfo(token);
        }catch (Exception ex){
            logger.error("query group error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public StatVerifyEntity queryStatInfo(int id) throws LightRpcException {
        try{
            return rpc.queryStatInfo(id);
        }catch (Exception ex){
            logger.error("query stat error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public List<StatValue> dataQuery(int statId, String dimensValue, long startTime, long endTime) throws LightRpcException {
        return null;
    }

    @Override
    public List<StatValue> dataQuery(int statId, String dimensValue, List<Long> batchTime) throws LightRpcException {
        return null;
    }

    @Override
    public Map<String, List<StatValue>> dataQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime) throws LightRpcException {
        return null;
    }

    @Override
    public Map<String, List<StatValue>> dataQueryWithDimensList(int statId, List<String> dimensValueList, List<Long> batchTime) throws LightRpcException {
        return null;
    }

    @Override
    public List<LimitValue> limitQuery(int statId, long batchTime) throws LightRpcException {
        return null;
    }
}
