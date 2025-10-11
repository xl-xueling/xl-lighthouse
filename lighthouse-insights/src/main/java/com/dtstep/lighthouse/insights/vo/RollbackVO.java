package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.modal.RollbackModal;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;

public class RollbackVO extends RollbackModal {

    private User createUser;

    public RollbackVO(RollbackModal rollbackModal){
        assert rollbackModal != null;
        BeanCopyUtil.copy(rollbackModal,this);
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }
}
