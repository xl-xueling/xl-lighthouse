package com.dtstep.lighthouse.client.rpc.ice;

import com.dtstep.lighthouse.common.ice.RemoteLightServerPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectPrx;

import java.util.UUID;

public class ICEHandler {

    private static RemoteLightServerPrx remoteLightServerPrx;

    public static RemoteLightServerPrx getRemotePrx(Communicator ic){
        if(remoteLightServerPrx == null){
            ObjectPrx auxBasePrx = ic.stringToProxy("LightHouseServiceIdentity").ice_connectionId(UUID.randomUUID().toString()).ice_locatorCacheTimeout(1200);
            remoteLightServerPrx = RemoteLightServerPrx.checkedCast(auxBasePrx);
        }
        return remoteLightServerPrx;
    }
}
