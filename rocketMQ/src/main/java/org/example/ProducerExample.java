package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;

import java.io.IOException;

@Slf4j
public class ProducerExample {
    public static void main(String[] args) throws ClientException, IOException {
        // 接入点地址
        String endpoint = "localhost:8001";
        // 消息发送的目标Topic名称，需要提前创建
        String topic = "TestTopic";
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration configuration = ClientConfiguration
                .newBuilder()
                .setEndpoints(endpoint)
                .build();
        // 初始化Producer时需要设置通信配置以及预绑定的Topic
        Producer producer = provider
                .newProducerBuilder()
                .setTopics(topic)
                .setClientConfiguration(configuration)
                .build();
        // 普通消息发送
        Message message = provider
                .newMessageBuilder()
                .setTopic(topic)
                // 设置消息索引键，可根据关键字精准查找某条消息
                .setKeys("messageKey")
                // 设置消息Tag，用于Consumer可以根据指定Tag过滤消息
                .setTag("messageTag")
                .setBody("hello world !".getBytes())
                .build();

        // 发消息
        try {
            SendReceipt sendReceipt = producer.send(message);
            log.info("Send message successfully, messageId={}", sendReceipt.getMessageId());
        }catch (ClientException e){
            log.error("Failed to send message", e);
        }
        finally {
            producer.close();
        }
    }
}
