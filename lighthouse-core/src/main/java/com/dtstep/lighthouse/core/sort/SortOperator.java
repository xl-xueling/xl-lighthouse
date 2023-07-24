package com.dtstep.lighthouse.core.sort;
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
import com.google.common.collect.Ordering;
import com.dtstep.lighthouse.common.util.ReflectUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import java.math.BigDecimal;
import java.util.*;


public class SortOperator {

    public static void sortList(List list) throws Exception{
        list.sort(Comparator.naturalOrder());
    }

    public static <T> List<T> sortList(List<T> list,String field,final boolean isAsc) throws Exception{
        Ordering<Object> ordering = Ordering.from(getComparator(isAsc,field));
        return ordering.sortedCopy(list);
    }

    private static Comparator<Object> getComparator(final boolean isAsc,final String fieldName){
        return (o1, o2) -> {
            Object value1;
            Object value2;
            if (StringUtil.isEmpty(fieldName)) {
                value1 = o1;
                value2 = o2;
            } else {
                value1 = ReflectUtil.getFieldValue(o1, fieldName);
                value2 = ReflectUtil.getFieldValue(o2, fieldName);
            }
            return compareTo(isAsc, value1, value2);
        };
    }

    private static int compareTo(boolean isAsc, Object object1, Object object2) {
        int result = 0;
        try {
            result = compareTo(object1, object2);
            if (!isAsc) {
                result = -result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static int compareTo(Object object1, Object object2) {
        boolean isEmptyV1 = (object1 == null);
        boolean isEmptyV2 = (object2 == null);
        if (!isEmptyV1 && !isEmptyV2) {
            String value1 = object1.toString();
            String value2 = object2.toString();
            if (object1 instanceof Date) {
                return ((Date) object1).compareTo((Date) object2);
            } else if (object1 instanceof Number || (StringUtil.isNumber(value1) && StringUtil.isNumber(value2))) {
                try {
                    return new BigDecimal(value1).compareTo(new BigDecimal(value2));
                } catch (Exception e) {
                    return value1.compareTo(value2);
                }
            } else {
                return value1.compareTo(value2);
            }
        } else if (!isEmptyV1 && isEmptyV2) {
            return 1;
        } else if (isEmptyV1 && !isEmptyV2) {
            return -1;
        } else {
            return 0;
        }
    }
}
