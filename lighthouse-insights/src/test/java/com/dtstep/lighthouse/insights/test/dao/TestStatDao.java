package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.insights.modal.Stat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestStatDao {

    @Autowired
    private StatDao statDao;

    @Test
    public void testCreate(){
        Stat stat = new Stat();
        stat.setTemplate("<stat-item title=\"每分钟_uv统计\" stat=\"count()\"  dimens=\"province\"/>");
        stat.setTitle("每分钟_uv数据统计");
        stat.setExpired(10000L);
        stat.setGroupId(100182);
        stat.setProjectId(11040);
        stat.setTimeparam("1-hour");
        stat.setRandomId(UUID.randomUUID().toString());
        LocalDateTime localDateTime = LocalDateTime.now();
        stat.setCreateTime(localDateTime);
        stat.setUpdateTime(localDateTime);
        int id = statDao.insert(stat);
        System.out.println("id:" + id);
    }

    @Test
    public void testQueryById(){
        int id = 1100465;
        Stat stat = statDao.queryById(id);
        System.out.println("stat:" + JsonUtil.toJSONString(stat));
    }

    @Test
    public void testUpdateById(){
        Stat stat = new Stat();
        stat.setId(1100465);
        stat.setState(StatStateEnum.FROZEN);
        int result = statDao.update(stat);
        System.out.println("result:" + result);
    }
}
