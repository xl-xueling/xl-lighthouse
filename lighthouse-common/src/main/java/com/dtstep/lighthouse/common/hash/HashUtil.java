package com.dtstep.lighthouse.common.hash;
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
import com.dtstep.lighthouse.common.util.StringUtil;

public class HashUtil {

    public static long BKDRHash(String str) {
        int seed = 131;
        int hash = 0;
        for(int i = 0; i < str.length(); i++)
        {
            hash = (hash * seed) + str.charAt(i);
        }
        return Math.abs(Long.valueOf(hash & 0x7FFFFFFF));
    }

    public static int getHashIndex(String text,int totalSize) {
        if(StringUtil.isEmpty(text)){
            return 0;
        }
        return (int)(HashUtil.BKDRHash(text) % totalSize);
    }
}
