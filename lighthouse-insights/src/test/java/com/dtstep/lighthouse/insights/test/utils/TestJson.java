package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.modal.Group;
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


}
