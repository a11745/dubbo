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
package com.alibaba.dubbo.rpc.protocol.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.alibaba.dubbo.remoting.exchange.ResponseFuture;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.StaticContext;
import com.alibaba.dubbo.rpc.protocol.dubbo.FutureAdapter;
import com.alibaba.dubbo.rpc.support.RpcUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Future;

/**
 * EventFilter
 *
 * Future主要是处理事件信息，主要有以下几个事件：
 *
 * oninvoke 在方法调用前触发（如果调用出现异常则会直接触发onthrow方法）
 * onreturn 在方法返回会触发（如果调用出现异常则会直接触发onthrow方法）
 * onthrow 调用出现异常时候触发
 *
 * 事件通知过滤器，可参见文档《Dubbo 用户指南 —— 事件通知》https://dubbo.gitbooks.io/dubbo-user-book/demos/events-notify.html
 */
@Activate(group = Constants.CONSUMER)
public class FutureFilter implements Filter {

    protected static final Logger logger = LoggerFactory.getLogger(FutureFilter.class);

    @Override
    public Result invoke(final Invoker<?> invoker, final Invocation invocation) throws RpcException {
        // 获得是否异步调用
        final boolean isAsync = RpcUtils.isAsync(invoker.getUrl(), invocation);

        // 触发前置方法 这里主要处理回调逻辑，主要区分三个时间：oninvoke：调用前触发，onreturn：调用后触发 onthrow：出现异常情况时候触发
        fireInvokeCallback(invoker, invocation);
        // need to configure if there's return value before the invocation in order to help invoker to judge if it's
        // necessary to return future.
        // 调用方法 需要在调用前配置好是否有返回值，已供invoker判断是否需要返回future.
        Result result = invoker.invoke(invocation);

        // 触发回调方法
        if (isAsync) { // 异步回调
            asyncCallback(invoker, invocation);
        } else { // 同步回调
            syncCallback(invoker, invocation, result);
        }
        return result;
    }

    /**
     * 同步回调
     * 同步异步的主要处理区别就是同步调用的话，事件触发是直接调用的，没有任何逻辑；异步的话就是首先获取到调用产生的Future对象，然后复写Future的done（）方法，将fireThrowCallback和fireReturnCallback逻辑引入即可。
     *
     * @param invoker Invoker 对象
     * @param invocation Invocation 对象
     * @param result RPC 结果
     */
    private void syncCallback(final Invoker<?> invoker, final Invocation invocation, final Result result) {
        if (result.hasException()) { // 异常，触发异常回调
            fireThrowCallback(invoker, invocation, result.getException());
        } else { // 正常，触发正常回调
            fireReturnCallback(invoker, invocation, result.getValue());
        }
    }

    /**
     * 异步回调
     *
     * @param invoker Invoker 对象
     * @param invocation Invocation 对象
     */
    private void asyncCallback(final Invoker<?> invoker, final Invocation invocation) {
        // 获得 Future 对象
        Future<?> f = RpcContext.getContext().getFuture();
        if (f instanceof FutureAdapter) {
            ResponseFuture future = ((FutureAdapter<?>) f).getFuture();
            // 触发回调
            future.setCallback(new ResponseCallback() {

                /**
                 * 触发正常回调方法
                 *
                 * @param rpcResult RPC 结果
                 */
                public void done(Object rpcResult) {
                    if (rpcResult == null) {
                        logger.error(new IllegalStateException("invalid result value : null, expected " + Result.class.getName()));
                        return;
                    }
                    // must be rpcResult
                    if (!(rpcResult instanceof Result)) {
                        logger.error(new IllegalStateException("invalid result type :" + rpcResult.getClass() + ", expected " + Result.class.getName()));
                        return;
                    }
                    Result result = (Result) rpcResult;
                    if (result.hasException()) { // 触发正常回调方法
                        fireThrowCallback(invoker, invocation, result.getException());
                    } else { // 触发异常回调方法
                        fireReturnCallback(invoker, invocation, result.getValue());
                    }
                }

                /**
                 * 触发异常回调方法
                 *
                 * @param exception 异常
                 */
                public void caught(Throwable exception) {
                    fireThrowCallback(invoker, invocation, exception);
                }
            });
        }
    }

    /**
     * 触发前置方法
     *
     * @param invoker Invoker 对象
     * @param invocation Invocation 对象
     */
    private void fireInvokeCallback(final Invoker<?> invoker, final Invocation invocation) {
        // 获得前置方法和对象
        final Method onInvokeMethod = (Method) StaticContext.getSystemContext().get(StaticContext.getKey(invoker.getUrl(), invocation.getMethodName(), Constants.ON_INVOKE_METHOD_KEY));
        final Object onInvokeInst = StaticContext.getSystemContext().get(StaticContext.getKey(invoker.getUrl(), invocation.getMethodName(), Constants.ON_INVOKE_INSTANCE_KEY));
        if (onInvokeMethod == null && onInvokeInst == null) {
            return;
        }
        if (onInvokeMethod == null || onInvokeInst == null) {
            throw new IllegalStateException("service:" + invoker.getUrl().getServiceKey() + " has a onreturn callback config , but no such " + (onInvokeMethod == null ? "method" : "instance") + " found. url:" + invoker.getUrl());
        }
        //由于JDK的安全检查耗时较多.所以通过setAccessible(true)的方式关闭安全检查就可以达到提升反射速度的目的
        if (!onInvokeMethod.isAccessible()) {
            onInvokeMethod.setAccessible(true);
        }

        // 调用前置方法
        Object[] params = invocation.getArguments();
        try {
            onInvokeMethod.invoke(onInvokeInst, params);
        } catch (InvocationTargetException e) {
            fireThrowCallback(invoker, invocation, e.getTargetException());
        } catch (Throwable e) {
            fireThrowCallback(invoker, invocation, e);
        }
    }

