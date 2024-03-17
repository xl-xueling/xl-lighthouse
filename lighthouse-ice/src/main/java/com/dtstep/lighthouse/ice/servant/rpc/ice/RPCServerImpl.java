package com.dtstep.lighthouse.ice.servant.rpc.ice;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.event.IceEvent;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.SnappyUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.disruptor.IceEventProducer;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.ice.servant.IceEventHandler;
import com.dtstep.lighthouse.ice.servant.rpc.RPCServer;
import com.google.common.base.Splitter;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class RPCServerImpl implements RPCServer {

    private static final Logger logger = LoggerFactory.getLogger(RPCServerImpl.class);

    private static final IceEventProducer eventProducer;

    static {
        Disruptor<IceEvent> disruptor = new Disruptor<>(
                IceEvent::new,
                1024 * 1024 * 2,
                Executors.defaultThreadFactory(),
                ProducerType.MULTI,
                new BlockingWaitStrategy()
        );
        IceEventHandler[] handlers = new IceEventHandler[10];
        for(int i=0;i<handlers.length;i++){
            handlers[i] = new IceEventHandler();
        }
        disruptor.handleEventsWithWorkerPool(handlers);
        disruptor.start();
        RingBuffer<IceEvent> ringBuffer = disruptor.getRingBuffer();
        eventProducer = new IceEventProducer(ringBuffer);
    }

    @Override
    public GroupVerifyEntity queryGroup(String token) throws Exception {
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryByToken(token);
        GroupVerifyEntity groupVerifyEntity = null;
        if(groupExtEntity != null){
            groupVerifyEntity = new GroupVerifyEntity();
            groupVerifyEntity.setVerifyKey(groupExtEntity.getVerifyKey());
            groupVerifyEntity.setRelationColumns(groupExtEntity.getRunningRelatedColumns());
            groupVerifyEntity.setState(groupExtEntity.getState());
            groupVerifyEntity.setGroupId(groupExtEntity.getId());
            groupVerifyEntity.setToken(groupExtEntity.getToken());
            groupVerifyEntity.setMinTimeParam(groupExtEntity.getMinTimeParam());
            if(logger.isTraceEnabled()){
                logger.trace("query groupInfo by token,token:{},groupInfo:{}",token, JsonUtil.toJSONString(groupVerifyEntity));
            }
        }
        return groupVerifyEntity;
    }

    @Override
    public void process(byte[] bytes) throws Exception {
        if(bytes == null){
            return;
        }
        String data;
        if(SnappyUtil.isCompress(bytes)){
            data = SnappyUtil.uncompressByte(bytes);
        }else{
            data = new String(bytes, StandardCharsets.UTF_8);
        }
        if(StringUtil.isEmpty(data)){
            return;
        }
        if(logger.isDebugEnabled()){
            logger.debug("lighthouse debug,ice service receive message:{}",data);
        }
        for (String temp : Splitter.on(StatConst.SEPARATOR_LEVEL_0).split(data)) {
            if (!StringUtil.isEmpty(temp)) {
                int index = temp.lastIndexOf(StatConst.SEPARATOR_LEVEL_1);
                eventProducer.onData(temp.substring(0, index), Integer.parseInt(temp.substring(index + 1)));
            }
        }
    }
}
