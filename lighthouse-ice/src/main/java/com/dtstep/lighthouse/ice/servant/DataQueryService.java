package com.dtstep.lighthouse.ice.servant;

import Ice.Communicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DataQueryService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(DataQueryService.class);

    private Ice.ObjectAdapter adapter;

    @Override
    public void start(String name, Communicator ic, String[] strings) {
        super.init();
        adapter = ic.createObjectAdapter(name);
        ic.getProperties().setProperty("Ice.MessageSizeMax", "1409600");
        Ice.Object object = new DataQueryI();
        adapter.add(object,ic.stringToIdentity("identity_dataquery"));
        adapter.activate();
        logger.info("ice server(data query) start success!");
    }

    @Override
    public void stop() {
        if(adapter != null){
            logger.info("call stop servant:{data query service}!");
            adapter.destroy();
        }
    }
}
