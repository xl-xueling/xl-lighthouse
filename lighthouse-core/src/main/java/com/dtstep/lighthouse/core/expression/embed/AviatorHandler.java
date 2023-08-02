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
import com.dtstep.lighthouse.core.expression.embed.aviator.*;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.lexer.token.OperatorType;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.*;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public final class AviatorHandler {

    private static final Logger logger = LoggerFactory.getLogger(FormulaCalculate.class);

    private static final AviatorHandler aviatorHandler = new AviatorHandler();

    private static final AviatorEvaluatorInstance aviatorEvaluatorInstance = AviatorEvaluator.getInstance();

    static {
        aviatorEvaluatorInstance.setCachedExpressionByDefault(true);
        aviatorEvaluatorInstance.useLRUExpressionCache(80000);
        AviatorEvaluator.removeFunction("contains");
        AviatorEvaluator.addFunction(new ContainsFunction());
        AviatorEvaluator.removeFunction("endsWith");
        AviatorEvaluator.addFunction(new EndsWithFunction());
        AviatorEvaluator.removeFunction("md5");
        AviatorEvaluator.addFunction(new MD5Function());
        AviatorEvaluator.removeFunction("hashcode");
        AviatorEvaluator.addFunction(new HashCodeFunction());
        AviatorEvaluator.removeFunction("left");
        AviatorEvaluator.addFunction(new LeftFunction());
        AviatorEvaluator.removeFunction("len");
        AviatorEvaluator.addFunction(new LenFunction());
        AviatorEvaluator.removeFunction("replace");
        AviatorEvaluator.addFunction(new ReplaceFunction());
        AviatorEvaluator.removeFunction("reverse");
        AviatorEvaluator.addFunction(new ReverseFunction());
        AviatorEvaluator.removeFunction("right");
        AviatorEvaluator.addFunction(new RightFunction());
        AviatorEvaluator.removeFunction("split");
        AviatorEvaluator.addFunction(new SplitFunction());
        AviatorEvaluator.removeFunction("startWith");
        AviatorEvaluator.addFunction(new StartWithFunction());
        AviatorEvaluator.removeFunction("trim");
        AviatorEvaluator.addFunction(new TrimFunction());
        AviatorEvaluator.removeFunction("dateFormat");
        AviatorEvaluator.addFunction(new DateFormatFunction());
        AviatorEvaluator.removeFunction("dateParse");
        AviatorEvaluator.addFunction(new DateParseFunction());
        AviatorEvaluator.removeFunction("toUpper");
        AviatorEvaluator.addFunction(new UpperFunction());
        AviatorEvaluator.removeFunction("toLower");
        AviatorEvaluator.addFunction(new LowerFunction());
        AviatorEvaluator.removeFunction("isIn");
        AviatorEvaluator.addFunction(new IsInFunction());
        AviatorEvaluator.removeFunction("section");
        AviatorEvaluator.addFunction(new SectionFunction());
        AviatorEvaluator.removeFunction("substr");
        AviatorEvaluator.addFunction(new SubStrFunction());
        AviatorEvaluator.removeFunction("isEmpty");
        AviatorEvaluator.addFunction(new IsEmptyFunction());
        AviatorEvaluator.removeFunction("isNull");
        AviatorEvaluator.addFunction(new IsNullFunction());
        AviatorEvaluator.removeFunction("isNumeric");
        AviatorEvaluator.addFunction(new IsNumericFunction());
        AviatorEvaluator.removeFunction("minuteSub");
        AviatorEvaluator.addFunction(new MinuteSubFunction());
        AviatorEvaluator.removeFunction("hourSub");
        AviatorEvaluator.addFunction(new HourSubFunction());
        AviatorEvaluator.removeFunction("daySub");
        AviatorEvaluator.addFunction(new DaySubFunction());


        AviatorEvaluator.addOpFunction(OperatorType.EQ, new AbstractFunction() {
            private static final long serialVersionUID = 7858793601172630686L;

            @Override
            public AviatorBoolean call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                String temp1 = String.valueOf(arg1.getValue(env));
                String temp2 = String.valueOf(arg2.getValue(env));
                if(temp1.trim().equals(temp2.trim())){
                    return AviatorBoolean.TRUE;
                }else if(StringUtil.isInt(temp1) && StringUtil.isInt(temp2)){
                    long a = Long.parseLong(temp1);
                    long b = Long.parseLong(temp2);
                    return AviatorBoolean.valueOf(a == b);
                }else if ((isNumber(arg1) || StringUtil.isNumber(temp1)) && (isNumber(arg2) || StringUtil.isNumber(temp2))) {
                    BigDecimal a = new BigDecimal(temp1);
                    BigDecimal b = new BigDecimal(temp2);
                    return AviatorBoolean.valueOf(a.compareTo(b) == 0);
                }else{
                    return AviatorBoolean.valueOf(false);
                }
            }

            @Override
            public String getName() {
                return "==";
            }
        });


        AviatorEvaluator.addOpFunction(OperatorType.LT, new AbstractFunction() {
            private static final long serialVersionUID = 1032142270615951426L;

            @Override
            public AviatorBoolean call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                String temp1 = String.valueOf(arg1.getValue(env));
                String temp2 = String.valueOf(arg2.getValue(env));
                if (StringUtil.isEmpty(temp1) || StringUtil.isEmpty(temp2)) {
                    return AviatorBoolean.FALSE;
                }else if(StringUtil.isInt(temp1) && StringUtil.isInt(temp2)){
                    long a = Long.parseLong(temp1);
                    long b = Long.parseLong(temp2);
                    return AviatorBoolean.valueOf(a < b);
                }else if ((isNumber(arg1) || StringUtil.isNumber(temp1)) && (isNumber(arg2) || StringUtil.isNumber(temp2))) {
                    BigDecimal a = new BigDecimal(temp1);
                    BigDecimal b = new BigDecimal(temp2);
                    return AviatorBoolean.valueOf(a.compareTo(b) < 0);
                } else {
                    return AviatorBoolean.valueOf(temp1.compareTo(temp2) < 0);
                }
            }

            @Override
            public String getName() {
                return "<";
            }
        });


        AviatorEvaluator.addOpFunction(OperatorType.LE, new AbstractFunction() {
            private static final long serialVersionUID = 3358634192708716929L;

            @Override
            public AviatorBoolean call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                String temp1 = String.valueOf(arg1.getValue(env));
                String temp2 = String.valueOf(arg2.getValue(env));
                if(StringUtil.isEmpty(temp1) || StringUtil.isEmpty(temp2)){
                    return AviatorBoolean.FALSE;
                }else if(StringUtil.isInt(temp1) && StringUtil.isInt(temp2)){
                    long a = Long.parseLong(temp1);
                    long b = Long.parseLong(temp2);
                    return AviatorBoolean.valueOf(a <= b);
                }else if ((isNumber(arg1) || StringUtil.isNumber(temp1)) && (isNumber(arg2) || StringUtil.isNumber(temp2))) {
                    BigDecimal a = new BigDecimal(temp1);
                    BigDecimal b = new BigDecimal(temp2);
                    return AviatorBoolean.valueOf(a.compareTo(b) <= 0);
                }else{
                    return AviatorBoolean.valueOf(temp1.compareTo(temp2) <= 0);
                }
            }

            @Override
            public String getName() {
                return "<=";
            }
        });

        AviatorEvaluator.addOpFunction(OperatorType.GT, new AbstractFunction() {

            private static final long serialVersionUID = 309126794543864959L;

            @Override
            public AviatorBoolean call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                String temp1 = String.valueOf(arg1.getValue(env));
                String temp2 = String.valueOf(arg2.getValue(env));
                if(StringUtil.isEmpty(temp1) || StringUtil.isEmpty(temp2)){
                    return AviatorBoolean.FALSE;
                }else if(StringUtil.isInt(temp1) && StringUtil.isInt(temp2)){
                    long a = Long.parseLong(temp1);
                    long b = Long.parseLong(temp2);
                    return AviatorBoolean.valueOf(a > b);
                }else if ((isNumber(arg1) || StringUtil.isNumber(temp1)) && (isNumber(arg2) || StringUtil.isNumber(temp2))) {
                    BigDecimal a = new BigDecimal(temp1);
                    BigDecimal b = new BigDecimal(temp2);
                    return AviatorBoolean.valueOf(a.compareTo(b) > 0);
                }else{
                    return AviatorBoolean.valueOf(temp1.compareTo(temp2) > 0);
                }
            }

            @Override
            public String getName() {
                return ">";
            }
        });

        AviatorEvaluator.addOpFunction(OperatorType.GE, new AbstractFunction() {

            private static final long serialVersionUID = 476660652675389194L;

            @Override
            public AviatorBoolean call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                String temp1 = String.valueOf(arg1.getValue(env));
                String temp2 = String.valueOf(arg2.getValue(env));
                if(StringUtil.isEmpty(temp1) || StringUtil.isEmpty(temp2)){
                    return AviatorBoolean.FALSE;
                }else if(StringUtil.isInt(temp1) && StringUtil.isInt(temp2)){
                    long a = Long.parseLong(temp1);
                    long b = Long.parseLong(temp2);
                    return AviatorBoolean.valueOf(a >= b);
                }else if ((isNumber(arg1) || StringUtil.isNumber(temp1)) && (isNumber(arg2) || StringUtil.isNumber(temp2))) {
                    BigDecimal a = new BigDecimal(temp1);
                    BigDecimal b = new BigDecimal(temp2);
                    return AviatorBoolean.valueOf(a.compareTo(b) >= 0);
                } else {
                    return AviatorBoolean.valueOf(temp1.compareTo(temp2) >= 0);
                }
            }

            @Override
            public String getName() {
                return ">=";
            }
        });

        AviatorEvaluator.addOpFunction(OperatorType.NEQ, new AbstractFunction() {

            private static final long serialVersionUID = 309126794543864959L;

            @Override
            public AviatorBoolean call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                String temp1 = String.valueOf(arg1.getValue(env));
                String temp2 = String.valueOf(arg2.getValue(env));
                if(StringUtil.isEmpty(temp1) && StringUtil.isEmpty(temp2)){
                    return AviatorBoolean.FALSE;
                }else if(StringUtil.isEmpty(temp1) || StringUtil.isEmpty(temp2)){
                    return AviatorBoolean.TRUE;
                }else if(StringUtil.isInt(temp1) && StringUtil.isInt(temp2)){
                    int a = Integer.parseInt(temp1);
                    int b = Integer.parseInt(temp2);
                    return AviatorBoolean.valueOf(a != b);
                }else if ((isNumber(arg1) || StringUtil.isNumber(temp1)) && (isNumber(arg2) || StringUtil.isNumber(temp2))) {
                    BigDecimal a = new BigDecimal(temp1);
                    BigDecimal b = new BigDecimal(temp2);
                    return AviatorBoolean.valueOf(a.compareTo(b) != 0);
                }else{
                    return AviatorBoolean.valueOf(!temp1.equals(temp2));
                }
            }

            @Override
            public String getName() {
                return "!=";
            }
        });

        AviatorEvaluator.addOpFunction(OperatorType.ADD, new AbstractFunction() {
            private static final long serialVersionUID = 3358634192708716929L;

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                String temp1 = String.valueOf(arg1.getValue(env));
                String temp2 = String.valueOf(arg2.getValue(env));
                if(StringUtil.isInt(temp1) && StringUtil.isInt(temp2)){
                    long a = Long.parseLong(temp1);
                    long b = Long.parseLong(temp2);
                    return AviatorLong.valueOf(a + b);
                }else if ((isNumber(arg1) || StringUtil.isNumber(temp1)) && (isNumber(arg2) || StringUtil.isNumber(temp2))) {
                    BigDecimal a = new BigDecimal(temp1);
                    BigDecimal b = new BigDecimal(temp2);
                    AviatorDecimal c = AviatorDecimal.valueOf(a.add(b));
                    return AviatorDecimal.valueOf(a.add(b).setScale(3,RoundingMode.HALF_UP));
                }else{
                    return new AviatorString(temp1 + temp2);
                }
            }

            @Override
            public String getName() {
                return "+";
            }
        });


        AviatorEvaluator.addOpFunction(OperatorType.MULT, new AbstractFunction() {
            private static final long serialVersionUID = 3358634192708716929L;

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2){
                String temp1 = String.valueOf(arg1.getValue(env));
                String temp2 = String.valueOf(arg2.getValue(env));
                if ((isNumber(arg1) || StringUtil.isNumber(temp1)) && (isNumber(arg2) || StringUtil.isNumber(temp2))) {
                    BigDecimal a = new BigDecimal(temp1);
                    BigDecimal b = new BigDecimal(temp2);
                    return AviatorDecimal.valueOf(a.multiply(b).setScale(3,RoundingMode.HALF_UP));
                }
                return AviatorNil.NIL;
            }


            @Override
            public String getName() {
                return "*";
            }
        });

        AviatorEvaluator.addOpFunction(OperatorType.DIV, new AbstractFunction() {
            private static final long serialVersionUID = 3358634192708716929L;

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                String temp1 = String.valueOf(arg1.getValue(env));
                String temp2 = String.valueOf(arg2.getValue(env));
                if((isNumber(arg1) || StringUtil.isNumber(temp1)) && (isNumber(arg2) || StringUtil.isNumber(temp2)) && Double.parseDouble(temp2) != 0D){
                    BigDecimal a = new BigDecimal(temp1);
                    BigDecimal b = new BigDecimal(temp2);
                    return AviatorDecimal.valueOf(a.divide(b,3, RoundingMode.HALF_UP));
                }
                return AviatorNil.NIL;
            }

            @Override
            public String getName() {
                return "/";
            }
        });

        AviatorEvaluator.addOpFunction(OperatorType.SUB, new AbstractFunction() {
            private static final long serialVersionUID = 3358634192708716929L;

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                String temp1 = String.valueOf(arg1.getValue(env));
                String temp2 = String.valueOf(arg2.getValue(env));
                if((isNumber(arg1) || StringUtil.isNumber(temp1)) && (isNumber(arg2) || StringUtil.isNumber(temp2)) && Double.parseDouble(temp2) != 0D){
                    BigDecimal a = new BigDecimal(temp1);
                    BigDecimal b = new BigDecimal(temp2);
                    return AviatorDecimal.valueOf(a.subtract(b).setScale(3,RoundingMode.HALF_UP));
                }
                return AviatorNil.NIL;
            }

            @Override
            public String getName() {
                return "-";
            }
        });
    }

    public AviatorHandler(){}

    public static AviatorHandler instance(){
        return aviatorHandler;
    }

    public static Object execute(String str){
        if(StringUtil.isEmpty(str)){
            return StatConst.NIL_VAL;
        }else if(StringUtil.isNumber(str)){
            return str;
        }else {
           try{
                return aviatorEvaluatorInstance.execute(str,null,true);
            }catch (Exception ex){
                logger.error("aviator execute formula [{}] error!",str,ex);
                return null;
            }
        }
    }


    public static boolean isNumber(AviatorObject object){
        return object.getAviatorType() == AviatorType.Double || object.getAviatorType() == AviatorType.Decimal ||
                object.getAviatorType() == AviatorType.Long || object.getAviatorType() == AviatorType.BigInt;
    }

    public static boolean isBoolFormula(String str,Map<String,Object> envMap){
        if(StringUtil.isEmpty(str) || StringUtil.isLetterNumOrUnderLine(str)){
            return false;
        }else {
            Object object = execute(str,envMap);
            return object instanceof Boolean;
        }
    }

    public static boolean check(String str,Map<String,Object> envMap){
        if(StringUtil.isEmpty(str) || StringUtil.isNumber(str)){
            return false;
        }else {
            Object object = execute(str,envMap);
            return object instanceof Boolean && (Boolean) object;
        }
    }

    public static void compileStatFormula(String str) throws Exception {
        Object object = aviatorEvaluatorInstance.execute(str,null,true);
        Validate.isTrue(StringUtil.isNumber(object.toString()));
    }

    public static void compileDimensFormula(String str,Map<String,Object> envMap) throws Exception {
        aviatorEvaluatorInstance.execute(str,null,true);
    }

    public static Object execute(String str,Map<String,Object> envMap){
        if(StringUtil.isEmpty(str)){
            return StatConst.NIL_VAL;
        }else if(StringUtil.isNumber(str)){
            return str;
        }else if(envMap != null && envMap.containsKey(str)){
            return envMap.get(str);
        }else {
            return aviatorEvaluatorInstance.execute(str,envMap,true);
        }
    }
}
