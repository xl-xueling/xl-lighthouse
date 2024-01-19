package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.dto_bak.ListSearchObject;
import com.dtstep.lighthouse.insights.dto.ApproveOrderQueryParam;
import com.dtstep.lighthouse.insights.modal.Stat;
import org.junit.Test;

import java.time.LocalDateTime;

public class TestJson {

    @Test
    public void testJson() {
        Stat stat = new Stat();
        stat.setCreateTime(LocalDateTime.now());
        String s = JsonUtil.toJSONString(stat);
        System.out.println("s is:" + s);
    }

    @Test
    public void testJsonParam() throws Exception {
//        String json = "{\n" +
//                "\t\"pagination\": {\n" +
//                "\t\t\"pageSize\": 1,\n" +
//                "\t\t\"pageNum\": 2\n" +
//                "\t}\n" +
//                "}";
//        ListSearchObject<OrderQueryParam> searchObject = JsonUtil.toJavaObject(json,ListSearchObject.class);
//        System.out.println("searchObject:" + JsonUtil.toJSONString(searchObject.getPagination()));
//        System.out.println("queryParam:" + JsonUtil.toJSONString(searchObject.getQueryParams()));

        ListSearchObject<ApproveOrderQueryParam> o1 = new ListSearchObject<>();
        System.out.println(o1.getQueryParams());
    }


}
