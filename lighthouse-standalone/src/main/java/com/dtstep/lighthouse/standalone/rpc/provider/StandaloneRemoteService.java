package com.dtstep.lighthouse.standalone.rpc.provider;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.ice.LightRpcException;

public interface StandaloneRemoteService {

    void process(byte[] bytes) throws LightRpcException;

    GroupVerifyEntity queryGroupInfo(String token) throws LightRpcException;

    StatVerifyEntity queryStatInfo(int id) throws LightRpcException;
}
