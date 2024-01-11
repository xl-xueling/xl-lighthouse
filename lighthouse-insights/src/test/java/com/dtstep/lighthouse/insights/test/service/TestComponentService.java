package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dto.TreeNode;
import com.dtstep.lighthouse.insights.service.ComponentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestComponentService {

    @Autowired
    private ComponentService componentService;

    @Test
    public void testVerify() {
        String s = "[\n" +
                "  {\n" +
                "    \"label\": \"Category 1\",\n" +
                "    \"value\": \"category1\",\n" +
                "    \"children\": [\n" +
                "      {\n" +
                "        \"label\": \"Subcategory 1.1\",\n" +
                "        \"value\": \"subcategory1.1\",\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"label\": \"Item 1.1.1\",\n" +
                "            \"value\": \"item1.1.1\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"label\": \"Item 1.1.2\",\n" +
                "            \"value\": \"item1.1.2\"\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"label\": \"Subcategory 1.2\",\n" +
                "        \"value\": \"subcategory1.2\",\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"label\": \"Item 1.2.1\",\n" +
                "            \"value\": \"item1.2.1\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"label\": \"Item 1.2.2\",\n" +
                "            \"value\": \"item1.2.2\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"label\": \"Category 2\",\n" +
                "    \"value\": \"category2\",\n" +
                "    \"children\": [\n" +
                "      {\n" +
                "        \"label\": \"Subcategory 2.1\",\n" +
                "        \"value\": \"subcategory2.1\",\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"label\": \"Item 2.1.1\",\n" +
                "            \"value\": \"item2.1.1\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"label\": \"Item 2.1.2\",\n" +
                "            \"value\": \"item2.1.2\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";


        String s2 = "[\n" +
                "  {\n" +
                "    \"label\": \"Category 2\",\n" +
                "    \"value\": \"category2\",\n" +
                "    \"children\": [\n" +
                "      {\n" +
                "        \"label\": \"Subcategory 2.1\",\n" +
                "        \"value\": \"subcategory2.1\",\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"label\": \"Item 2.1.1\",\n" +
                "            \"value\": \"item2.1.1\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"label\": \"Item 2.1.2\",\n" +
                "            \"value\": \"item2.1.2\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";
        ResultCode resultCode = componentService.verify(s);
        System.out.println("resultCode:" + JsonUtil.toJSONString(resultCode));
        System.out.println("resultCode:" + JsonUtil.toJSONString(resultCode.getParams()));
    }
}
