package com.yzy.community.service;

import java.util.Date;

/**
 * @author: yzy
 **/
public interface DataService {
    /**
     * 将指定的ip计入UV
     *
     * @param ip
     */
    void recordUV(String ip);

    /**
     * 统计指定日期范围内的UV
     *
     * @param start
     * @param end
     * @return
     */
    long calculateUV(Date start, Date end);

    /**
     * 将指定的用户计入DAU
     *
     * @param userId
     */
    void recordDAU(Integer userId);

    /**
     * 统计指定日期范围内的DAU
     * @param start
     * @param end
     * @return
     */
    long calculateDAU(Date start, Date end);
}
