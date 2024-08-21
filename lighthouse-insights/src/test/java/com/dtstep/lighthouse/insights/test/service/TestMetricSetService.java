package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.MetricBindElement;
import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dto.MetricBindParam;
import com.dtstep.lighthouse.insights.service.MetricSetService;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import com.dtstep.lighthouse.insights.vo.MetricSetVO;
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
}
