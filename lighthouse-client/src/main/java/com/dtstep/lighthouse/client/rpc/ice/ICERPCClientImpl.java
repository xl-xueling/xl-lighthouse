package com.dtstep.lighthouse.client.rpc.ice;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.dtstep.lighthouse.client.rpc.RPCClient;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.ice.RemoteLightServerPrx;
import com.dtstep.lighthouse.common.util.SerializeUtil;
import com.dtstep.lighthouse.common.util.SnappyUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ICERPCClientImpl implements RPCClient {

    private static final Logger logger = LoggerFactory.getLogger(ICERPCClientImpl.class);

    private static Communicator ic;

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
            String[] initParams = new String[]{cfg};
            Properties iceProperties = Util.createProperties();
            iceProperties.setProperty("Ice.Override.ConnectTimeout", "5000");
            iceProperties.setProperty("Ice.RetryIntervals", "20");
            iceProperties.setProperty("Ice.ThreadPool.Client.Size", "50");
            iceProperties.setProperty("Ice.ThreadPool.Client.SizeMax", "300");
            iceProperties.setProperty("Ice.MessageSizeMax", "1409600");
            InitializationData initData = new InitializationData();
            initData.properties = iceProperties;
            ic = Util.initialize(initParams, initData);
            RemoteLightServerPrx remoteLightServerPrx = ICEHandler.getRemotePrx(ic);
            remoteLightServerPrx.ice_ping();
            logger.info("lighthouse client init success!");
        }catch (Exception ex){
            throw new InitializationException(String.format("lighthouse remote service not available,locators:%s",locators));
        }
        return true;
    }

    @Override
    public GroupVerifyEntity queryGroup(String token) throws Exception {
        RemoteLightServerPrx remoteLightServerPrx = ICEHandler.getRemotePrx(ic);
        byte[] bytes = remoteLightServerPrx.queryGroupInfo(token);
        GroupVerifyEntity groupVerifyEntity = null;
        if(bytes != null){
            groupVerifyEntity = SerializeUtil.deserialize(bytes);
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
        RemoteLightServerPrx remoteLightServerPrx = ICEHandler.getRemotePrx(ic);
        remoteLightServerPrx.process(bytes);
    }

    @Override
    public StatVerifyEntity queryStat(int id) throws Exception {
        RemoteLightServerPrx remoteLightServerPrx = ICEHandler.getRemotePrx(ic);
        byte[] bytes = remoteLightServerPrx.queryStatInfo(id);
        StatVerifyEntity statVerifyEntity = null;
        if(bytes != null){
            statVerifyEntity = SerializeUtil.deserialize(bytes);
        }
        return statVerifyEntity;
    }

    @Override
    public List<StatValue> dataQuery(int statId, String dimensValue, List<Long> batchList) throws Exception {
        RemoteLightServerPrx remoteLightServerPrx = ICEHandler.getRemotePrx(ic);
        byte[] bytes = remoteLightServerPrx.dataQuery(statId,dimensValue,batchList);
        List<StatValue> valueList = null;
        if(bytes != null){
            valueList = SerializeUtil.deserialize(bytes);
        }
        return valueList;
    }

    @Override
    public List<StatValue> dataDurationQuery(int statId, String dimensValue, long startTime, long endTime) throws Exception {
        RemoteLightServerPrx remoteLightServerPrx = ICEHandler.getRemotePrx(ic);
        byte[] bytes = remoteLightServerPrx.dataDurationQuery(statId,dimensValue,startTime,endTime);
        List<StatValue> valueList = null;
        if(bytes != null){
            valueList = SerializeUtil.deserialize(bytes);
        }
        return valueList;
    }

    @Override
    public Map<String, List<StatValue>> dataQueryWithDimensList(int statId, List<String> dimensValueList, List<Long> batchList) throws Exception {
        RemoteLightServerPrx remoteLightServerPrx = ICEHandler.getRemotePrx(ic);
        byte[] bytes = remoteLightServerPrx.dataQueryWithDimensList(statId,dimensValueList,batchList);
        Map<String, List<StatValue>> data = null;
        if(bytes != null){
            data = SerializeUtil.deserialize(bytes);
        }
        return data;
    }

    @Override
    public Map<String, List<StatValue>> dataDurationQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime) throws Exception {
        RemoteLightServerPrx remoteLightServerPrx = ICEHandler.getRemotePrx(ic);
        byte[] bytes = remoteLightServerPrx.dataDurationQueryWithDimensList(statId,dimensValueList,startTime,endTime);
        Map<String, List<StatValue>> data = null;
        if(bytes != null){
            data = SerializeUtil.deserialize(bytes);
        }
        return data;
    }

    @Override
    public List<LimitValue> limitQuery(int statId, long batchTime) throws Exception {
        RemoteLightServerPrx remoteLightServerPrx = ICEHandler.getRemotePrx(ic);
        byte[] bytes = remoteLightServerPrx.limitQuery(statId,batchTime);
        List<LimitValue> valueList = null;
        if(bytes != null){
            valueList = SerializeUtil.deserialize(bytes);
        }
        return valueList;
    }

    @Override
    public void reconnect() throws Exception {}
}
