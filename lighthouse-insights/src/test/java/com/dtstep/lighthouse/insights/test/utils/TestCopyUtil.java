package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.dto.UserCreateParam;
import com.dtstep.lighthouse.insights.modal.User;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import java.time.LocalDateTime;

public class TestCopyUtil {

    @Test
    public void testCopy() throws Exception {
        UserCreateParam createParam = new UserCreateParam();
        createParam.setCreateTime(LocalDateTime.now());
        createParam.setUpdateTime(LocalDateTime.now());
        createParam.setUsername("xueling");
        createParam.setEmail("xuliwasd");
        createParam.setDepartmentId(Integer.valueOf(1));
        createParam.setPhone("12345");
        User user = new User();
        BeanUtils.copyProperties(user,createParam);
        System.out.println("user is:" + JsonUtil.toJSONString(user));
        createParam.setUsername("xueling22222");
        createParam.setDepartmentId(Integer.valueOf(2));
        System.out.println("user is:" + JsonUtil.toJSONString(user));
        User user2 = new User();
        BeanCopyUtil.copy(createParam,user2);
        System.out.println("user is:" + JsonUtil.toJSONString(user2));
    }
}
