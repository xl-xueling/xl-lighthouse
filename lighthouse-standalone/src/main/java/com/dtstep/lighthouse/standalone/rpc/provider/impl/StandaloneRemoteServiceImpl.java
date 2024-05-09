package com.dtstep.lighthouse.standalone.rpc.provider.impl;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.ice.LightRpcException;
import com.dtstep.lighthouse.core.ipc.RPCServer;
import com.dtstep.lighthouse.core.ipc.impl.RPCServerImpl;
import com.dtstep.lighthouse.standalone.rpc.provider.StandaloneRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandaloneRemoteServiceImpl implements StandaloneRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(StandaloneRemoteServiceImpl.class);

    private static final RPCServer rpc = new RPCServerImpl();

    @Override
    public void process(byte[] bytes) throws LightRpcException {

    }

    @Override
    public GroupVerifyEntity queryGroupInfo(String token) throws LightRpcException {
        try{
            return rpc.queryGroup(token);
        }catch (Exception ex){
            logger.error("query group error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public StatVerifyEntity queryStatInfo(int id) throws LightRpcException {
        try{
            return rpc.queryStat(id);
        }catch (Exception ex){
            logger.error("query stat error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }
}
