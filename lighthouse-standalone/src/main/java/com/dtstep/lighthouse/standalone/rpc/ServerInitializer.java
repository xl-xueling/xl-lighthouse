package com.dtstep.lighthouse.standalone.rpc;

import com.dtstep.lighthouse.common.serializer.KryoSerializer;
import com.dtstep.lighthouse.standalone.rpc.RpcDecoder;
import com.dtstep.lighthouse.standalone.rpc.RpcEncoder;
import com.dtstep.lighthouse.standalone.rpc.RpcRequest;
import com.dtstep.lighthouse.standalone.rpc.RpcResponse;
import com.dtstep.lighthouse.standalone.rpc.pool4.ServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final CustomIdleStateHandler customIdleStateHandler = new CustomIdleStateHandler();

    private static final NettyServerHandler nettyServerHandler = new NettyServerHandler();

    @Override
    protected void initChannel(SocketChannel ch) {
        int fieldLength = 4;
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(customIdleStateHandler);
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,fieldLength,0,fieldLength));
        pipeline.addLast(new LengthFieldPrepender(fieldLength));
        pipeline.addLast("encoder",new RpcEncoder(RpcResponse.class,new KryoSerializer()));
        pipeline.addLast("decoder",new RpcDecoder(RpcRequest.class,new KryoSerializer()));
        pipeline.addLast(nettyServerHandler);
    }
}
