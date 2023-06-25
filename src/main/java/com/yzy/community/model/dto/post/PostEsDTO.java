package com.yzy.community.model.dto.post;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.gson.Gson;
import com.yzy.community.model.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * es数据传输类
 *
 * @author: yzy
 **/
@Document(indexName = "post_v2")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEsDTO implements Serializable {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 用户id
     */
    @Field(type = FieldType.Integer)
    private Integer userId;


    /**
     * 标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    /**
     * 内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    /**
     * 0-普通; 1-置顶;
     */
    @Field(type = FieldType.Integer)
    private Integer type;

    /**
     * 0-正常; 1-精华; 2-拉黑;
     */
    @Field(type = FieldType.Integer)
    private Integer status;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, pattern = DATE_TIME_PATTERN)
    private Date createTime;
    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, pattern = DATE_TIME_PATTERN)
    private Date updateTime;

    /**
     *
     */
    @Field(type = FieldType.Integer)
    private Integer commentCount;

    /**
     *
     */
    @Field(type = FieldType.Double)
    private Double score;

    //构造函数
    public PostEsDTO(Post post) {
        BeanUtils.copyProperties(post, this);
    }


}
