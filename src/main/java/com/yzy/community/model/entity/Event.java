package com.yzy.community.model.entity;

import ch.qos.logback.core.encoder.EchoEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: yzy
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Event {
    private String topic;
    //消息发送者
    private Integer userId;
    //消息的类型
    private Integer entityType;
    //消息所对应的实体id
    private Integer entityId;
    //消息所对应的实体的作者id
    private Integer entityUserId;
    //扩展字段
    private Map<String, Object> data = new HashMap<>();

    public Event setData(String key, Object data) {
        this.data.put(key, data);
        return this;
    }


}
