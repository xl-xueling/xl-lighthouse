package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.UserDao;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
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
        boolean isExist = userDao.isUserNameExist(SysConst.DEFAULT_ADMIN_USER);
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
        User user = new User();
        user.setId(110183);
        user.setState(UserStateEnum.USER_NORMAL);
        user.setPhone("150111");
        int result = userDao.update(user);
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

    @Test
    public void testTermQueryByIds() throws Exception {
        List<Integer> ids = List.of(110239);
        List<User> users = userDao.termQueryByIds(ids);
        System.out.println("user info is:" + JsonUtil.toJSONString(users));
    }
}
