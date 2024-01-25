package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.RelationDao;
import com.dtstep.lighthouse.insights.dto.RelationQueryParam;
import com.dtstep.lighthouse.insights.enums.RelationTypeEnum;
import com.dtstep.lighthouse.insights.modal.Relation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestRelationDao {

    @Autowired
    private RelationDao relationDao;

    @Test
    public void testQueryList() throws Exception {
        RelationQueryParam queryParam = new RelationQueryParam();
        queryParam.setRelationType(RelationTypeEnum.MetricSetBindRelation);
        List<Relation> list =  relationDao.queryListByPage(queryParam);
        System.out.println("list:" + JsonUtil.toJSONString(list.size()));
    }
}
