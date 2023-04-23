package com.yzy.community;


import com.yzy.community.model.entity.Comment;
import com.yzy.community.model.entity.User;
import com.yzy.community.service.CommentService;
import com.yzy.community.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Test
    void contextLoads() {
        List<User> list = userService.list();
        for (User comment : list) {
            System.out.println(comment);
        }
    }

}
