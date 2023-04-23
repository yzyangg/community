package com.yzy.community.event;

import com.alibaba.fastjson2.JSONObject;
import com.yzy.community.contant.CommonConstant;
import com.yzy.community.model.entity.Event;
import com.yzy.community.model.entity.Message;
import com.yzy.community.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: yzy
 **/
@Component
@Slf4j
public class EventConsumer implements CommonConstant {

    @Resource
    private MessageService messageService;

    //消费消息
    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW}, groupId = "community-consumer-group")
    public void consumeMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("消息为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);

        if (event == null) {
            log.error("消息格式错误");
            return;
        }

        //发送站内通知
        //message基本信息
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());

        Map<String, Object> map = new HashMap<>();
        map.put("userId", event.getUserId());
        map.put("entityType", event.getEntityType());
        map.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(map));
        messageService.save(message);
    }
}
