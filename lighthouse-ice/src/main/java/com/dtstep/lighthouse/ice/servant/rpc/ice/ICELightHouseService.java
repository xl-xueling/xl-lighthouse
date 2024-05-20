package com.dtstep.lighthouse.ice.servant.rpc.ice;

import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.ice.RemoteLightServer;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import com.zeroc.IceBox.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ICELightHouseService implements Service {

    private static final Logger logger = LoggerFactory.getLogger(ICELightHouseService.class);

    @Override
    public void start(String s, Communicator communicator, String[] strings) {
        try{
            LDPConfig.loadConfiguration();
        }catch (Exception ex){
            logger.error("ice server start error,system initialization error!",ex);
            throw new InitializationException();
        }
        ObjectAdapter adapter = communicator.createObjectAdapter(s);
        communicator.getProperties().setProperty("Ice.MessageSizeMax", "1409600");
        RemoteLightServer servant = new ICERemoteLightServerImpl();
        adapter.add(servant, Util.stringToIdentity("LightHouseServiceIdentity"));
        adapter.activate();
        System.out.println("lighthouse server start success!");
    }

    @Override
    public void stop() {}
}
