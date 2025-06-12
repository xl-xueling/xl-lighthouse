package com.dtstep.lighthouse.core.formula;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
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

import java.util.ArrayList;
import java.util.List;

public class TemplateUtil {

    public static String[] split(String str) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        boolean inBrackets = false;
        boolean inParentheses = false;
        StringBuilder builder = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c == ';' && !inQuotes && !inBrackets && !inParentheses) {
                String temp = builder.toString().trim();
                if(!StringUtil.isEmpty(temp)){
                    result.add(temp);
                }
                builder.setLength(0);
            } else {
                builder.append(c);
                if (c == '\'') {
                    inQuotes = !inQuotes;
                } else if (c == '[') {
                    inBrackets = true;
                } else if (c == ']') {
                    inBrackets = false;
                } else if (c == '(') {
                    inParentheses = true;
                } else if (c == ')') {
                    inParentheses = false;
                }
            }
        }
        String temp = builder.toString().trim();
        if(!StringUtil.isEmpty(temp)){
            result.add(temp);
        }
        String[] resultArray = new String[result.size()];
        result.toArray(resultArray);
        return resultArray;
    }

}
