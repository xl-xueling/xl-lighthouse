package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.modal.AuthRecord;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.service.PermissionService;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import com.dtstep.lighthouse.insights.vo.PermissionVO;
import com.dtstep.lighthouse.insights.vo.ResourceVO;
import net.minidev.json.JSONUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
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
    public void testExtendPermission(){
        int permissionId = 100809;
    }

    @Test
    public void testDeletePermission() throws Exception {
        PermissionQueryParam queryParam = new PermissionQueryParam();
        queryParam.setRoleId(402);
        int id = permissionDao.delete(queryParam);
        System.out.println("id:" + id);
    }

    @Test
    public void testPermissionList() throws Exception {
        PermissionQueryParam permissionQueryParam = new PermissionQueryParam();
        permissionQueryParam.setOwnerId(11017);
        permissionQueryParam.setOwnerType(OwnerTypeEnum.CALLER);
        ListData<AuthRecord> listData = permissionService.queryOwnerAuthList(permissionQueryParam,1,10);
        List<AuthRecord> voList1 = listData.getList();
        for(AuthRecord authRecord : voList1){
            System.out.println("authRecord is:" + JsonUtil.toJSONString(authRecord));
        }
    }

    @Test
    public void testQueryAuthList() throws Exception {
        PermissionQueryParam queryParam = new PermissionQueryParam();
        queryParam.setOwnerType(OwnerTypeEnum.CALLER);
        queryParam.setOwnerId(11033);
        ListData<AuthRecord> list = permissionService.queryOwnerAuthList(queryParam,1,10);
        List<AuthRecord> voList = list.getList();
        for(AuthRecord authRecord : voList){
            System.out.println("authRecord is:" + JsonUtil.toJSONString(authRecord));
        }
    }

    @Test
    public void testExtensionPermission() throws Exception {
        int id = 100888;
        int expire = 300;
        int s = permissionService.extensionPermission(id,expire);
        System.out.println("s is:" + s);
    }
}
