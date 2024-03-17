package com.dtstep.lighthouse.ice.servant.logic;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;

public interface RPCServer {

    GroupVerifyEntity queryGroup(String token) throws Exception;

    void process(byte[] bytes) throws Exception;
}