    /**
     * 触发正常回调方法
     *
     * @param invoker Invoker 对象
     * @param invocation Invocation 对象
     * @param result RPC 结果
     */
    @SuppressWarnings("Duplicates")
    private void fireReturnCallback(final Invoker<?> invoker, final Invocation invocation, final Object result) {
        // 获得 `onreturn` 方法和对象
        final Method onReturnMethod = (Method) StaticContext.getSystemContext().get(StaticContext.getKey(invoker.getUrl(), invocation.getMethodName(), Constants.ON_RETURN_METHOD_KEY));
        final Object onReturnInst = StaticContext.getSystemContext().get(StaticContext.getKey(invoker.getUrl(), invocation.getMethodName(), Constants.ON_RETURN_INSTANCE_KEY));
        //not set onreturn callback
        if (onReturnMethod == null && onReturnInst == null) {
            return;
        }
        if (onReturnMethod == null || onReturnInst == null) {
            throw new IllegalStateException("service:" + invoker.getUrl().getServiceKey() + " has a onreturn callback config , but no such " + (onReturnMethod == null ? "method" : "instance") + " found. url:" + invoker.getUrl());
        }
        if (!onReturnMethod.isAccessible()) {
            onReturnMethod.setAccessible(true);
        }

        // 参数数组
        Object[] args = invocation.getArguments();
        Object[] params;
        Class<?>[] rParaTypes = onReturnMethod.getParameterTypes();
        if (rParaTypes.length > 1) {
            if (rParaTypes.length == 2 && rParaTypes[1].isAssignableFrom(Object[].class)) { // TODO 芋艿，泛化调用
                params = new Object[2];
                params[0] = result;
                params[1] = args;
            } else {
                params = new Object[args.length + 1];
                params[0] = result;
                System.arraycopy(args, 0, params, 1, args.length);
            }
        } else {
            params = new Object[]{result};
        }

        // 调用方法
        try {
            onReturnMethod.invoke(onReturnInst, params);
        } catch (InvocationTargetException e) {
            fireThrowCallback(invoker, invocation, e.getTargetException());
        } catch (Throwable e) {
            fireThrowCallback(invoker, invocation, e);
        }
    }

    /**
     * 触发异常回调方法
     * fireReturnCallback的逻辑与fireThrowCallback基本一样
     *
     * @param invoker Invoker 对象
     * @param invocation Invocation 方法
     * @param exception 异常
     */
    @SuppressWarnings("Duplicates")
    private void fireThrowCallback(final Invoker<?> invoker, final Invocation invocation, final Throwable exception) {
        // 获得 `onthrow` 方法和对象
        final Method onthrowMethod = (Method) StaticContext.getSystemContext().get(StaticContext.getKey(invoker.getUrl(), invocation.getMethodName(), Constants.ON_THROW_METHOD_KEY));
        final Object onthrowInst = StaticContext.getSystemContext().get(StaticContext.getKey(invoker.getUrl(), invocation.getMethodName(), Constants.ON_THROW_INSTANCE_KEY));
        // onthrow callback not configured
        if (onthrowMethod == null && onthrowInst == null) {
            return;
        }
        if (onthrowMethod == null || onthrowInst == null) {
            throw new IllegalStateException("service:" + invoker.getUrl().getServiceKey() + " has a onthrow callback config , but no such " + (onthrowMethod == null ? "method" : "instance") + " found. url:" + invoker.getUrl());
        }
        if (!onthrowMethod.isAccessible()) {
            onthrowMethod.setAccessible(true);
        }

        Class<?>[] rParaTypes = onthrowMethod.getParameterTypes();
        if (rParaTypes[0].isAssignableFrom(exception.getClass())) { // 符合异常
            try {
                // 参数数组 因为onthrow方法的参数第一个值必须为异常信息，所以这里需要构造参数列表
                Object[] args = invocation.getArguments();
                Object[] params;
                if (rParaTypes.length > 1) {
                    //原调用方法只有一个参数而且这个参数是数组（单独拎出来计算的好处是这样可以少复制一个数组）
                    if (rParaTypes.length == 2 && rParaTypes[1].isAssignableFrom(Object[].class)) {
                        params = new Object[2];
                        params[0] = exception;
                        params[1] = args;
                    } else {
                        //原调用方法有多于一个参数
                        params = new Object[args.length + 1];
                        params[0] = exception;
                        System.arraycopy(args, 0, params, 1, args.length);
                    }
                } else {
                    //原调用方法没有参数
                    params = new Object[]{exception};
                }

                // 调用方法
                onthrowMethod.invoke(onthrowInst, params);
            } catch (Throwable e) {
                logger.error(invocation.getMethodName() + ".call back method invoke error . callback method :" + onthrowMethod + ", url:" + invoker.getUrl(), e);
            }
        } else { // 不符合异常，打印错误日志
            logger.error(invocation.getMethodName() + ".call back method invoke error . callback method :" + onthrowMethod + ", url:" + invoker.getUrl(), exception);
        }
    }
}