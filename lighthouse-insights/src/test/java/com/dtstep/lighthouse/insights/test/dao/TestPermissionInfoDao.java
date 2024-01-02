package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.modal.Permission;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestPermissionInfoDao {

    @Autowired
    private PermissionDao permissionDao;

    @Test
    public void testCreate(){
        Permission permission = new Permission();
        LocalDateTime localDateTime = LocalDateTime.now();
        permission.setCreateTime(localDateTime);
        permission.setUpdateTime(localDateTime);
        permission.setOwnerType(OwnerTypeEnum.USER);
        permission.setRoleId(1);
        int result = permissionDao.insert(permission);
        System.out.println("result:" + result);
    }

    @Test
    public void testBatchInsert(){
        List<Permission> permissionList = new ArrayList<>();
        for(int i=0;i<3;i++){
           Permission permission = new Permission(i,OwnerTypeEnum.USER,i);
           permission.setUpdateTime(LocalDateTime.now());
           permission.setCreateTime(LocalDateTime.now());
           permissionList.add(permission);
        }
        permissionDao.batchInsert(permissionList);
    }

}
