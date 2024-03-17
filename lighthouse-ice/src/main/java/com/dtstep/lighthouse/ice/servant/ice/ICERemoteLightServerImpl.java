package com.dtstep.lighthouse.ice.servant.ice;

import com.dtstep.lighthouse.client.rpc.ice.ICERPCClientImpl;
import com.dtstep.lighthouse.common.ice.RemoteLightServer;
import com.dtstep.lighthouse.ice.servant.logic.RPCServer;
import com.dtstep.lighthouse.ice.servant.logic.impl.RPCServerImpl;
import com.zeroc.Ice.Current;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ICERemoteLightServerImpl implements RemoteLightServer {

    private static final Logger logger = LoggerFactory.getLogger(ICERemoteLightServerImpl.class);

    private static final RPCServer rpc = new RPCServerImpl();

    @Override
    public String process(byte[] message, Current current){
        try{
            rpc.process(message);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String queryGroupInfo(String token, Current current) {
        return "ssss";
    }
}
