package com.yzy.community.mapper;

import com.yzy.community.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Lenovo
 * @description 针对表【user】的数据库操作Mapper
 * @createDate 2023-04-17 15:28:34
 * @Entity com.yzy.community.model.entity.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




