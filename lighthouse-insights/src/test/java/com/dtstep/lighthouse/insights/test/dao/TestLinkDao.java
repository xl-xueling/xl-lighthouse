package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.enums.LinkTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.enums.SwitchStateEnum;
import com.dtstep.lighthouse.common.modal.ShortLink;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.ShortLinkDao;
import com.dtstep.lighthouse.insights.dto.LinkQueryParam;
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
public class TestLinkDao {

    @Autowired
    private ShortLinkDao shortLinkDao;

    @Test
    public void testCreateLink() throws Exception {
        ShortLink shortLink = new ShortLink();
        LocalDateTime localDateTime = LocalDateTime.now();
        shortLink.setCreateTime(localDateTime);
        shortLink.setExpireTime(localDateTime);
        shortLink.setShortCode("Test");
        shortLink.setLinkType(LinkTypeEnum.VIEW_PUBLIC);
        shortLink.setFullUrl("https://dbstep.com/test/");
        shortLink.setResourceId(1);
        shortLink.setResourceType(ResourceTypeEnum.View);
        shortLink.setParams("Test");
        shortLinkDao.insert(shortLink);
    }

    @Test
    public void testUpdate() throws Exception {
        ShortLink shortLink = new ShortLink();
        shortLink.setState(SwitchStateEnum.OPEN);
        shortLink.setParams("test2");
        shortLink.setId(10009);
        shortLinkDao.update(shortLink);
    }

    @Test
    public void testQueryLink() throws Exception {
        ShortLink shortLink = shortLinkDao.queryById(10009);
        System.out.println("shortLink is:" + JsonUtil.toJSONString(shortLink));
    }

    @Test
    public void testQueryList() throws Exception {
        LinkQueryParam queryParam = new LinkQueryParam();
        queryParam.setLinkType(LinkTypeEnum.VIEW_PUBLIC);
        queryParam.setResourceId(1);
        List<ShortLink> links = shortLinkDao.queryList(queryParam);
        System.out.println("links is:" + JsonUtil.toJSONString(links));
    }
}
