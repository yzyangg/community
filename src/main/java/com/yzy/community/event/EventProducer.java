package com.yzy.community.event;

import com.alibaba.fastjson2.JSONObject;
import com.yzy.community.model.entity.Event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: yzy
 **/
@Component
public class EventProducer {
    @Resource
    private KafkaTemplate kafkaTemplate;

    //发布事件
    public void fireEvent(Event event) {
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
