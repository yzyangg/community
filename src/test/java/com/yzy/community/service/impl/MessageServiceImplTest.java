package com.yzy.community.service.impl;

import com.yzy.community.model.entity.Message;
import com.yzy.community.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class MessageServiceImplTest {

    @Resource
    private MessageService messageService;

    @Test
    public void test() {
        List<Message> conversationList = messageService.getConversationList(2);
        for (Message message : conversationList) {
            System.out.println(message);
        }
        Integer conversionCount = messageService.getConversionCount(2);
        System.out.println(conversionCount);

    }

    @Test
    public void test02() {
        List<Message> letter = messageService.getLetter("2_5");
        for (Message message : letter) {
            System.out.println(message);
        }
    }

    @Test
    public void test03() {
        int lettersUnreadCount = messageService.getLettersUnreadCount(5, "2_5");
        System.out.println(lettersUnreadCount);
    }
}