package com.dtstep.lighthouse.core.test.serialzation;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.TimeParam;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.SerializeUtil;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class SerialzationTest {

    @Test
    public void test() throws Exception {
        GroupVerifyEntity groupVerifyEntity = new GroupVerifyEntity();
        TimeParam timeParam = new TimeParam();
        timeParam.setTimeUnit(TimeUnit.HOURS);
        timeParam.setDuration(1000);
        timeParam.setInterval(3);
        groupVerifyEntity.setMinTimeParam(timeParam);
        byte[] bytes = SerializeUtil.serialize(groupVerifyEntity);
        GroupVerifyEntity groupVerifyEntity1 = SerializeUtil.deserialize(bytes);
        System.out.println("group:" + JsonUtil.toJSONString(groupVerifyEntity1));


        String s = "asdaasdgasgasdg";
        byte [] bytes1 = SerializeUtil.serialize(s);
        String v = SerializationUtils.deserialize(bytes1);
        System.out.println("v is:" + v);
    }
}
