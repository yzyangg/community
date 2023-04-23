package com.yzy.community;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: yzy
 **/
@SpringBootTest
public class testKafka {

    @Resource
    private KafkaTemplate kafkaTemplate;

    @Resource
    private KafkaProducer kafkaProducer;


    @Test
    public void testSend() throws InterruptedException {

        kafkaProducer.send("test", "hello world");
        Thread.sleep(1000);

    }


}

@Component
class KafkaProducer {

    @Resource
    private KafkaTemplate kafkaTemplate;

    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}


@Component
class KafkaConsumer {

    @KafkaListener(topics = "test", groupId = "community-consumer-group")
    public void receive(ConsumerRecord record) {
        System.out.println(record.value());
    }
}
