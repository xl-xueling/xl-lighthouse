package com.dtstep.lighthouse.ice.servant;

import com.dtstep.lighthouse.common.ice.RemoteLightServer;
import com.zeroc.Ice.Current;

public class RemoteLightServerImpl implements RemoteLightServer {

    @Override
    public String process(byte[] message, Current current) {
        System.out.println("process message...");
        return null;
    }

    @Override
    public String queryGroupInfo(String token, Current current) {
        System.out.println("----Token is:" + token);
        return null;
    }
}
