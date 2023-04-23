package com.yzy.community.service.impl;

import com.yzy.community.model.entity.User;
import com.yzy.community.model.vo.UserVO;
import com.yzy.community.service.FollowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FollowServiceImplTest {

    @Resource
    private FollowService followService;

    @Test
    void testFollow() {
        List<UserVO> followees = followService.findFollowees(2);
        for (UserVO followee : followees) {
            System.out.println(followee);
        }
    }

    @Test
    void testFollower() {
        List<UserVO> followers = followService.findFollowers(4);
        for (UserVO follower : followers) {
            System.out.println(follower);
        }
    }
}