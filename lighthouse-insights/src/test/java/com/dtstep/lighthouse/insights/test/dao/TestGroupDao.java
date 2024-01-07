package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dto.GroupQueryParam;
import com.dtstep.lighthouse.insights.enums.ColumnTypeEnum;
import com.dtstep.lighthouse.insights.enums.LimitedStrategyEnum;
import com.dtstep.lighthouse.insights.modal.*;
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
        DebugConfig debugConfig = new DebugConfig();
        debugConfig.setStartTime(System.currentTimeMillis());
        debugConfig.setEndTime(System.currentTimeMillis());
        List<LimitedConfig> configList = new ArrayList<>();
        LimitedConfig limitedConfig1 = new LimitedConfig();
        limitedConfig1.setStrategy(LimitedStrategyEnum.GROUP_MESSAGE_LIMITED_STRATEGY);
        limitedConfig1.setThreshold(100);
        configList.add(limitedConfig1);
        LimitedConfig limitedConfig2 = new LimitedConfig();
        limitedConfig2.setStrategy(LimitedStrategyEnum.STAT_RESULT_LIMITED_STRATEGY);
        limitedConfig2.setThreshold(500);
        configList.add(limitedConfig2);
        groupExtendConfig.setLimitedConfig(configList);
        groupExtendConfig.setDebugConfig(debugConfig);
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
}
