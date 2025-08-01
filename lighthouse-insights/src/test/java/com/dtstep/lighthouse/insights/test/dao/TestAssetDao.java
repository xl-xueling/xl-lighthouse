package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.enums.AssetTypeEnum;
import com.dtstep.lighthouse.common.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.common.modal.Asset;
import com.dtstep.lighthouse.common.modal.AssetQueryParam;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.AssetDao;
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
public class TestAssetDao {

    @Autowired
    private AssetDao assetDao;

    @Test
    public void testInsert() throws Exception {
        Asset asset = new Asset();
        asset.setAssetType(AssetTypeEnum.HEADER);
        asset.setUserId(1);
        asset.setPrivateType(PrivateTypeEnum.Private);
        asset.setCreateTime(LocalDateTime.now());
        asset.setUpdateTime(LocalDateTime.now());
        asset.setPath("/uploads");
        assetDao.insert(asset);
    }

    @Test
    public void testQuery() throws Exception {
        AssetQueryParam queryParam = new AssetQueryParam();
        queryParam.setAssetType(AssetTypeEnum.HEADER);
        queryParam.setUserId(1);
        List<Asset> assetList = assetDao.queryList(queryParam);
        System.out.println("assetList is:" + JsonUtil.toJSONString(assetList));
    }
}
