package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dto.GroupQueryParam;
import com.dtstep.lighthouse.common.enums.ColumnTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestGroupDao {

    @Autowired
    private GroupDao groupDao;

    @Test
    public void testCreateGroup() throws Exception {
        Group group = new Group();
        group.setToken("sss2");
        group.setState(GroupStateEnum.FROZEN);
        List<Column> columnList = new ArrayList<>();
        Column column = new Column();
        column.setName("province");
        column.setType(ColumnTypeEnum.STRING);
        column.setComment("省份");
        columnList.add(column);
        GroupExtendConfig groupExtendConfig = new GroupExtendConfig();
        DebugParam debugConfig = new DebugParam();
        debugConfig.setStartTime(System.currentTimeMillis());
        debugConfig.setEndTime(System.currentTimeMillis());
        group.setExtendConfig(groupExtendConfig);
        group.setColumns(columnList);
        groupDao.insert(group);
    }

    @Test
    public void testQueryById() throws Exception {
        int id = 100171;
        Group group = groupDao.queryById(id);
        System.out.println("group columns:" + group.getColumns());
        System.out.println("column:" + group.getColumns().get(0).getComment());
        System.out.println("group is:" + JsonUtil.toJSONString(group));
    }

    @Test
    public void testQueryByProjectId() throws Exception {
        Integer projectId = 11040;
        List<Group> groupList = groupDao.queryByProjectId(projectId);
        System.out.println("groupList:" + JsonUtil.toJSONString(groupList));
    }

    @Test
    public void testCount() throws Exception {
        GroupQueryParam queryParam = new GroupQueryParam();
        queryParam.setToken("homepage_avg_stat2");
        int size = groupDao.count(queryParam);
        System.out.println("size:" + size);
    }

    @Test
    public void testGetSecretKey() throws Exception {
        Integer id = 100215;
        String s = groupDao.getSecretKey(id);
        System.out.println("s:" + s);
    }
}
