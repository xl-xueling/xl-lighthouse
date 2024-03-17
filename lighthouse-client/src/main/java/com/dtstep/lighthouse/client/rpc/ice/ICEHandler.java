package com.dtstep.lighthouse.client.rpc.ice;

import com.dtstep.lighthouse.common.ice.AuxInterfacePrx;
import com.dtstep.lighthouse.common.ice.AuxInterfacePrxHelper;
import com.dtstep.lighthouse.common.ice.ReceiverInterfacePrx;
import com.dtstep.lighthouse.common.ice.ReceiverInterfacePrxHelper;

import java.util.UUID;

public class ICEHandler {

    private static AuxInterfacePrx auxInterfacePrx;

    private static ReceiverInterfacePrx receiverInterfacePrx;

    public static AuxInterfacePrx getAuxInterfacePrx(Ice.Communicator ic){
        if(auxInterfacePrx == null){
            Ice.ObjectPrx auxBasePrx = ic.stringToProxy("identity_aux").ice_connectionId(UUID.randomUUID().toString()).ice_locatorCacheTimeout(1200);
            auxInterfacePrx = AuxInterfacePrxHelper.checkedCast(auxBasePrx);
        }
        return auxInterfacePrx;
    }

    public static ReceiverInterfacePrx getReceiverInterfacePrx(Ice.Communicator ic){
        if(receiverInterfacePrx == null){
            Ice.ObjectPrx receiverBasePrx = ic.stringToProxy("identity_receiver").ice_connectionId(UUID.randomUUID().toString()).ice_locatorCacheTimeout(1200);
            receiverInterfacePrx = ReceiverInterfacePrxHelper.checkedCast(receiverBasePrx);
        }
        return receiverInterfacePrx;
    }

}
