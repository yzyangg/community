package com.yzy.community;

import com.yzy.community.model.entity.Post;
import com.yzy.community.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: yzy
 **/
@SpringBootTest
public class testSql {
    @Resource
    private PostService postService;

    @Test
    public void testGet() {
        Post byId = postService.getById(1L);
        System.out.println(byId);
    }
}
