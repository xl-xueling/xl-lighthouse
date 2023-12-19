package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.UserDao;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.insights.modal.User;
import org.eclipse.jetty.util.ajax.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestUserDao {

    @Autowired
    private UserDao userDao;

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
        userDao.insert(user);
    }

    @Test
    public void testQueryById() throws Exception {
        User user = userDao.queryById(110137);
        System.out.println("user:" + JsonUtil.toJSONString(user));
    }

    @Test
    public void testQueryByUserName() throws Exception {
        User user = userDao.queryByUserName("123");
        System.out.println("user:" + JsonUtil.toJSONString(user));
    }

    @Test
    public void testQueryList() throws Exception {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setId(110137);
        List<User> users = userDao.queryList(userQueryParam,1,10);
        System.out.println("data:" + JsonUtil.toJSONString(users));
    }

    @Test
    public void testUpdate() throws Exception {
        User user = new User();
        user.setPhone("150111");
        user.setId(110137);
        int result = userDao.update(user);
        System.out.println("result:" + result);
    }

    @Test
    public void changePassword() throws Exception {
        int result = userDao.changePasswd(110137, "123654");
        System.out.println("result:" + result);
    }

    @Test
    public void changeState() throws Exception {
        int result = userDao.changeState(110137, UserStateEnum.USER_FREEZE);
        System.out.println("result:" + result);
    }
}
