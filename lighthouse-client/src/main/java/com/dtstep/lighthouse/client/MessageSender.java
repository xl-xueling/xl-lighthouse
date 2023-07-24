package com.dtstep.lighthouse.client;

import Ice.Communicator;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.enums.fusing.FusingRules;
import com.dtstep.lighthouse.common.exception.LightTimeOutException;
import com.dtstep.lighthouse.common.fusing.FusingSwitch;
import com.dtstep.lighthouse.common.fusing.FusingToken;
import com.dtstep.lighthouse.common.ice.ReceiverInterfacePrx;
import com.dtstep.lighthouse.common.ice.ReceiverInterfacePrxHelper;
import com.dtstep.lighthouse.common.util.SnappyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private final ReceiverInterfacePrx receiverPrx;

    public MessageSender(Communicator ic){
        Ice.ObjectPrx receiverBasePrx = ic.stringToProxy("identity_receiver").ice_connectionId(UUID.randomUUID().toString()).ice_locatorCacheTimeout(1200);
        receiverPrx = ReceiverInterfacePrxHelper.checkedCast(receiverBasePrx);
    }

    public void send(String text){
        FusingToken fusingToken = null;
        try{
            fusingToken = FusingSwitch.entry(FusingRules.CLIENT_EXCEPTION_RULE);
            if(fusingToken == null){
                logger.error("number of exceptions reaches the threshold, the call is blocked!");
                return;
            }
            byte[] bytes;
            if(text.length() < SysConst._COMPRESS_THRESHOLD_SIZE){
                bytes = text.getBytes(StandardCharsets.UTF_8);
            }else{
                bytes = SnappyUtil.compressToByte(text);
            }
            Lock lock = new ReentrantLock();
            Condition condition = lock.newCondition();
            Ice.AsyncResult asyncResult = receiverPrx.begin_logic(bytes,new NotifyThread(lock,condition));
            lock.lock();
            try {
                condition.await(LightHouse._limiterTimeOut, TimeUnit.MILLISECONDS);
                if(!asyncResult.isCompleted()){
                    throw new LightTimeOutException();
                }
            } finally {
                lock.unlock();
            }
            receiverPrx.end_logic(asyncResult);
        }catch (Ice.NotRegisteredException ex){
            LightHouse._InitFlag.set(false);
            logger.error("lighthouse client failed to send message!",ex);
            FusingSwitch.track(fusingToken);
        }catch (Exception ex){
            logger.error("lighthouse client failed to send message!",ex);
            FusingSwitch.track(fusingToken);
        }
    }
}
