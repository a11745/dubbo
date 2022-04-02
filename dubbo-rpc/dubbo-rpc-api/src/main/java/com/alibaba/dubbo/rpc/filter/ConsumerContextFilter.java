/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;

/**
 * 它是怎么将客户端设置的隐式参数传递给服务端,载体就是Invocation对象，在客户端调用Invoker.invoke方法时候，会去取当前状态记录器RpcContext中的attachments属性，然后设置到RpcInvocation对象中，在RpcInvocation传递到provider的时候会通过另外一个过滤器ContextFilter将RpcInvocation对象重新设置回RpcContext中供服务端逻辑重新获取隐式参数。这就是为什么RpcContext只能记录一次请求的状态信息，因为在第二次调用的时候参数已经被新的RpcInvocation覆盖掉，第一次的请求信息对于第二次执行是不可见的。
 * ConsumerContextInvokerFilter
 *
 * 服务消费者的 ContextFilter
 */
@Activate(group = Constants.CONSUMER, order = -10000)
public class ConsumerContextFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // 设置 RpcContext 对象 在当前的RpcContext中记录本地调用的一次状态信息
        RpcContext.getContext()
                .setInvoker(invoker)
                .setInvocation(invocation)
                .setLocalAddress(NetUtils.getLocalHost(), 0) // 本地地址
                .setRemoteAddress(invoker.getUrl().getHost(), invoker.getUrl().getPort()); // 远程地址
        // 设置 RpcInvocation 对象的 `invoker` 属性
        if (invocation instanceof RpcInvocation) {
            ((RpcInvocation) invocation).setInvoker(invoker);
        }
        // 服务调用
        try {
            return invoker.invoke(invocation);
        } finally {
            // 清理隐式参数集合
            RpcContext.getContext().clearAttachments();
        }
    }

}