package com.dtstep.lighthouse.client.rpc;

import com.dtstep.lighthouse.client.rpc.ice.ICERPCClientImpl;

public class RPCClientProxy {

    private static final RPCClient instance = new ICERPCClientImpl();

    public static RPCClient instance(){
        return instance;
    }
}
