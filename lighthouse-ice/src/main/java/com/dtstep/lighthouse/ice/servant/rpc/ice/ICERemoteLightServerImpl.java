package com.dtstep.lighthouse.ice.servant.rpc.ice;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.ice.LightRpcException;
import com.dtstep.lighthouse.common.ice.RemoteLightServer;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.SerializeUtil;
import com.dtstep.lighthouse.ice.servant.logic.RPCServer;
import com.dtstep.lighthouse.ice.servant.logic.impl.RPCServerImpl;
import com.zeroc.Ice.Current;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ICERemoteLightServerImpl implements RemoteLightServer {

    private static final Logger logger = LoggerFactory.getLogger(ICERemoteLightServerImpl.class);

    private static final RPCServer rpc = new RPCServerImpl();

    @Override
    public byte[] process(byte[] message, Current current) throws LightRpcException{
        try{
            rpc.process(message);
        }catch (Exception ex){
            logger.error("process message error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
        return null;
    }

    @Override
    public byte[] queryGroupInfo(String token, Current current) throws LightRpcException{
        GroupVerifyEntity groupVerifyEntity;
        try{
            groupVerifyEntity = rpc.queryGroup(token);
        }catch (Exception ex){
            logger.error("query group info error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
        return SerializeUtil.serialize(groupVerifyEntity);
    }

    @Override
    public byte[] queryStatInfo(int id, Current current) throws LightRpcException {
        StatVerifyEntity statVerifyEntity;
        try{
            statVerifyEntity = rpc.queryStat(id);
        }catch (Exception ex){
            logger.error("query stat info error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
        return SerializeUtil.serialize(statVerifyEntity);
    }

    @Override
    public byte[] dataDurationQuery(int statId, String dimensValue, long startTime, long endTime, Current current) throws LightRpcException {
        List<StatValue> statValues;
        try{
            statValues = rpc.dataQuery(statId,dimensValue,startTime,endTime);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        return SerializeUtil.serialize(statValues);
    }

    @Override
    public byte[] dataQuery(int statId, String dimensValue, List<Long> batchList, Current current) throws LightRpcException {
        List<StatValue> statValues;
        try{
            statValues = rpc.dataQuery(statId,dimensValue,batchList);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        return SerializeUtil.serialize(statValues);
    }

    @Override
    public byte[] dataDurationQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime, Current current) throws LightRpcException{
        Map<String,List<StatValue>> statValues;
        try{
            statValues = rpc.dataQueryWithDimensList(statId,dimensValueList,startTime,endTime);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        return SerializeUtil.serialize(statValues);
    }

    @Override
    public byte[] dataQueryWithDimensList(int statId, List<String> dimensValueList, List<Long> batchList, Current current) throws LightRpcException{
        Map<String,List<StatValue>> statValues;
        try{
            statValues = rpc.dataQueryWithDimensList(statId,dimensValueList,batchList);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        return SerializeUtil.serialize(statValues);
    }

    @Override
    public byte[] limitQuery(int statId, long batchTime, Current current) throws LightRpcException{
        List<LimitValue> limitValues;
        try{
            limitValues = rpc.limitQuery(statId,batchTime);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        return SerializeUtil.serialize(limitValues);
    }
}
