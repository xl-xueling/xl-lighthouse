package com.dtstep.lighthouse.common.util;

import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonUtilTest {

    @Test
    public void test3() {
        LightMessage lightMessage = new LightMessage();
        lightMessage.setGroupId(101);
        lightMessage.setRepeat(3);
        lightMessage.setTime(System.currentTimeMillis());
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("a","123");
        paramMap.put("b","456");
        paramMap.put("c","789");
        lightMessage.setParamMap(paramMap);
        String s = JsonUtil.toJSONString(lightMessage);
        System.out.println("s:" + s);
        LightMessage lightMessage2 = JsonUtil.toJavaObject(s, LightMessage.class);
        System.out.println("lighthouseMessage2:" + lightMessage2);
    }

    @Test
    public void test4() throws Exception{
        LightMessage lightMessage = new LightMessage();
        lightMessage.setGroupId(101);
        lightMessage.setRepeat(3);
        lightMessage.setTime(System.currentTimeMillis());
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("a","123");
        paramMap.put("b","456");
        paramMap.put("c","789");
        lightMessage.setParamMap(paramMap);
        String s = JsonUtil.toJSONString(lightMessage);
        System.out.println("str:" + s);
        LightMessage lightMessage2 = JsonUtil.toJavaObject(s, LightMessage.class);
        System.out.println("lighthouseMessage2:" + lightMessage2);
        System.out.println("str2:" + JsonUtil.toJSONString(lightMessage2));
    }

    @Test
    public void test5() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode parentNode = objectMapper.createObjectNode();
        parentNode.put("b","asgag");
        String s = parentNode.toString();
        List<LightMessage> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            LightMessage lightMessage = new LightMessage();
            lightMessage.setRepeat(3);
            lightMessage.setGroupId(333);
            lightMessage.setTime(System.currentTimeMillis());
            HashMap<String,String> paramMap = new HashMap<>();
            paramMap.put("a","123");
            paramMap.put("b","456");
            list.add(lightMessage);
        }
        ObjectMapper objectMapper2 = new ObjectMapper();
        String str = JsonUtil.toJSONString(list);
        System.out.println("str:" + str);
        List<LightMessage> lendReco = objectMapper2.readValue(str,new TypeReference<List<LightMessage>>(){});
        System.out.println("lendReco:" + lendReco.size());
        JsonNode arrNode = new ObjectMapper().readTree(str);
        System.out.println("arrNode:" + arrNode.isArray());
        int size = arrNode.size();
        System.out.println("size:" + size);

        int t = arrNode.get(0).get("groupId").intValue();
        System.out.println("t:" + t);
    }
}
