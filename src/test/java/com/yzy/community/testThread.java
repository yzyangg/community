package com.yzy.community;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.Resource;

/**
 * @author: yzy
 **/
@SpringBootTest
public class testThread {

    @Resource
    ThreadPoolTaskExecutor taskExecutor;


    @Resource
    ThreadPoolTaskScheduler taskScheduler;

    @Test
    public void test01() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            taskExecutor.execute(() -> System.out.println(Thread.currentThread().getName()));
        }
        Thread.sleep(10000);
    }

    @Test
    public void test02() {
        taskScheduler.scheduleAtFixedRate(() -> System.out.println(Thread.currentThread().getName()), 1000);
    }


}
