package com.yzy.community.mapper;

import com.yzy.community.model.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Lenovo
 * @description 针对表【post】的数据库操作Mapper
 * @createDate 2023-04-17 15:28:32
 * @Entity com.yzy.community.model.entity.Post
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
    /**
     * 根据keyword进行查询
     *
     * @param keyword
     * @return
     */
    List<Post> selectByKeyword(String keyword);
}




