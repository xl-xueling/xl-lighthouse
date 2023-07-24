package com.dtstep.lighthouse.core.expression.embed;
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
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.StringUtil;


public final class EmbedFunction {

    public static long day_sub(long t1,int n){
        return DateUtil.getDayBefore(t1, n);
    }

    public static long minute_sub(long t1,int n){
        return DateUtil.getMinuteBefore(t1, n);
    }

    public static String date_format(long t,String fmt){
        return DateUtil.formatTimeStamp(t, fmt);
    }

    public static long parse_date(String dateStr,String fmt){
        try{
            return DateUtil.parseDate(dateStr,fmt);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return -1;
    }

    public static long hour_sub(long t1,int n){
        return DateUtil.getHourBefore(t1, n);
    }

    public static String split(String str1,String split,int index){
        return StringUtil.split(str1, split, index);
    }

    public static String reverse(String str){
        return StringUtil.reverse(str);
    }

    public static String replace(String str,String str1,String str2) {
        return StringUtil.replace(str, str1, str2);
    }

    public static String md5(String str) {
        return StringUtil.md5(str);
    }

    public static int hashcode(String str) {
       return StringUtil.hashcode(str);
    }

    public static String left(String str,int len) {
        return StringUtil.left(str, len);
    }

    public static String substr(String str,int startIndex,int len){
        return StringUtil.substr(str,startIndex,len);
    }

    public static String right(String str,int len) {
        return StringUtil.right(str, len);
    }

    public static boolean start_with(String str1,String str2) {
        return StringUtil.startwith(str1, str2);
    }

    public static boolean end_with(String str1,String str2) {
       return StringUtil.endwith(str1, str2);
    }

    public static int len(String str) {
        return StringUtil.len(str);
    }

    public static String lower(String str) {
        return str.toLowerCase();
    }

    public static String upper(String str) {
        return str.toUpperCase();
    }

    public static boolean contains(String str1,String str2) {
        return StringUtil.contains(str1, str2);
    }

    public static String trim(String str) {
        return str.trim();
    }

    public static String concat(String ...strs) {
       return StringUtil.concat(strs);
    }

}
