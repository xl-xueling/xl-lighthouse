package com.dtstep.lighthouse.common.util;

import net.sf.cglib.beans.BeanCopier;

public class BeanCopyUtil {

    public static void copy(Object a,Object b){
        BeanCopier copier = BeanCopier.create(a.getClass(),b.getClass(),false);
        copier.copy(a,b,null);
    }
}
