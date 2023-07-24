package com.dtstep.lighthouse.common.util;

import org.apache.commons.beanutils.BeanUtils;

public class CopyUtil {

    public static <T> void clone(T obj1,T obj2) throws Exception{
        BeanUtils.copyProperties(obj1, obj2);
    }
}
