package com.dtstep.lighthouse.core.wrapper;
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
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.stat.FilterParam;
import com.dtstep.lighthouse.common.entity.stat.FilterParamElement;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.hash.HashUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import com.dtstep.lighthouse.core.storage.proxy.DimensStorageProxy;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public final class DimensDBWrapper {

    private static final Logger logger = LoggerFactory.getLogger(DimensDBWrapper.class);

    private final static Cache<String, Optional<List<String>>> DIMENS_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .maximumSize(90000)
            .softValues()
            .build();

    public static String generateKey(String token,String dimens,String dimensValue){
        int index = Math.abs((int) (HashUtil.BKDRHash(dimensValue) % SysConst._DIMENS_STORAGE_PRE_PARTITIONS_SIZE));
        String prefix = SysConst._DBKeyPrefixArray[index];
        String origin = Md5Util.getMD5(token + "_" + dimens);
        return prefix + origin + "_" + Md5Util.getMD5(dimensValue);
    }


    public static List<String> loadDimension(String token, String dimens,String last, int limitSize) {
        String cacheKey = token + "_" + dimens + "_" + last + "_" + limitSize;
        Optional<List<String>> optional = DIMENS_CACHE.get(cacheKey,k -> {
            List<String> result = null;
            try{
                result = DimensStorageProxy.queryDimensList(token, dimens, last, limitSize);
            }catch (Exception ex){
                logger.error("load dimens error!",ex);
            }
            return Optional.ofNullable(result);
        });
        if(optional != null && optional.isPresent()){
            return optional.get();
        }
        return null;
    }



    public static String getDimensValue(Map<String,Object> envMap,String[] dimensArr,long batchTime) throws Exception {
        if(dimensArr == null){
            return null;
        }else if(dimensArr.length == 1 && envMap.containsKey(dimensArr[0])){
            return String.valueOf(envMap.get(dimensArr[0]));
        }else{
            StringBuilder sbr = new StringBuilder(32);
            for(int i=0;i<dimensArr.length;i++){
                if(i != 0){
                    sbr.append(StatConst.DIMENS_SEPARATOR);
                }
                String subDimens = dimensArr[i];
                if(envMap.containsKey(subDimens)){
                    subDimens = String.valueOf(envMap.get(subDimens));
                }else{
                    subDimens = String.valueOf(FormulaCalculate.parseVariableEntity(subDimens,envMap,batchTime));
                }
                sbr.append(subDimens);
            }
            return sbr.toString();
        }
    }

    public static HashMap<String,String> columnCombination(List<FilterParam> filterParams, StatExtEntity statExtEntity) {
        if(CollectionUtils.isEmpty(filterParams)){
            return null;
        }
        Set<FilterParamElement>[] sets = new Set[filterParams.size()];
        int i=0;
        List<String> currentDimensList = new ArrayList<>();
        for(FilterParam filterParam : filterParams){
            sets[i] = new HashSet<>(filterParam.getValueList());
            currentDimensList.addAll(Arrays.asList(filterParam.getFilterKey().split(StatConst.DIMENS_SEPARATOR)));
            i++;
        }
        List<String> statDimensList = Arrays.asList(statExtEntity.getTemplateEntity().getDimensArr());
        Set<List<FilterParamElement>> set = Sets.cartesianProduct(sets);
        Iterator<List<FilterParamElement>> it = set.iterator();
        HashMap<String,String> dimensMapper = new HashMap<>();
        while (it.hasNext()){
            List<FilterParamElement> list = it.next();
            List<DimensSorter> valueList = new ArrayList<>();
            List<DimensSorter> nameList = new ArrayList<>();
            int index = 0;
            for (FilterParamElement element : list) {
                String value = element.getValue();
                String aliasName = element.getAliasName();
                if(StringUtil.isEmpty(aliasName)){
                    aliasName = value;
                }
                String[] valueArr = value.split(StatConst.DIMENS_SEPARATOR);
                String[] aliasArr = aliasName.split(StatConst.DIMENS_SEPARATOR);
                for (int m = 0; m < valueArr.length; m++) {
                    String dimensValue = valueArr[m];
                    String aliasDimensName = aliasArr[m];
                    valueList.add(new DimensSorter(dimensValue, statDimensList.indexOf(currentDimensList.get(index))));
                    nameList.add(new DimensSorter(aliasDimensName, statDimensList.indexOf(currentDimensList.get(index))));
                    index++;
                }
            }
            List<String> resValueList = valueList.stream().sorted(Comparator.comparing(DimensSorter::getIndex)).map(DimensSorter::getValue).collect(Collectors.toList());
            List<String> resNameList = nameList.stream().sorted(Comparator.comparing(DimensSorter::getIndex)).map(DimensSorter::getValue).collect(Collectors.toList());
            dimensMapper.put(Joiner.on(StatConst.DIMENS_SEPARATOR).join(resValueList),Joiner.on(StatConst.DIMENS_SEPARATOR).join(resNameList));
        }
        return dimensMapper;
    }


    private static class DimensSorter{

        private String value;

        private int index;

        DimensSorter(String value,int index){
            this.value = value;
            this.index = index;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

}
