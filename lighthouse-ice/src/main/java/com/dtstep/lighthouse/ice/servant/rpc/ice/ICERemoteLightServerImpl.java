package com.dtstep.lighthouse.ice.servant.rpc.ice;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.ice.LightRpcException;
import com.dtstep.lighthouse.common.ice.RemoteLightServer;
import com.dtstep.lighthouse.common.serializer.SerializerProxy;
import com.dtstep.lighthouse.core.ipc.RPCServer;
import com.dtstep.lighthouse.core.ipc.impl.RPCServerImpl;
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
            groupVerifyEntity = rpc.queryGroupInfo(token);
        }catch (Exception ex){
            logger.error("query group info error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
        if(groupVerifyEntity == null){
            return null;
        }
        try{
            return SerializerProxy.instance().serialize(groupVerifyEntity);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] queryStatInfo(int id, Current current) throws LightRpcException {
        StatVerifyEntity statVerifyEntity;
        try{
            statVerifyEntity = rpc.queryStatInfo(id);
        }catch (Exception ex){
            logger.error("query stat info error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
        if(statVerifyEntity == null){
            return null;
        }
        try{
            return SerializerProxy.instance().serialize(statVerifyEntity);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] dataQuery(int statId, String dimensValue, List<Long> batchList, Current current) throws LightRpcException {
        List<StatValue> statValues;
        try{
            statValues = rpc.dataQuery(statId,dimensValue,batchList);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(statValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] dataDurationQuery(int statId, String dimensValue, long startTime, long endTime, Current current) throws LightRpcException {
        List<StatValue> statValues;
        try{
            statValues = rpc.dataDurationQuery(statId,dimensValue,startTime,endTime);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(statValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] dataQueryWithDimensList(int statId, List<String> dimensValueList, List<Long> batchList, Current current) throws LightRpcException{
        Map<String,List<StatValue>> statValues;
        try{
            statValues = rpc.dataQueryWithDimensList(statId,dimensValueList,batchList);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(statValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] dataDurationQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime, Current current) throws LightRpcException{
        Map<String,List<StatValue>> statValues;
        try{
            statValues = rpc.dataDurationQueryWithDimensList(statId,dimensValueList,startTime,endTime);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(statValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] limitQuery(int statId, long batchTime, Current current) throws LightRpcException{
        List<LimitValue> limitValues;
        try{
            limitValues = rpc.limitQuery(statId,batchTime);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(limitValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }
}
