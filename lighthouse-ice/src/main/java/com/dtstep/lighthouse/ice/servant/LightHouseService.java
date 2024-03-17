package com.dtstep.lighthouse.ice.servant;


import com.dtstep.lighthouse.common.ice.RemoteLightServer;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import com.zeroc.IceBox.Service;

public class LightHouseService implements Service {

    @Override
    public void start(String s, Communicator communicator, String[] strings) {
        ObjectAdapter adapter = communicator.createObjectAdapter(s);
        communicator.getProperties().setProperty("Ice.MessageSizeMax", "1409600");
        RemoteLightServer servant = new RemoteLightServerImpl();
        adapter.add(servant, Util.stringToIdentity("LightHouseServiceIdentity"));
        adapter.activate();
        System.out.println("lighthouse server start success!");
    }

    @Override
    public void stop() {

    }
}
