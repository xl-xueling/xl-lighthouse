package com.dtstep.lighthouse.core.sql;
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
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.util.StringUtil;

import java.util.Collection;
import java.util.stream.Collectors;


public class SqlBinder {

    private StringBuilder sbr;

    public SqlBinder(StringBuilder sbr){
        this.sbr = sbr;
    }

    public SqlBinder(){}

    @Override
    public String toString(){
        return sbr.toString();
    }

    public static class Builder {

        private final StringBuilder sbr = new StringBuilder(80);

        private boolean isFirstCondition = true;

        public Builder(){}

        public Builder appendSegment(String segment){
            sbr.append(" ").append(segment);
            return this;
        }

        public Builder appendSegmentIf(boolean is,String segment){
            if(is){
                sbr.append(" ").append(segment);
            }
            return this;
        }

        public Builder appendWhereSegment(String segment){
            if(isFirstCondition){
                sbr.append(String.format(" where %s",segment));
                isFirstCondition = false;
            }else{
                sbr.append(String.format(" and %s",segment));
            }
            return this;
        }

        public Builder appendLike(String k,Object v){
            if(v == null || SysConst.NULL_NUMERIC_VALUE.equals(v.toString())){
                return this;
            }
            String param = avoidInject(v.toString());
            if(isFirstCondition){
                sbr.append(String.format(" where %s like %s",k,"'%" + param + "%'"));
                isFirstCondition = false;
            }else{
                sbr.append(String.format(" and %s like %s",k,"'%" + param + "%'"));
            }
            return this;
        }

        public Builder appendLeftLike(String k,Object v){
            if(v == null || SysConst.NULL_NUMERIC_VALUE.equals(v.toString())){
                return this;
            }
            String param = avoidInject(v.toString());
            if(isFirstCondition){
                sbr.append(String.format(" where %s like %s",k,"'%" + param + "'"));
                isFirstCondition = false;
            }else{
                sbr.append(String.format(" and %s like %s",k,"'%" + param + "'"));
            }
            return this;
        }

        public Builder appendRightLike(String k, Object v){
            if(v == null || SysConst.NULL_NUMERIC_VALUE.equals(v.toString())){
                return this;
            }
            String param = avoidInject(v.toString());
            if(isFirstCondition){
                sbr.append(String.format(" where %s like %s",k,"'" + param + "%'"));
                isFirstCondition = false;
            }else{
                sbr.append(String.format(" and %s like %s",k,"'" + param + "%'"));
            }
            return this;
        }

        public Builder appendWhere(String k,Object v){
            if(v == null || SysConst.NULL_NUMERIC_VALUE.equals(v.toString())){
                return this;
            }
            String param = avoidInject(v.toString());
            if(isFirstCondition){
                sbr.append(String.format(" where %s = '%s'",k,param));
                isFirstCondition = false;
            }else{
                sbr.append(String.format(" and %s = '%s'",k,param));
            }
            return this;
        }

        public Builder appendIn(String k, Collection<Integer> collection){
            String ids;
            if(collection == null || collection.isEmpty()){
                ids = "-1";
            }else{
                ids = collection.stream().map(String::valueOf).collect(Collectors.joining(","));
            }
            if(isFirstCondition){
                sbr.append(String.format(" where %s in (%s)",k,ids));
                isFirstCondition = false;
            }else{
                sbr.append(String.format(" and %s in (%s)",k,ids));
            }
            return this;
        }


        public Builder appendInExceptNull(String k, Collection<Integer> collection){
            if(collection == null){
                return this;
            }
            String ids = collection.stream().map(String::valueOf).collect(Collectors.joining(","));
            if(StringUtil.isEmpty(ids)){
                ids = "-1";
            }
            if(isFirstCondition){
                sbr.append(String.format(" where %s in (%s)",k,ids));
                isFirstCondition = false;
            }else{
                sbr.append(String.format(" and %s in (%s)",k,ids));
            }
            return this;
        }

        static String avoidInject(String str)
        {
            return str.replaceAll("([';])+|(--)+","");
        }

        public SqlBinder create(){
            return new SqlBinder(sbr);
        }
    }
}
