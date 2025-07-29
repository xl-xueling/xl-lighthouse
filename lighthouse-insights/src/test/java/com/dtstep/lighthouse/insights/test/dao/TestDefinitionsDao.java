package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.enums.DefinitionsEnum;
import com.dtstep.lighthouse.common.modal.Definition;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.DefinitionsDao;
import com.dtstep.lighthouse.insights.dto.DefinitionsQueryParam;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TestDefinitionsDao {

    @Autowired
    private DefinitionsDao definitionsDao;

    @Test
    public void testCreate() throws Exception {
        Definition definitions = new Definition();
        definitions.setCreateTime(LocalDateTime.now());
        definitions.setUpdateTime(LocalDateTime.now());
        definitions.setDesc("test");
        definitions.setName("test");
        definitions.setUserId(1);
        definitions.setExtend("extend");
        definitions.setType(DefinitionsEnum.VIEW_CATEGORY);
        definitionsDao.insert(definitions);
    }

    @Test
    public void testQueryList() throws Exception {
        DefinitionsQueryParam queryParam = new DefinitionsQueryParam();
        queryParam.setType(DefinitionsEnum.VIEW_CATEGORY);
        List<Definition> list = definitionsDao.queryList(queryParam);
        System.out.println("list:" + JsonUtil.toJSONString(list));
    }

    @Test
    public void testDelete() throws Exception {
        int id = 1;
        definitionsDao.deleteById(id);
    }
}
