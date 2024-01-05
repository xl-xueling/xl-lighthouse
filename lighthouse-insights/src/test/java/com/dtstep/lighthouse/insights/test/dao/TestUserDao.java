package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.UserDao;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.insights.dto.UserUpdateParam;
import com.dtstep.lighthouse.insights.modal.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
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
        List<Integer> departmentIdList = List.of(10180,10182);
        userQueryParam.setDepartmentIds(departmentIdList);
        PageHelper.startPage(1,10);
        List<User> users = userDao.queryList(userQueryParam);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        System.out.println("pageInfo:" + pageInfo.getTotal());
        System.out.println("data:" + JsonUtil.toJSONString(users));
    }

    @Test
    public void testUpdate() throws Exception {
        UserUpdateParam user = new UserUpdateParam();
        user.setId(110183);
        user.setState(UserStateEnum.USR_NORMAL);
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
        int result = userDao.changeState(110137, UserStateEnum.USER_FROZEN);
        System.out.println("result:" + result);
    }

    @Test
    public void testCount() throws Exception {
        UserQueryParam userQueryParam = new UserQueryParam();
        int count = userDao.count(userQueryParam);
        System.out.println("count:" + count);
    }

    @Test
    public void testTermQuery() throws Exception {
        String s = "test";
        List<User> users = userDao.termQuery(s);
        System.out.println("users:" + JsonUtil.toJSONString(users));
    }
}
