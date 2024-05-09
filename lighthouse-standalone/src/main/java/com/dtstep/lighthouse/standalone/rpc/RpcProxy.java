package com.dtstep.lighthouse.standalone.rpc;

import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.serializer.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProxy {
    public static <T> T create(Class<?> clazz){
        MethodProxy proxy = new MethodProxy(clazz);
        Class<?> [] interfaces = clazz.isInterface() ?
                new Class[]{clazz} :
                clazz.getInterfaces();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),interfaces,proxy);
    }

    private static final CustomIdleStateHandler customIdleStateHandler = new CustomIdleStateHandler();

    public static class MethodProxy implements InvocationHandler {

        private final Class<?> clazz;

        public MethodProxy(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if(Object.class.equals(method.getDeclaringClass())){
                return method.invoke(this,args);
            }else{
                return invoke(method,args);
            }
        }
        private Object invoke(Method method, Object[] args) {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setRequestId(RandomID.id(32));
            rpcRequest.setClassName(this.clazz.getName());
            rpcRequest.setMethodName(method.getName());
            rpcRequest.setParameterTypes(method.getParameterTypes());
            rpcRequest.setParameterValues(args);
            final NettyClientHandler clientHandler = new NettyClientHandler();
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap client = new Bootstrap();
                client.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer() {
                            @Override
                            protected void initChannel(Channel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                int fieldLength = 4;
                                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,fieldLength,0,fieldLength));
                                pipeline.addLast(new LengthFieldPrepender(fieldLength));
                                pipeline.addLast(customIdleStateHandler);
                                pipeline.addLast("encoder",new RpcEncoder(RpcRequest.class,new KryoSerializer()));
                                pipeline.addLast("decoder",new RpcDecoder(RpcResponse.class,new KryoSerializer()));
                                pipeline.addLast("handler",clientHandler);
                            }
                        })
                        .option(ChannelOption.TCP_NODELAY, true);
                ChannelFuture future = client.connect("localhost",8081).sync();
                future.channel().writeAndFlush(rpcRequest).sync();
                future.channel().closeFuture().sync();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                group.shutdownGracefully();
            }
            return clientHandler.getResponse();
        }
    }
}