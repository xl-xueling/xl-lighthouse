package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.enums.RelationTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Indicator;
import com.dtstep.lighthouse.common.modal.MetricBindElement;
import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dto.MetricBindParam;
import com.dtstep.lighthouse.insights.dto.MetricPendQueryParam;
import com.dtstep.lighthouse.insights.dto.RelationQueryParam;
import com.dtstep.lighthouse.insights.service.MetricSetService;
import com.dtstep.lighthouse.insights.service.RelationService;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import com.dtstep.lighthouse.insights.vo.MetricSetVO;
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
public class TestMetricSetService {

    @Autowired
    private MetricSetService metricSetService;

    @Autowired
    private RelationService relationService;

    @Test
    public void binded() throws Exception {
        MetricBindParam bindParam = new MetricBindParam();
        bindParam.setMetricIds(List.of(100011));
        MetricBindElement bindElement = new MetricBindElement();
        bindElement.setResourceId(17);
        bindElement.setResourceType(ResourceTypeEnum.View);
        bindParam.setBindElements(List.of(bindElement));
        metricSetService.binded(bindParam);
    }

    @Test
    public void testGetStructure() throws Exception {
        int id = 100011;
        MetricSetVO metricSetVO = metricSetService.queryById(id);
        TreeNode nodes = metricSetService.getStructure(metricSetVO);
        System.out.println("treeNode is:" + JsonUtil.toJSONString(nodes));
    }

    @Test
    public void testQueryIndicators() throws Exception {
        int id = 100012;
        MetricPendQueryParam queryParam = new MetricPendQueryParam();
        queryParam.setId(id);
        ListData<Indicator> indicators = metricSetService.queryIndicatorList(queryParam,1,15);
        System.out.println("indicators:" + JsonUtil.toJSONString(indicators));
    }

    @Test
    public void testBindList() throws Exception {
        RelationQueryParam relationQueryParam = new RelationQueryParam();
        relationQueryParam.setRelationType(RelationTypeEnum.MetricSetBindRelation);
        relationQueryParam.setSubjectId(100012);
        ListData<RelationVO> listData = relationService.queryList(relationQueryParam, 1, 15);
        System.out.println("listData is:" + listData.getTotal());
    }
}
