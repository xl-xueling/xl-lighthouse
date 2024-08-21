package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.enums.RelationTypeEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dto.RelationQueryParam;
import com.dtstep.lighthouse.insights.service.RelationService;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import com.dtstep.lighthouse.insights.vo.RelationVO;
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
public class TestRelationService {

    @Autowired
    private RelationService relationService;

    @Test
    public void testQueryList() throws Exception {
        RelationQueryParam relationQueryParam = new RelationQueryParam();
        relationQueryParam.setRelationType(RelationTypeEnum.MetricSetBindRelation);
        relationQueryParam.setSubjectId(100011);
        ListData<RelationVO> voList = relationService.queryList(relationQueryParam,1,10);
        System.out.println("voList is:" + JsonUtil.toJSONString(voList));
    }
}
