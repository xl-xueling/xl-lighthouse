package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.service.ComponentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestComponentService {

    @Autowired
    private ComponentService componentService;

    @Test
    public void testVerify() {
        String s2 = "[{\n" +
                "\t\t\"label\": \"Category 1\",\n" +
                "\t\t\"value\": \"category1\",\n" +
                "\t\t\"children\": [{\n" +
                "\t\t\t\t\"label\": \"Subcategory 1.1\",\n" +
                "\t\t\t\t\"value\": \"subcategory1.1\",\n" +
                "\t\t\t\t\"children\": [{\n" +
                "\t\t\t\t\t\t\"label\": \"Item 1.1.1\",\n" +
                "\t\t\t\t\t\t\"value\": \"item1.1.1\",\n" +
                "\t\t\t\t\t\t\"children\": [{\n" +
                "\t\t\t\t\t\t\t\"label\": \"Item 1.1.1.1\",\n" +
                "\t\t\t\t\t\t\t\"value\": \"item1.1.1.2\"\n" +
                "\t\t\t\t\t\t}]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"label\": \"Item 1.1.2\",\n" +
                "\t\t\t\t\t\t\"value\": \"item1.1.2\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"label\": \"Subcategory 1.2\",\n" +
                "\t\t\t\t\"value\": \"subcategory1.2\",\n" +
                "\t\t\t\t\"children\": [{\n" +
                "\t\t\t\t\t\t\"label\": \"Item 1.2.1\",\n" +
                "\t\t\t\t\t\t\"value\": \"item1.2.1\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"label\": \"Item 1.2.2\",\n" +
                "\t\t\t\t\t\t\"value\": \"item1.2.2\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"label\": \"Category 2\",\n" +
                "\t\t\"value\": \"category2\",\n" +
                "\t\t\"children\": [{\n" +
                "\t\t\t\"label\": \"Subcategory 2.1\",\n" +
                "\t\t\t\"value\": \"subcategory2.1\",\n" +
                "\t\t\t\"children\": [{\n" +
                "\t\t\t\t\t\"label\": \"Item 2.1.1\",\n" +
                "\t\t\t\t\t\"value\": \"item2.1.1\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"label\": \"Item 2.1.2\",\n" +
                "\t\t\t\t\t\"value\": \"item2.1.2\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t}]\n" +
                "\t}\n" +
                "]";
        ResultCode resultCode = componentService.verify(s2);
        System.out.println("resultCode:" + JsonUtil.toJSONString(resultCode));
        System.out.println("resultCode:" + JsonUtil.toJSONString(resultCode.getParams()));
    }
}
