package com.dtstep.lighthouse.standalone.rpc.provider.impl;

import com.dtstep.lighthouse.standalone.rpc.provider.IRpcService;

public class RpcServiceImpl implements IRpcService {

    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
