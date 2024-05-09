package com.dtstep.lighthouse.standalone.rpc;

import com.dtstep.lighthouse.standalone.rpc.provider.IRpcService;

public class RpcClientStarter {
    public static void main(String[] args) {
        IRpcService rpc = com.dtstep.lighthouse.standalone.rpc.RpcProxy.create(IRpcService.class);
        System.out.println("8 + 2 = " + rpc.add(8,2));
    }
}
