package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.UserDao;
import com.dtstep.lighthouse.insights.enums.ColumnTypeEnum;
import com.dtstep.lighthouse.insights.modal.Column;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.User;
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
        Map<String,String> debugParams = new HashMap<>();
        debugParams.put("abc","123");
        debugParams.put("ABWE","456");
        group.setDebugParams(debugParams);
        group.setColumns(columnList);
        groupDao.insert(group);
    }

    @Test
    public void testQueryById() throws Exception {
        int id = 100161;
        Group group = groupDao.queryById(id);
        System.out.println("group columns:" + group.getColumns());
        System.out.println("column:" + group.getColumns().get(0).getComment());
        System.out.println("group is:" + JsonUtil.toJSONString(group));
    }
}
