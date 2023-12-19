package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.UserMapper;
import com.dtstep.lighthouse.insights.modal.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestUserDao {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setState(1);
        user.setCreatedTime(new Date());
        user.setLastTime(new Date());
        user.setPassword("123");
        user.setUsername("123");
        user.setDepartmentId(1);
        user.setEmail("ccssd");
        userMapper.insert(user);
    }
}
