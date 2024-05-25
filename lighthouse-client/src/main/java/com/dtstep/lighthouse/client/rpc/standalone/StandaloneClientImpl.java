package com.dtstep.lighthouse.client.rpc.standalone;

import com.dtstep.lighthouse.client.rpc.RPCClient;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.rpc.BasicRemoteLightServerPrx;
import com.dtstep.lighthouse.common.rpc.netty.NettyClientAdapter;
import com.dtstep.lighthouse.common.util.SnappyUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class StandaloneClientImpl implements RPCClient {

    private static final Logger logger = LoggerFactory.getLogger(StandaloneClientImpl.class);

    @Override
    public boolean init(String locators) throws Exception {
        if(StringUtil.isEmpty(locators)){
            throw new InitializationException("lighthouse client init failed,locators cannot be empty!");
        }
        try {
            NettyClientAdapter.instance().init(locators);
            logger.info("lighthouse client init success!");
        }catch (Exception ex){
            throw new InitializationException(String.format("lighthouse remote service not available,locators:%s",locators));
        }
        return true;
    }

    @Override
    public void reconnect() throws Exception {}

    @Override
    public void send(String text) throws Exception {
        byte[] bytes;
        if(text.length() < SysConst._COMPRESS_THRESHOLD_SIZE){
            bytes = text.getBytes(StandardCharsets.UTF_8);
        }else{
            bytes = SnappyUtil.compressToByte(text);
        }
        BasicRemoteLightServerPrx standaloneRemoteService = StandaloneHandler.getRemoteProxy();
        standaloneRemoteService.process(bytes);
    }

    @Override
    public GroupVerifyEntity queryGroupInfo(String token) throws Exception {
        BasicRemoteLightServerPrx standaloneRemoteService = StandaloneHandler.getRemoteProxy();
        return standaloneRemoteService.queryGroupInfo(token);
    }

    @Override
    public StatVerifyEntity queryStatInfo(int id) throws Exception {
        BasicRemoteLightServerPrx standaloneRemoteService = StandaloneHandler.getRemoteProxy();
        return standaloneRemoteService.queryStatInfo(id);
    }

    @Override
    public List<StatValue> dataQuery(int statId, String dimensValue, List<Long> batchList) throws Exception {
        BasicRemoteLightServerPrx standaloneRemoteService = StandaloneHandler.getRemoteProxy();
        return standaloneRemoteService.dataQuery(statId, dimensValue, batchList);
    }

    @Override
    public List<StatValue> dataDurationQuery(int statId, String dimensValue, long startTime, long endTime) throws Exception {
        BasicRemoteLightServerPrx standaloneRemoteService = StandaloneHandler.getRemoteProxy();
        return standaloneRemoteService.dataDurationQuery(statId, dimensValue, startTime, endTime);
    }

    @Override
    public Map<String, List<StatValue>> dataQueryWithDimensList(int statId, List<String> dimensValueList, List<Long> batchList) throws Exception {
        BasicRemoteLightServerPrx standaloneRemoteService = StandaloneHandler.getRemoteProxy();
        return standaloneRemoteService.dataQueryWithDimensList(statId, dimensValueList, batchList);
    }

    @Override
    public Map<String, List<StatValue>> dataDurationQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime) throws Exception {
        BasicRemoteLightServerPrx standaloneRemoteService = StandaloneHandler.getRemoteProxy();
        return standaloneRemoteService.dataDurationQueryWithDimensList(statId, dimensValueList, startTime, endTime);
    }

    @Override
    public List<LimitValue> limitQuery(int statId, long batchTime) throws Exception {
        BasicRemoteLightServerPrx standaloneRemoteService = StandaloneHandler.getRemoteProxy();
        return standaloneRemoteService.limitQuery(statId, batchTime);
    }
}
