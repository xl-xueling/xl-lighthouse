package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.modal.Group;
import org.junit.Test;

public class TestJson {

    @Test
    public void testJson() {
        String s = "{\"projectId\":\"11040\",\"token\":\"homage\",\"desc\":\"asdg\",\"columns\":[{\"type\":\"numberic\",\"name\":\"aaaa\",\"comment\":\"sssss\"}]}\n";
        Group group = JsonUtil.toJavaObject(s, Group.class);
        System.out.println("group:" + JsonUtil.toJSONString(group));
    }
}
