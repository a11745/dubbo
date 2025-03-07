doc
Dubbo模块的作用模块名称作 用
dubbo-common 通用逻辑模块,提供工具类和通用模型
dubbo-remoting 远程通信模块,为消费者和服务提供者提供通信能力
dubbo-rpc 容易和remote模块混淆,本模块抽象各种通信协议,以及动态代理
dubbo-cluster 集群容错模块,RPC只关心单个调用,本模块则包括负载均衡、集群容错、 路由、分组聚合等
dubbo-registry 注册中心模块
dubbo-monitor 监控模块,监控Dubbo接口的调用次数、时间等
dubbo-config 配置模块,实现了 API配置、属性配置、XML配置、注解配置等功能
dubbo-container 容器模块,如果项目比较轻量,没用到Web特性,因此不想使用Tomcat 等Web容器,则可以使用这个Main方法加载Spring的容器
dubbo-filter 过滤器模块,包含Dubbo内置的过滤器
dubbo-plugin 插件模块,提供内置的插件,如QoS
dubbo-demo 一个简单的远程调用示例模块
dubbo-test 测试模块,包含性能测试、兼容性测试等
























# Dubbo Project

[![Build Status](https://travis-ci.org/alibaba/dubbo.svg?branch=master)](https://travis-ci.org/alibaba/dubbo) 
[![codecov](https://codecov.io/gh/alibaba/dubbo/branch/master/graph/badge.svg)](https://codecov.io/gh/alibaba/dubbo)
[![Gitter](https://badges.gitter.im/alibaba/dubbo.svg)](https://gitter.im/alibaba/dubbo?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
![license](https://img.shields.io/github/license/alibaba/dubbo.svg)
![maven](https://img.shields.io/maven-central/v/com.alibaba/dubbo.svg)

Dubbo is a high-performance, java based RPC framework open-sourced by Alibaba. Please visit [dubbo.io](http://dubbo.io) for quick start and other information.

We are now collecting dubbo user info in order to help us to improve dubbo better, pls. kindly help us by providing yours on [issue#1012: Wanted: who's using dubbo](https://github.com/alibaba/dubbo/issues/1012), thanks :)

## Links

* [Side projects](http://github.com/dubbo)
    * [Dubbo Spring Boot](https://github.com/dubbo/dubbo-spring-boot-project) - Spring Boot Project for Dubbo.
    * [Dubbo ops](https://github.com/dubbo/dubbo-ops) - The ops and reference implementation for dubbo. contains dubbo-admin,dubbo-monitor module.
* [Gitter channel](https://gitter.im/alibaba/dubbo)
* [Mailing list](https://groups.google.com/forum/#!forum/dubbo)
* [Dubbo user manual](http://dubbo.io/books/dubbo-user-book/)
* [Dubbo developer guide](http://dubbo.io/books/dubbo-dev-book/)
* [Dubbo admin manual](http://dubbo.io/books/dubbo-admin-book/)

## 精尽 Dubbo 源码解析

* [《精尽 Dubbo 源码分析 —— 调试环境搭建》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 项目结构一览》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— API 配置（一）之应用》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— API 配置（二）之服务提供者》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— API 配置（三）之服务消费者》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 属性配置》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— XML 配置》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 核心流程一览》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 拓展机制 SPI》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 线程池》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务暴露（一）之本地暴露（Injvm）》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务暴露（二）之远程暴露（Dubbo）》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务引用（一）之本地引用（Injvm）》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务引用（二）之远程引用（Dubbo）》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— Zookeeper 客户端》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 注册中心（一）之抽象 API》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 注册中心（二）之 Zookeeper》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 注册中心（三）之 Redis》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 动态编译（一）之 Javassist》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 动态代理（一）之 Javassist》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 动态代理（二）之 JDK》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 动态代理（三）之本地存根 Stub》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务调用（一）之本地调用（Injvm）》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务调用（二）之远程调用（Dubbo）【1】通信实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务调用（二）之远程调用（Dubbo）【2】同步调用》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务调用（三）之远程调用（Dubbo）【3】异步调用》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务调用（三）之远程调用（HTTP）》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务调用（四）之远程调用（Hessian）》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务调用（五）之远程调用（WebService）》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务调用（六）之远程调用（REST）》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务调用（七）之远程调用（WebService）》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务调用（八）之远程调用（Redis）》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务调用（九）之远程调用（Memcached）》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 调用特性（一）之回声测试》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 调用特性（二）之泛化引用》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 调用特性（二）之泛化实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 过滤器（一）之 ClassLoaderFilter》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 过滤器（二）之 ContextFilter》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 过滤器（三）之 AccessLogFilter》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 过滤器（四）之 ActiveLimitFilter && ExecuteLimitFilter》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 过滤器（五）之 TimeoutFilter》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 过滤器（六）之 DeprecatedFilter》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 过滤器（七）之 ExceptionFilter》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 过滤器（八）之 TokenFilter》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 过滤器（九）之 TpsLimitFilter》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 过滤器（十）之 CacheFilter》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 过滤器（十一）之 ValidationFilter》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— NIO 服务器（一）之抽象 API》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— NIO 服务器（二）之 Transport 层》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— NIO 服务器（三）之 Telnet 层》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— NIO 服务器（四）之 Exchange 层》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— NIO 服务器（五）之 Buffer 层》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— NIO 服务器（六）之 Netty4 实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— NIO 服务器（七）之 Netty3 实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— HTTP 服务器》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 序列化（一）之总体实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 序列化（二）之 Dubbo 实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 序列化（三）之 Kryo 实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码分析 —— 服务容器》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码解析 —— 集群容错（一）之抽象 API》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码解析 —— 集群容错（二）之 Cluster 实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码解析 —— 集群容错（三）之 Directory 实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码解析 —— 集群容错（四）之 LoadBalance 实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码解析 —— 集群容错（五）之 Merger 实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码解析 —— 集群容错（六）之 Configurator 实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码解析 —— 集群容错（七）之 Router 实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码解析 —— 集群容错（八）之 Mock 实现》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码解析 —— 优雅停机》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
* [《精尽 Dubbo 源码解析 —— 日志适配》](http://www.iocoder.cn/Dubbo/good-collection?github&1610)
