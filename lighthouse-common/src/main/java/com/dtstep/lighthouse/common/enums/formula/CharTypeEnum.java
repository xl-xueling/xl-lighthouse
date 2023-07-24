package com.dtstep.lighthouse.common.enums.formula;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public enum CharTypeEnum {

    //a-z
    LETTER(1,1,"[a-zA-Z]"),

    //0-9
    NUMERIC(2,1,"[0-9]"),

    //.
    SPOT(16,1,"[.]"),

    //#
    POUND(13,1,"[#]"),

    //_
    UNDERLINE(3,1,"[_]"),

    //:
    COLON(32,1,"[:]"),

    //%
    PERCENT(31,1,"[%]"),

    //@
    AT(32,1,"[@]"),

    //'
    SINGLE_QUOTATION(12,1,"[']"),

    //"
    QUOTATION(13,1,"[\"]"),

    //CHINESE
    CHINESE(14,1,"[\\u4e00-\\u9fa5]"),

    //&&,||,!,> < ==
    LOGICAL(4,2,"[&|!><=]"),

    //+ - * / %
    CALCULATE(6,2,"[\\+\\-\\*/]"),

    //;
    SEMICOLON(5,3,"[;]"),

    //(
    LBRACKET(7,4,"[(]"),

    //)
    RBRACKET(8,4,"[)]"),

    //$
    DOLLAR(9,1,"[$]"),

    //{
    LBRACE(10,1,"[{]"),

    //}
    RBRACE(11,1,"[}]"),

    //，
    COMMA(12,6,"[,]"),

    //[
    LSQUARE_BRACKET(13,7,"[\\[]"),

    //]
    RSQUARE_BRACKET(14,7,"[\\]]"),

    //space
    SPACE(17,9,"[ ]"),

    //u001
    END(99,99,"[\u0001]");

    private int type;

    private int level;

    private String reg;

    CharTypeEnum(int type,int level,String reg){
        this.type = type;
        this.level = level;
        this.reg = reg;
    }

    CharTypeEnum(int type,int level){
        this.type = type;
        this.level = level;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public static CharTypeEnum getCharTypeEnum(char c){
        for(CharTypeEnum charTypeEnum:CharTypeEnum.values()){
            String reg = charTypeEnum.getReg();
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(String.valueOf(c));
            if(m.matches()){
                return charTypeEnum;
            }
        }
        return null;
    }
}
