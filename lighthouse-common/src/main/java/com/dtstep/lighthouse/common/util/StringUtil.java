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
import com.dtstep.lighthouse.common.constant.StatConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class StringUtil {

    private static final Pattern p1 = Pattern.compile("[a-z_0-9]+");

    public static String replace(String temp,Map<String,String> replaceMap) {
        StringBuffer sbr = new StringBuffer();
        if(StringUtil.isEmpty(temp)){
            return null;
        }
        if(replaceMap == null){
            return temp;
        }
        Matcher m = p1.matcher(temp);
        while (m.find()){
            String key = m.group();
            if(replaceMap.containsKey(key)){
                m.appendReplacement(sbr, replaceMap.get(key).toString());
            }
        }
        m.appendTail(sbr);
        return sbr.toString();
    }

    public static String escape(String str){
        if(str == null){
            return null;
        }
        str = str.replace("\"","\\\"");
        str = str.replace("\'","\\\'");
        return str;
    }

    public static boolean isEqual(String a,String b){
        if(StringUtil.isDouble(a) && StringUtil.isDouble(b)){
            return (Double.parseDouble(a) == Double.parseDouble(b));
        }else{
            return (a.compareTo(b) == 0);
        }
    }

    public static boolean isGreater(String a,String b){
        if(StringUtil.isDouble(a) && StringUtil.isDouble(b)){
            return (Double.parseDouble(a) > Double.parseDouble(b));
        }else{
            return (a.compareTo(b) > 0);
        }
    }

    public static boolean isSmaller(String a,String b){
        if(StringUtil.isDouble(a) && StringUtil.isDouble(b)){
            return Double.parseDouble(a) < Double.parseDouble(b);
        }else{
            return a.compareTo(b) < 0;
        }
    }

    public static boolean isGreaterOrEqual(String a,String b){
        if(StringUtil.isDouble(a) && StringUtil.isDouble(b)){
            return Double.parseDouble(a) >= Double.parseDouble(b);
        }else{
            return a.compareTo(b) >= 0;
        }
    }

    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static boolean isEmptyOrNullStr(String str){
        return str == null || str.trim().length() == 0 || StatConst.NULL_STR.equals(str) || ("'" + StatConst.NULL_STR + "'").equals(str);
    }

    public static String join(Collection var0, String var1) {
        StringBuilder var2 = new StringBuilder(256);
        for(Iterator var3 = var0.iterator(); var3.hasNext(); var2.append((String)var3.next())) {
            if (var2.length() != 0) {
                var2.append(var1);
            }
        }
        return var2.toString();
    }

    private static final Pattern phonePattern = Pattern.compile("^[1][3,4,5,6,7,8][0-9]{9}$");

    public static boolean isPhone(final String str) {
        Matcher m = phonePattern.matcher(str);
        return m.matches();
    }

    public static String filterIllegalChar(String str) throws PatternSyntaxException {
        String regEx="";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    private static final Pattern dataPat = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");

    public static boolean isDate(String date){
        Matcher m = dataPat.matcher(date);
        return m.matches();
    }

    private static final String doubleRegex = "-?[0-9]+\\.?[0-9]*";
    private static final Pattern doublePattern =Pattern.compile(doubleRegex);

    public static boolean isDouble(String str){
        Matcher m = doublePattern.matcher(str);
        return m.matches();
    }

    private static final String intRegex = "-?[0-9]+[0-9]*";
    private static final Pattern intPattern = Pattern.compile(intRegex);

    public static boolean isInt(String str){
        Matcher m = intPattern.matcher(str);
        return m.matches();
    }

    public static boolean isLetter(String str){
        if(StringUtils.isEmpty(str)){
            return false;
        }
        return str.matches("[a-zA-Z]+");
    }

    private static final Pattern emailPat = Pattern.compile("^([a-z0-9A-Z]+[-|\\._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

    public static boolean isEmail(String email)
    {
        Matcher matcher = emailPat.matcher(email);
        return matcher.matches();
    }

    public static boolean isLetterOrNumeric(String str){
        if(StringUtils.isEmpty(str)){
            return false;
        }
        return str.matches("[a-zA-Z0-9]+");
    }

    private static final Pattern leterNumOrUnderline = Pattern.compile("[a-zA-Z0-9_]+");

    public static boolean isLetterNumOrUnderLine(String str){
        if(StringUtils.isEmpty(str)){
            return false;
        }
        return leterNumOrUnderline.matcher(str).matches();
    }

    public static boolean isNumber(String str){
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        if(str.startsWith("-")){
            str = str.substring(1);
        }
        boolean hasDot = false;
        for(int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if (c == 46) {
                if(hasDot){
                    return false;
                }else{
                    hasDot = true;
                }
            }else if(c < 48 || c > 57){
                return false;
            }
        }
        return true;
    }

    private static String scientificRegx = "[+-]*\\d+\\.?\\d*[Ee]*[+-]*\\d+";
    private static Pattern ScientificPattern = Pattern.compile(scientificRegx);

    public static boolean isScientific(String str){
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return ScientificPattern.matcher(str).matches();
    }

    public static boolean isDigits(String str){
        return NumberUtils.isDigits(str);
    }


    public static String split(String str1,String split,int index){
        if(StringUtils.isEmpty(str1) || StringUtils.isEmpty(split) || index < 0){
            return null;
        }
        String[] temArr = str1.split(split);
        if(temArr.length > index){
            return temArr[index];
        }
        return null;
    }

    public static String reverse(String str) {
        if(StringUtils.isEmpty(str)){
            return null;
        }
        return StringUtils.reverse(str);
    }

    public static String replace(String str,String str1,String str2)  {
        if(StringUtils.isEmpty(str) || StringUtils.isEmpty(str1)){
            return null;
        }
        return str.replace(str1,str2);
    }

    public static String md5(String str)  {
        if(StringUtils.isEmpty(str)){
            return null;
        }
        return Md5Util.getMD5(str);
    }

    public static int hashcode(String str)  {
        if(StringUtils.isEmpty(str)){
            return -1;
        }
        return Math.abs(str.hashCode());
    }

    public static String left(String str,int len) {
        if(StringUtils.isEmpty(str)){
            return null;
        }
        return StringUtils.left(str,len);
    }

    public static String substr(String str,int startIndex,int endIndex){
        if(StringUtils.isEmpty(str)){
            return null;
        }
        return StringUtils.substring(str,startIndex,endIndex);
    }

    public static String right(String str,int len) {
        if(StringUtils.isEmpty(str)){
            return null;
        }
        return StringUtils.right(str,len);
    }

    public static boolean startwith(String str1,String str2)  {
        if(StringUtils.isEmpty(str1) || StringUtils.isEmpty(str2)){
            return false;
        }
        return str1.startsWith(str2);
    }

    public static boolean endwith(String str1,String str2)  {
        if(StringUtils.isEmpty(str1) || StringUtils.isEmpty(str2)){
            return false;
        }
        return str1.endsWith(str2);
    }

    public static int len(String str)  {
        if(StringUtils.isEmpty(str)){
            return 0;
        }
        return str.length();
    }

    public static String lower(String str)  {
        return str.toLowerCase();
    }

    public static String upper(String str)  {
        return str.toUpperCase();
    }

    public static boolean contains(String str1,String str2)  {
        if(StringUtils.isEmpty(str1) || StringUtils.isEmpty(str2)){
            return false;
        }
        return str1.contains(str2);
    }

    public static String trim(String str)  {
        return str.trim();
    }

    public static String concat(String ...strs)  {
        String str = "";
        for(String temp : strs){
            str = str.concat(temp);
        }
        return str;
    }

    public static String displayDecimal(double d){
        return String.format("%.3f", d);
    }

    public static String displayFormat(double d){
        DecimalFormat df = new DecimalFormat("#.###");
        return df.format(d);
    }

    public static boolean isExistSpecialChar(String str) {
        String regEx = "[`~@$%^&*+=|{}<>/?~！@#￥%……&*——+|{}‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static byte[] getBytes(final String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    public static String newString(final byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static int getBLen(String value){
        int valueLength = 0;
        String chinaChar = "[\u0391-\uFFE5]";
        for(int i=0;i<value.length();i++){
            String temp = value.substring(i,i+1);
            if(temp.matches(chinaChar)){
                valueLength += 2;
            }else{
                valueLength += 1;
            }
        }
        return valueLength;
    }

    public static boolean isMD5(String str){
        return str.matches("^([a-fA-F0-9]{32})$");
    }


    private StringUtil(){}
}
