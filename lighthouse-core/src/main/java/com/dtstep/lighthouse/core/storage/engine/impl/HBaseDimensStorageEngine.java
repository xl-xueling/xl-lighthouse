package com.dtstep.lighthouse.core.storage.engine.impl;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import com.dtstep.lighthouse.core.hbase.HBaseClient;
import com.dtstep.lighthouse.core.wrapper.DimensDBWrapper;
import com.google.common.collect.Lists;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.event.DimensBucket;
import com.dtstep.lighthouse.common.hash.HashUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.storage.engine.DimensStorageEngine;
import org.apache.commons.collections.MapUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.javatuples.Quartet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;

public class HBaseDimensStorageEngine extends DimensStorageEngine<DimensBucket, String> {

    private static final Logger logger = LoggerFactory.getLogger(HBaseDimensStorageEngine.class);

    @Override
    public void put(List<DimensBucket> events) throws Exception {
        List<Quartet<String,String,Object,Long>> list = Lists.newArrayListWithCapacity(events.size());
        for(DimensBucket quartet : events){
            String rowKey = DimensDBWrapper.generateKey(quartet.getToken(),quartet.getDimens(),quartet.getDimensValue());
            if(logger.isTraceEnabled()){
                logger.trace("save dimens,token:{},dimens:{},dimensValue:{},rowKey:{}",quartet.getToken(),quartet.getDimens(),quartet.getDimensValue(),rowKey);
            }
            Quartet<String,String,Object,Long> result = Quartet.with(rowKey,"d",quartet.getDimensValue(),quartet.getTtl());
            list.add(result);
        }
        HBaseClient.batchPut(StatConst.DIMENS_STORAGE_TABLE,list);
    }

    @Override
    public List<String> queryDimensList(String token, String dimens, String lastDimensValue, int limitSize) throws Exception {
        List<String> dimensList = Lists.newArrayListWithExpectedSize(limitSize);
        int startIndex = 0;
        String startRow = null;
        if(!StringUtil.isEmpty(lastDimensValue)){
            startIndex = Math.abs((int) (HashUtil.BKDRHash(lastDimensValue) % SysConst._DIMENS_STORAGE_PRE_PARTITIONS_SIZE));
            startRow = DimensDBWrapper.generateKey(token,dimens,lastDimensValue) + ".";
        }
        String finalStartRow = startRow;
        for(int i=startIndex;i<SysConst._DIMENS_STORAGE_PRE_PARTITIONS_SIZE;i++){
            if(dimensList.size() >= limitSize){
                break;
            }
            String prefix = SysConst._DBKeyPrefixArray[i];
            String partStartRow = prefix + Md5Util.getMD5(token + "_" + dimens);
            if(finalStartRow != null && finalStartRow.compareTo(partStartRow) > 0){
                partStartRow = finalStartRow;
            }
            String partEnd = prefix + Md5Util.getMD5(token + "_" + dimens) + "|";
            try{
                while (true){
                    LinkedHashMap<String, Result> resultHashMap = HBaseClient.scanWithMap(StatConst.DIMENS_STORAGE_TABLE, partStartRow,partEnd,(limitSize - dimensList.size()));
                    if(MapUtils.isEmpty(resultHashMap)){
                        break;
                    }
                    for(String rowKey : resultHashMap.keySet()){
                        Result result = resultHashMap.get(rowKey);
                        byte[] dimensBytes = result.getValue(Bytes.toBytes("f"),Bytes.toBytes("d"));
                        if(dimensBytes != null && dimensList.size() < limitSize){
                            String dimensValue = Bytes.toString(dimensBytes);
                            partStartRow = rowKey;
                            dimensList.add(dimensValue);
                        }
                    }
                    if(dimensList.size() >= limitSize){
                        break;
                    }
                }
            }catch (Exception ex){
                logger.error("load dimens error!",ex);
            }
        }
        return dimensList;
    }
}
