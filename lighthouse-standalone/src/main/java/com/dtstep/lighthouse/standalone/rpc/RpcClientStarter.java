package com.dtstep.lighthouse.standalone.rpc;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.standalone.rpc.provider.StandaloneRemoteService;

public class RpcClientStarter {
    public static void main(String[] args) throws Exception {
        StandaloneRemoteService rpc = RpcProxy.create(StandaloneRemoteService.class);
        for(int i = 0;i<100;i++){
            GroupVerifyEntity groupVerifyEntity = rpc.queryGroupInfo("Gjd:feed_behavior_stat");
            System.out.println("groupVerifyEntity:" + JsonUtil.toJSONString(groupVerifyEntity));
        }
    }
}
