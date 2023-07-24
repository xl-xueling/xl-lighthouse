package com.dtstep.lighthouse.web.service.order;

import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.order.OrderEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;

public interface ApproveService {

    ListViewDataObject queryListByPage(UserEntity currentUser, int page, int state, String search) throws Exception;

    void approve(UserEntity currentUser,OrderEntity orderEntity) throws Exception;

    void reject(UserEntity currentUser,OrderEntity orderEntity) throws Exception;

    int countApproveByParam(UserEntity currentUser, int state, String search) throws Exception;
}
