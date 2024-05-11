package com.dtstep.lighthouse.common.rpc.netty;

import com.dtstep.lighthouse.common.entity.rpc.RpcResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessedFuture {

    private static final ConcurrentHashMap<String, CompletableFuture<RpcResponse<?>>> futureMap = new ConcurrentHashMap<>();

    public static void put(String id, CompletableFuture<RpcResponse<?>> future) {
        futureMap.put(id, future);
    }

    public static CompletableFuture<RpcResponse<?>> remove(String id) {
        return futureMap.remove(id);
    }
}
