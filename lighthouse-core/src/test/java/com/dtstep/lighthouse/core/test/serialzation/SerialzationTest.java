package com.dtstep.lighthouse.core.test.serialzation;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.TimeParam;
import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.common.serializer.HessianSerializer;
import com.dtstep.lighthouse.common.serializer.KryoSerializer;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.SerializeUtil;
import com.esotericsoftware.kryo.Kryo;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.time.LocalDateTime;
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

    @Test
    public void testSerializer() throws Exception {
        User user = new User();
        user.setCreateTime(LocalDateTime.now());
        user.setDepartmentId(1);
        user.setState(UserStateEnum.USER_PEND);
        user.setUsername("test");
        user.setEmail("test@email.com");
        user.setId(3);
        user.setUpdateTime(LocalDateTime.now());


        User user1 = new User();
        user1.setDepartmentId(1);

        long t1 = System.currentTimeMillis();
        for(int i = 0 ;i<100000;i++){
            SerializeUtil.serialize(user);
        }
        long t2 = System.currentTimeMillis();

        HessianSerializer serializer = new HessianSerializer();
        KryoSerializer kryoSerializer = new KryoSerializer();
        long t3 = System.currentTimeMillis();
        for(int i = 0 ;i<100000;i++){
            kryoSerializer.serialize(user1);
        }
        long t4 = System.currentTimeMillis();

        System.out.println("cost1:" + (t2 - t1) + ",cost2:" + (t4 - t3));
    }

}
