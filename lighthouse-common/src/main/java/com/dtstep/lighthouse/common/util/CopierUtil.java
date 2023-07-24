package com.dtstep.lighthouse.common.util;

import net.sf.cglib.beans.BeanCopier;

public class CopierUtil {

    public static void copyTo(Object a,Object b){
        if(a == null || b == null){
            return;
        }
        BeanCopier copier = BeanCopier.create(a.getClass(), b.getClass(),false);
        copier.copy(a,b,null);
    }

}
