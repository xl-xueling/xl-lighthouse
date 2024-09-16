package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.modal.View;
import com.dtstep.lighthouse.common.modal.ViewQueryParam;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.ViewDao;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
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
public class TestViewDao {

    @Autowired
    private ViewDao viewDao;

    @Test
    public void testQueryList() throws Exception {
        ViewQueryParam queryParam = new ViewQueryParam();
        queryParam.setOwnerId(110239);
        List<View> viewList =  viewDao.queryList(queryParam);
        for(int i=0;i<viewList.size();i++){
            View view = viewList.get(i);
            System.out.println("view is:" + JsonUtil.toJSONString(view));
        }
    }
}
