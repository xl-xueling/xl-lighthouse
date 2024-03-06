package com.dtstep.lighthouse.core.storage.dimens;

import com.dtstep.lighthouse.common.modal.Group;

import java.util.List;

public interface DimensStorageHandler<W,R> {

    void put(List<W> list) throws Exception;

    List<R> queryDimensList(Group group, String dimens, String lastDimensValue, int limit) throws Exception;
}
