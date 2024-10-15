package com.dtstep.lighthouse.ice.servant.rpc.ice;
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
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.ice.LightRpcException;
import com.dtstep.lighthouse.common.ice.RemoteLightServer;
import com.dtstep.lighthouse.common.serializer.SerializerProxy;
import com.dtstep.lighthouse.core.builtin.CallerStat;
import com.dtstep.lighthouse.core.ipc.RPCServer;
import com.dtstep.lighthouse.core.ipc.impl.RPCServerImpl;
import com.dtstep.lighthouse.core.tools.ObjectSize;
import com.zeroc.Ice.Current;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ICERemoteLightServerImpl implements RemoteLightServer {

    private static final Logger logger = LoggerFactory.getLogger(ICERemoteLightServerImpl.class);

    private static final RPCServer rpc = new RPCServerImpl();

    @Override
    public byte[] process(byte[] message, Current current) throws LightRpcException{
        try{
            rpc.process(message);
        }catch (Exception ex){
            logger.error("process message error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
        return null;
    }

    @Override
    public byte[] queryGroupInfo(String token, Current current) throws LightRpcException{
        GroupVerifyEntity groupVerifyEntity;
        try{
            groupVerifyEntity = rpc.queryGroupInfo(token);
        }catch (Exception ex){
            logger.error("query group info error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
        if(groupVerifyEntity == null){
            return null;
        }
        try{
            return SerializerProxy.instance().serialize(groupVerifyEntity);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] queryStatInfo(int id, Current current) throws LightRpcException {
        StatVerifyEntity statVerifyEntity;
        try{
            statVerifyEntity = rpc.queryStatInfo(id);
        }catch (Exception ex){
            logger.error("query stat info error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
        if(statVerifyEntity == null){
            return null;
        }
        try{
            return SerializerProxy.instance().serialize(statVerifyEntity);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] dataQuery(int statId, String dimensValue, List<Long> batchList, Current current) throws LightRpcException {
        List<StatValue> statValues;
        try{
            statValues = rpc.dataQuery(statId,dimensValue,batchList);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(statValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] dataDurationQuery(int statId, String dimensValue, long startTime, long endTime, Current current) throws LightRpcException {
        List<StatValue> statValues;
        try{
            statValues = rpc.dataDurationQuery(statId,dimensValue,startTime,endTime);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(statValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] dataQueryWithDimensList(int statId, List<String> dimensValueList, List<Long> batchList, Current current) throws LightRpcException{
        Map<String,List<StatValue>> statValues;
        try{
            statValues = rpc.dataQueryWithDimensList(statId,dimensValueList,batchList);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(statValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] dataDurationQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime, Current current) throws LightRpcException{
        Map<String,List<StatValue>> statValues;
        try{
            statValues = rpc.dataDurationQueryWithDimensList(statId,dimensValueList,startTime,endTime);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(statValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] limitQuery(int statId, long batchTime, Current current) throws LightRpcException{
        List<LimitValue> limitValues;
        try{
            limitValues = rpc.limitQuery(statId,batchTime);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(limitValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] dataDurationQueryV2(String callerName, String callerKey, int statId, String dimensValue, long startTime, long endTime, Current current) throws LightRpcException {
        int callerId;
        try{
            callerId = rpc.authVerification(callerName,callerKey,statId, ResourceTypeEnum.Stat);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        long inBytes = ObjectSize.getObjectSize(callerName) + ObjectSize.getObjectSize(callerKey);
        long outBytes = 0;
        List<StatValue> statValues;
        try{
            statValues = rpc.dataDurationQuery(statId,dimensValue,startTime,endTime);
            outBytes = ObjectSize.getObjectSize(statValues);
            CallerStat.stat(callerId,"dataDurationQuery",1,0,inBytes,outBytes);
        }catch (Exception ex){
            CallerStat.stat(callerId,"dataDurationQuery",1,0,inBytes,outBytes);
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(statValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] dataQueryV2(String callerName, String callerKey, int statId, String dimensValue, List<Long> batchList, Current current) throws LightRpcException {
        int callerId;
        try{
            callerId = rpc.authVerification(callerName,callerKey,statId, ResourceTypeEnum.Stat);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        long inBytes = ObjectSize.getObjectSize(callerName) + ObjectSize.getObjectSize(callerKey);
        long outBytes = 0;
        List<StatValue> statValues;
        try{
            statValues = rpc.dataQuery(statId,dimensValue,batchList);
            outBytes = ObjectSize.getObjectSize(statValues);
            CallerStat.stat(callerId,"dataQuery",1,0,inBytes,outBytes);
        }catch (Exception ex){
            CallerStat.stat(callerId,"dataQuery",1,0,inBytes,outBytes);
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(statValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] dataDurationQueryWithDimensListV2(String callerName, String callerKey, int statId, List<String> dimensValueList, long startTime, long endTime, Current current) throws LightRpcException {
        int callerId;
        try{
            callerId = rpc.authVerification(callerName,callerKey,statId, ResourceTypeEnum.Stat);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        long inBytes = ObjectSize.getObjectSize(callerName) + ObjectSize.getObjectSize(callerKey);
        long outBytes = 0;
        Map<String,List<StatValue>> statValues;
        try{
            statValues = rpc.dataDurationQueryWithDimensList(statId,dimensValueList,startTime,endTime);
            outBytes = ObjectSize.getObjectSize(statValues);
            CallerStat.stat(callerId,"dataDurationQueryWithDimensList",1,0,inBytes,outBytes);
        }catch (Exception ex){
            CallerStat.stat(callerId,"dataDurationQueryWithDimensList",1,0,inBytes,outBytes);
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(statValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] dataQueryWithDimensListV2(String callerName, String callerKey, int statId, List<String> dimensValueList, List<Long> batchList, Current current) throws LightRpcException {
        int callerId;
        try{
            callerId = rpc.authVerification(callerName,callerKey,statId, ResourceTypeEnum.Stat);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        long inBytes = ObjectSize.getObjectSize(callerName) + ObjectSize.getObjectSize(callerKey);
        long outBytes = 0;
        Map<String,List<StatValue>> statValues;
        try{
            statValues = rpc.dataQueryWithDimensList(statId,dimensValueList,batchList);
            outBytes = ObjectSize.getObjectSize(statValues);
            CallerStat.stat(callerId,"dataQueryWithDimensList",1,0,inBytes,outBytes);
        }catch (Exception ex){
            CallerStat.stat(callerId,"dataQueryWithDimensList",1,0,inBytes,outBytes);
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(statValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }

    @Override
    public byte[] limitQueryV2(String callerName, String callerKey, int statId, long batchTime, Current current) throws LightRpcException {
        int callerId;
        try{
            callerId = rpc.authVerification(callerName,callerKey,statId, ResourceTypeEnum.Stat);
        }catch (Exception ex){
            throw new LightRpcException(ex.getMessage());
        }
        long inBytes = ObjectSize.getObjectSize(callerName) + ObjectSize.getObjectSize(callerKey);
        long outBytes = 0;
        List<LimitValue> limitValues;
        try{
            limitValues = rpc.limitQuery(statId,batchTime);
            outBytes = ObjectSize.getObjectSize(limitValues);
            CallerStat.stat(callerId,"limitQuery",1,0,inBytes,outBytes);
        }catch (Exception ex){
            CallerStat.stat(callerId,"limitQuery",1,0,inBytes,outBytes);
            throw new LightRpcException(ex.getMessage());
        }
        try{
            return SerializerProxy.instance().serialize(limitValues);
        }catch (Exception ex){
            logger.error("rpc response serialize error!",ex);
            throw new LightRpcException(ex.getMessage());
        }
    }
}
