package com.dtstep.lighthouse.core.storage.dimens;

import com.dtstep.lighthouse.common.entity.event.DimensBucket;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.core.storage.dimens.impl.DefaultDimensStorageHandler;

import java.util.List;

public class DimensStorageAdapter {

    private static final DimensStorageHandler<DimensBucket,String> dimensStorageHandler = new DefaultDimensStorageHandler();

    public static void increment(List<DimensBucket> bucketList) throws Exception{
        dimensStorageHandler.put(bucketList);
    }

    List<String> query(Group group, String dimens, String lastDimensValue, int limit) throws Exception {
        return dimensStorageHandler.query(group,dimens,lastDimensValue,limit);
    }
}
