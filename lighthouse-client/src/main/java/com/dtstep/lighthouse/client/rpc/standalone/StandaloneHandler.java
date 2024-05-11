package com.dtstep.lighthouse.client.rpc.standalone;

import com.dtstep.lighthouse.common.rpc.netty.NettyClientAdapter;
import com.dtstep.lighthouse.common.rpc.BasicRemoteLightServerPrx;

public class StandaloneHandler {

    private static BasicRemoteLightServerPrx standaloneRemoteService;

    public static BasicRemoteLightServerPrx getRemoteProxy(){
        if(standaloneRemoteService == null){
            NettyClientAdapter client = new NettyClientAdapter();
            standaloneRemoteService = client.create(BasicRemoteLightServerPrx.class);
        }
        return standaloneRemoteService;
    }
}
