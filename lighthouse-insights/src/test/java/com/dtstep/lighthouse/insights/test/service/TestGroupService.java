package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dto.DimensValueDeleteParam;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import com.dtstep.lighthouse.insights.vo.GroupVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TestGroupService {

    @Autowired
    private GroupService groupService;

    @Test
    public void testQueryById() throws Exception{
        int groupId = 100285;
        GroupVO groupVo = groupService.queryById(groupId);
        System.out.println("groupVo is:" + JsonUtil.toJSONString(groupVo));
    }

    @Test
    public void testQueryDimensList() throws Exception {
        int groupId = 100309;
        List<String> list = groupService.queryDimensList(groupId);
        for(String dimens : list){
            System.out.println("dimens:" + dimens);
        }
    }

    @Test
    public void testQueryDimensValuesList() throws Exception {
        int groupId = 100309;
        List<String> list = groupService.queryDimensValueList(groupId,"behavior_type");
        for(String dimensValue : list){
            System.out.println("dimensValue:" + dimensValue);
        }
    }

    @Test
    public void testDeleteDimensValue() throws Exception {
        int groupId = 100309;
        List<DimensValueDeleteParam> deleteParams = new ArrayList<>();
        DimensValueDeleteParam deleteParam = new DimensValueDeleteParam();
        deleteParam.setDimensValue("2");
        deleteParam.setGroupId(groupId);
        deleteParam.setDimens("behavior_type");
        deleteParams.add(deleteParam);
        groupService.deleteDimensValue(deleteParams);
        List<String> list = groupService.queryDimensValueList(groupId,"behavior_type");
        for(String dimensValue : list){
            System.out.println("dimensValue:" + dimensValue);
        }
    }

    @Test
    public void testClearDimensValue() throws Exception {
        int groupId = 100309;
        groupService.clearDimensValue(groupId);
    }
}
