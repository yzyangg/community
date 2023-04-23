package com.yzy.community.service.impl;

import cn.hutool.db.ds.simple.SimpleDataSource;
import com.yzy.community.service.DataService;
import com.yzy.community.utils.RedisUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: yzy
 **/
public class DataServiceImpl implements DataService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    @Override
    public void recordUV(String ip) {
        String uvKey = RedisUtil.getUVKey(dateFormat.format(new Date()));
        stringRedisTemplate.opsForHyperLogLog().add(uvKey, ip);
    }

    @Override
    public long calculateUV(Date start, Date end) {
        List<String> keyList = new ArrayList<>();

        // 从开始日期到结束日期，逐一生成对应的key
        //逐天循环
        Calendar instance = Calendar.getInstance();
        instance.setTime(start);
        while (!instance.getTime().after(end)) {
            String key = RedisUtil.getUVKey(dateFormat.format(instance.getTime()));
            keyList.add(key);
            instance.add(Calendar.DATE, 1);
        }
        //区间UV的key
        String uvKey = RedisUtil.getUVKey(dateFormat.format(start), dateFormat.format(end));
        //合并多个key
        stringRedisTemplate.opsForHyperLogLog().union(uvKey, keyList.toString());
        //返回统计的结果
        return stringRedisTemplate.opsForHyperLogLog().size(uvKey);
    }

    @Override
    public void recordDAU(Integer userId) {
        String dauKey = RedisUtil.getDAUKey(dateFormat.format(new Date()));
        stringRedisTemplate.opsForValue().setBit(dauKey, userId, true);
    }

    @Override
    public long calculateDAU(Date start, Date end) {

        List<byte[]> keyList = new ArrayList<>();

        // 从开始日期到结束日期，逐一生成对应的key
        //逐天循环
        Calendar instance = Calendar.getInstance();
        instance.setTime(start);
        while (!instance.getTime().after(end)) {
            String key = RedisUtil.getDAUKey(dateFormat.format(instance.getTime()));
            keyList.add(key.getBytes());
            instance.add(Calendar.DATE, 1);
        }
        //区间UV的key
        String dauKey = RedisUtil.getDAUKey(dateFormat.format(start), dateFormat.format(end));
        //合并多个key
        stringRedisTemplate.opsForHyperLogLog().union(dauKey, keyList.toString());
        //返回统计的结果
        return stringRedisTemplate.opsForHyperLogLog().size(dauKey);
    }
}
