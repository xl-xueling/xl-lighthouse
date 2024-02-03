package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.service.PermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestPermissionService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionDao permissionDao;

    @Test
    public void testGrantPermission(){
        int result = permissionService.grantPermission(110194, OwnerTypeEnum.USER,1);
        System.out.println("result:" + result);
    }

    @Test
    public void testDeletePermission() throws Exception {
        PermissionQueryParam queryParam = new PermissionQueryParam();
        queryParam.setRoleId(402);
        int id = permissionDao.delete(queryParam);
        System.out.println("id:" + id);
    }
}
