package com.yzy.community;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author: yzy
 **/
@SpringBootTest
public class testRedis {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void testHyperLogLog() {
        String key1 = "key1";
        for (int i = 0; i < 10000; i++) {
            redisTemplate.opsForHyperLogLog().add(key1, i);
        }
        String key2 = "key2";
        for (int i = 10001; i < 20000; i++) {
            redisTemplate.opsForHyperLogLog().add(key2, i);
        }
        String unionKey = "key3";
        redisTemplate.opsForHyperLogLog().union(unionKey, key1, key2);
        System.out.println(redisTemplate.opsForHyperLogLog().size(key1));
        System.out.println(redisTemplate.opsForHyperLogLog().size(key2));
        System.out.println(unionKey);
        System.out.println(redisTemplate.opsForHyperLogLog().size(unionKey));


    }

    @Test
    public void testBitMap() {
        String key = "key";
        Object execute = redisTemplate.execute((RedisCallback) connection -> connection.bitCount(key.getBytes()));
        assert execute != null;
        System.out.println(execute);
    }

}
