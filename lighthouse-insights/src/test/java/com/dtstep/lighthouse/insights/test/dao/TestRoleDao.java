package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.RoleDao;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestRoleDao {

    @Autowired
    private RoleDao roleDao;

    @Test
    public void testCreate() throws Exception {
        Role role = new Role();
        LocalDateTime localDateTime = LocalDateTime.now();
        role.setCreateTime(localDateTime);
        role.setUpdateTime(localDateTime);
        role.setRoleType(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION);
        int result = roleDao.insert(role);
        System.out.println("result:" + result);
    }

    @Test
    public void testQueryRole() throws Exception{
        Role role = roleDao.queryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,11050);
        System.out.println("role:" + JsonUtil.toJSONString(role));
    }

    @Test
    public void testQueryList() throws Exception {
        List<Role> roleList = roleDao.queryListByPid(0,1,100);
        System.out.println("roleList:" + JsonUtil.toJSONString(roleList));
    }
}
