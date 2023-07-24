package com.dtstep.lighthouse.common.util;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtilTest {

    @Test
    public void test6() throws Exception{
        String columnName = "recallno";
        String formula = "behaviorType;recallno";
        Matcher m = Pattern.compile("(.*[^0-9_a-zA-Z]+|^)" + columnName + "([^0-9_a-zA-Z]+.*|$)").matcher(formula);
        if (m.matches()) {
            System.out.println("match ok!");
        }else{
            System.out.println("match fail");
        }
    }


    @Test
    public void test7() throws Exception {
        String formula = "4";
        Matcher m = Pattern.compile("[1-3]").matcher(formula);
        System.out.println("fatch:" + m.matches());
    }

    @Test
    public void isExistSpecialCharTest(){
        String str = "App Home Page Adv ()Stat";
        boolean is = StringUtil.isExistSpecialChar(str);
        System.out.println("is:" + is);
    }

}
