# 基本使用

## 启动MQ

```shell
cd /Users/lpl/Desktop/tool/rocketmq/distribution/target/rocketmq-5.2.0/rocketmq-5.2.0


# 启动namesrv
nohup sh bin/mqnamesrv &

# 验证是否启动成功
tail -f ~/logs/rocketmqlogs/namesrv.log

# 先启动broker
nohup sh bin/mqbroker -n localhost:9876 autoCreateTopicEnable=true --enable-proxy &

# 验证broker是否启动成功, 比如, broker的ip是192.168.1.2 然后名字是broker-a
tail -f ~/logs/rocketmqlogs/proxy.log 
```

## 关闭MQ

```shell
sh bin/mqshutdown namesrv
sh bin/mqshutdown broker
```



## javaSDK

> pom依赖
```xml
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-client-java</artifactId>
    <version>${rocketmq-client-java-version}</version>
</dependency>
```

> 通过mqadmin创建Topic
```shell
sh bin/mqadmin updatetopic -n localhost:9876 -t TestTopic -c DefaultCluster
```

