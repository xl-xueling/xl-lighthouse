package com.dtstep.lighthouse.common.util;

import com.dtstep.lighthouse.common.entity.message.LightMessage;
import org.junit.Test;

import java.util.LinkedHashMap;

public class MessageHelperTest {


    @Test
    public void test2() throws Exception{
        String ss = "100106\u00011589167620000\u0001seqno\u00031e390b67-7ec9-4022-82de-e2d1a96a1142\u00014000";
        LightMessage lightMessage = MessageHelper.parseText(ss);
        System.out.println("lighthouseMessage:" + lightMessage.getGroupId());
        System.out.println(lightMessage.getParamMap());
        System.out.println(lightMessage.getTime());
        System.out.println(lightMessage.getRepeat());
    }

    @Test
    public void test3() throws Exception{
        String s = "100108\u00021592046240000\u0002null\u0002135000";
        LightMessage message = MessageHelper.parseText(s);
        System.out.println("message:" + message);
    }

    @Test
    public void testMap() throws Exception{
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("province","null");
        map.put("city","sag");
        String s = MessageHelper.serializeOfText(101,map,System.currentTimeMillis());
        System.out.println("s:" + s);
        LightMessage lightMessage = MessageHelper.parseText(s);
        System.out.println("lighthouseMessage:" + lightMessage);
    }

}
