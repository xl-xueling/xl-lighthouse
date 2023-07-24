package com.dtstep.lighthouse.common.util;
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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;


public final class Md5Util {

    public static String get16MD5(String message) {
        String str = getMD5(message);
        if(!StringUtils.isEmpty(str)){
            str = str.substring(8,24);
        }
        return str;
    }

    public static String getMD5(String message) {
        return DigestUtils.md5Hex(message);
    }
}
