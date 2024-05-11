package com.dtstep.lighthouse.client.rpc.standalone;

import com.dtstep.lighthouse.common.rpc.netty.RpcClientStarter;
import com.dtstep.lighthouse.common.rpc.netty.provider.StandRemoteLightServerPrx;

public class StandaloneHandler {

    private static StandRemoteLightServerPrx standaloneRemoteService;

    public static StandRemoteLightServerPrx getRemoteProxy(){
        if(standaloneRemoteService == null){
            RpcClientStarter client = new RpcClientStarter();
            standaloneRemoteService = client.create(StandRemoteLightServerPrx.class);
        }
        return standaloneRemoteService;
    }
}
