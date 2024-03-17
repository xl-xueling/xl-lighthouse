package com.dtstep.lighthouse.client.rpc.ice;

import com.dtstep.lighthouse.client.rpc.RPCClient;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.ice.AuxInterfacePrx;
import com.dtstep.lighthouse.common.ice.ReceiverInterfacePrx;
import com.dtstep.lighthouse.common.util.SnappyUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

public class ICERPCClientImpl implements RPCClient {

    private static final Logger logger = LoggerFactory.getLogger(ICERPCClientImpl.class);

    private static String[] initParams;

    private static Ice.Communicator ic;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean init(String locators) throws Exception {
        if(StringUtil.isEmpty(locators)){
            throw new InitializationException("lighthouse client init failed,locators cannot be empty!");
        }
        StringBuilder locatorSbr = new StringBuilder();
        try {
            String[] locatorArr = locators.split(",");
            for (String conf : locatorArr) {
                String[] arr = conf.split(":");
                String ip = arr[0];
                String port = arr[1];
                locatorSbr.append(":").append("tcp -h ").append(ip).append(" -p ").append(port);
            }
        }catch (Exception ex){
            throw new InitializationException("lighthouse client init failed,locators format error!");
        }
        try {
            String cfg = String.format("--Ice.Default.Locator=LightHouseIceGrid/Locator %s -z", locatorSbr.toString());
            initParams = new String[]{cfg};
            Ice.Properties iceProperties = Ice.Util.createProperties();
            iceProperties.setProperty("Ice.Override.ConnectTimeout", "5000");
            iceProperties.setProperty("Ice.RetryIntervals", "10");
            iceProperties.setProperty("Ice.RetryOnConnectTimeout", "1");
            iceProperties.setProperty("Ice.ThreadPool.Client.Size", "50");
            iceProperties.setProperty("Ice.ThreadPool.Client.SizeMax", "300");
            iceProperties.setProperty("Ice.MessageSizeMax", "1409600");
            Ice.InitializationData initData = new Ice.InitializationData();
            initData.properties = iceProperties;
            ic = Ice.Util.initialize(initParams, initData);
            logger.info("lighthouse client init success!");
        }catch (Exception ex){
            throw new InitializationException(String.format("lighthouse remote service not available,locators:%s",locators));
        }
        return true;
    }

    @Override
    public GroupVerifyEntity queryGroup(String token) throws Exception {
        AuxInterfacePrx auxInterfacePrx = ICEHandler.getAuxInterfacePrx(ic);
        String str = auxInterfacePrx.queryGroupByToken(token);
        GroupVerifyEntity groupVerifyEntity = null;
        if(!StringUtil.isEmpty(str)){
            groupVerifyEntity = objectMapper.readValue(str, GroupVerifyEntity.class);
        }
        return groupVerifyEntity;
    }

    @Override
    public void send(String text) throws Exception {
        byte[] bytes;
        if(text.length() < SysConst._COMPRESS_THRESHOLD_SIZE){
            bytes = text.getBytes(StandardCharsets.UTF_8);
        }else{
            bytes = SnappyUtil.compressToByte(text);
        }
        ReceiverInterfacePrx receiverInterfacePrx = ICEHandler.getReceiverInterfacePrx(ic);
        receiverInterfacePrx.logic(bytes);
    }


    @Override
    public void reconnect() throws Exception {}
}
