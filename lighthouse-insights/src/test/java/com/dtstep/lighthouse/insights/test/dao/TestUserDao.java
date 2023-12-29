package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.UserDao;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.insights.dto.UserUpdateParam;
import com.dtstep.lighthouse.insights.modal.User;
import org.eclipse.jetty.util.ajax.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        LocalDateTime localDateTime = LocalDateTime.now();
        user.setCreateTime(localDateTime);
        user.setUpdateTime(localDateTime);
        user.setLastTime(localDateTime);
        user.setUsername("sss");
        user.setPassword("1235");
        user.setUsername("123522");
        user.setState(UserStateEnum.USER_DELETED);
        user.setDepartmentId(2);
        user.setEmail("sssss");
        userDao.insert(user);
    }

    @Test
    public void testIsUserNameExist() throws Exception {
        boolean isExist = userDao.isUserNameExist(SystemConstant.DEFAULT_ADMIN_USER);
        System.out.println("isExist:" + isExist);
    }

    @Test
    public void testQueryById() throws Exception {
        User user = userDao.queryById(110154);
        System.out.println("user:" + JsonUtil.toJSONString(user));
    }

    @Test
    public void testQueryByUserName() throws Exception {
        User user = userDao.queryById(110137);
        System.out.println("user:" + JsonUtil.toJSONString(user));
    }

    @Test
    public void testQueryList() throws Exception {
        UserQueryParam userQueryParam = new UserQueryParam();
//        List<Integer> statesList = new ArrayList<>();
//        statesList.add(0);
//        statesList.add(1);
//        statesList.add(2);
//        userQueryParam.setStates(statesList);
        List<Integer> departmentIdList = new ArrayList<>();
        userQueryParam.setDepartmentIds(departmentIdList);
        List<User> users = userDao.queryList(userQueryParam,1,10);
        System.out.println("data:" + JsonUtil.toJSONString(users));
    }

    @Test
    public void testUpdate() throws Exception {
        UserUpdateParam user = new UserUpdateParam();
        user.setId(110152);
        user.setPhone("150111");
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

    @Test
    public void testCount() throws Exception {
        UserQueryParam userQueryParam = new UserQueryParam();
        int count = userDao.count(userQueryParam);
        System.out.println("count:" + count);
    }
}
