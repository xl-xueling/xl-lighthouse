package com.dtstep.lighthouse.client.rpc;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;

public interface RPCClient {

    boolean init(String configuration) throws Exception;

    GroupVerifyEntity queryGroup(String token) throws Exception;

    void send(String text) throws Exception;

    void reconnect() throws Exception;
}
