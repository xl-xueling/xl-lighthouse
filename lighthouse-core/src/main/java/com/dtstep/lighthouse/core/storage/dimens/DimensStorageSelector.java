package com.dtstep.lighthouse.core.storage.dimens;

import com.dtstep.lighthouse.common.entity.event.DimensBucket;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.core.storage.dimens.impl.DefaultDimensStorageHandler;

import java.util.List;

public class DimensStorageSelector {

    private static final DimensStorageHandler<DimensBucket,String> dimensStorageHandler = new DefaultDimensStorageHandler();

    public static void put(List<DimensBucket> bucketList) throws Exception{
        dimensStorageHandler.put(bucketList);
    }

    public static List<String> query(Group group, String dimens, String lastDimensValue, int limit) throws Exception {
        return dimensStorageHandler.query(group,dimens,lastDimensValue,limit);
    }
}
