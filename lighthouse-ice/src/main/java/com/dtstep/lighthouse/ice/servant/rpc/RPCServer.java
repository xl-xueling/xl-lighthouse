package com.dtstep.lighthouse.ice.servant.rpc;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;

public interface RPCServer {

    GroupVerifyEntity queryGroup(String token) throws Exception;

    void process(byte[] bytes) throws Exception;
}
