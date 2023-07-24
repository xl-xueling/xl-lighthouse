package com.dtstep.lighthouse.web.interceptor;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.exception.PermissionException;
import com.dtstep.lighthouse.common.exception.ProcessException;
import com.dtstep.lighthouse.common.util.EncryptUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class IntersectRequest extends HttpServletRequestWrapper {

    private static final Logger logger = LoggerFactory.getLogger(IntersectRequest.class);

    private final Map<String,String[]> paramsMap = new HashMap<>();

    private final String uri;

    public void decode() throws Exception {
        String param = this.getParameter("p");
        String vk = this.getParameter("vk");
        if(StringUtil.isEmpty(param) || StringUtil.isEmpty(vk)){
            if(uri.equals("/") || uri.equals("/index.shtml")){
                return;
            }else{
                throw new ProcessException();
            }
        }
        String desParams = EncryptUtil.decrypt(param, SysConst._ENCRYPT_KEY);
        String validvk = Md5Util.getMD5(desParams);
        if(!validvk.equals(vk)){
            throw new PermissionException();
        }
        JsonNode paramNode = new ObjectMapper().readTree(desParams);
        Iterator<String> iterator = paramNode.fieldNames();
        while (iterator.hasNext()){
            String fieldName = iterator.next();
            JsonNode jsonNode = paramNode.get(fieldName);
            String value;
            if(jsonNode.isArray() || jsonNode.isObject()){
               value = jsonNode.toString();
            }else{
                value = jsonNode.asText();
            }
            if(!StringUtil.isEmptyOrNullStr(value)){
                value = URLEncoder.encode(value,"utf-8");
                value = URLDecoder.decode(value, "utf-8");
                paramsMap.put(fieldName, new String[]{value});
            }
        }
    }

    IntersectRequest(String uri,HttpServletRequest request) {
        super(request);
        this.uri = uri;
        paramsMap.putAll(request.getParameterMap());
    }

    @Override
    public String getParameter(String name) {
        String [] vArr = paramsMap.get(name);
        if(vArr != null && vArr.length > 0){
            return vArr[0];
        }else{
            return null;
        }
    }
}
