package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;

import java.util.Collections;

@Slf4j
public class ConsumerExample {
    public static void main(String[] args) throws Exception {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        //接入点地址
        String endpoints = "localhost:8081";
        ClientConfiguration configuration = ClientConfiguration.newBuilder().build();
        //订阅消息的过滤规则，表示订阅所有Tag的消息
        String tag = "*";
        FilterExpression expression = new FilterExpression(tag, FilterExpressionType.TAG);
        //未消费者指定所属的消费者分组，Group需要提早创建
        String consumerGroup = "YourConsumerGroup";
        //指定需要订阅哪个目标Topic，Topic需要提前创建
        String topic = "TestTopic";
        //初始化Consumer
        PushConsumer pushConsumer = provider
                .newPushConsumerBuilder()
                .setClientConfiguration(configuration)
                .setConsumerGroup(consumerGroup)
                .setSubscriptionExpressions(Collections.singletonMap(topic, expression))
                .setMessageListener(messageView -> {
                    log.info("消费：{}", messageView);
                    return ConsumeResult.SUCCESS;
                })
                .build();
        Thread.sleep(Long.MAX_VALUE);
        pushConsumer.close();


    }
}
