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
import java.util.Stack;


public class Encode64 {


    private static char[] charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    final static char[] digits = {
            '0' , '1' , '2' , '3' , '4' , '5' ,
            '6' , '7' , '8' , '9' ,
            'A' , 'B' , 'C' , 'D' , 'E' , 'F' ,
            'G' , 'H' , 'I' , 'J' , 'K' , 'L' ,
            'M' , 'N' , 'O' , 'P' , 'Q' , 'R' ,
            'S' , 'T' , 'U' , 'V' , 'W' , 'X' ,'Y' , 'Z',
            'a' , 'b' ,
            'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
            'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
            'o' , 'p' , 'q' , 'r' , 's' , 't' ,
            'u' , 'v' , 'w' , 'x' , 'y' , 'z' ,
             '+' , '/'  ,
    };

    public static String translate62To10(String ident62 ) {
        int decimal = 0;
        int base = 62;
        int k;
        int cnt = 0;
        byte[] ident = ident62.getBytes();
        for ( int i = ident.length - 1; i >= 0; i-- ) {
            int num = 0;
            if ( ident[i] > 48 && ident[i] <= 57 ) {
                num = ident[i] - 48;
            }
            else if ( ident[i] >= 65 && ident[i] <= 90 ) {
                num = ident[i] - 65 + 10;
            }
            else if ( ident[i] >= 97 && ident[i] <= 122 ) {
                num = ident[i] - 97 + 10 + 26;
            }
            k = (int) java.lang.Math.pow( (double) base, (double) cnt );
            decimal += num * k;
            cnt++;
        }
        return String.format( "%08d", decimal );
    }

    /**
     * 将10进制转化为62进制
     * @param number
     * @param length 转化成的62进制长度，不足length长度的话高位补0，否则不改变什么
     * @return
     */
    public static String translate10To62(long number, int length){
        long rest=number;
        Stack<Character> stack=new Stack<Character>();
        StringBuilder result=new StringBuilder(0);
        while(rest!=0){
            stack.add(charSet[Long.valueOf((rest - (rest / 62) * 62)).intValue()]);
            rest=rest/62;
        }
        for(;!stack.isEmpty();){
            result.append(stack.pop());
        }
        int result_length = result.length();
        StringBuilder temp0 = new StringBuilder();
        for(int i = 0; i < length - result_length; i++){
            temp0.append('0');
        }
        return temp0.toString() + result.toString();

    }

}
