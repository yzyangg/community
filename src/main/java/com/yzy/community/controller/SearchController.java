package com.yzy.community.controller;

import com.yzy.community.common.BaseResponse;
import com.yzy.community.common.ResultUtils;
import com.yzy.community.model.dto.post.PostEsDTO;
import com.yzy.community.model.entity.Post;
import com.yzy.community.service.ElasticSearchService;
import com.yzy.community.service.PostService;
import com.yzy.community.service.UserService;
import javafx.scene.control.TableView;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.metadata.PostgresTableMetaDataProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author: yzy
 **/
@RestController
@RequestMapping("/search")
public class SearchController {
    @Resource
    private ElasticSearchService elasticSearchService;

    @Resource
    private PostService postService;


    @Resource
    private UserService userService;


    /**
     * 搜索帖子
     * TODO  分页查询
     *
     * @param keyword
     * @return
     */
    @GetMapping("byES")
    public BaseResponse<List<PostEsDTO>> searchByES(String keyword) {
        //通过ES查询
        List<PostEsDTO> postEsDTOS = elasticSearchService.searchPost(keyword);


        return ResultUtils.success(postEsDTOS, "从ES查询成功 ");

    }

    @GetMapping("/byMysql")
    public BaseResponse<List<PostEsDTO>> searchByMysql(String keyword) {

        //通过Mysql查询
        List<Post> posts = postService.searchPostByKeywordUsingMysql(keyword);

        List<PostEsDTO> postEsDTOList = posts.stream().map(post -> {
            PostEsDTO postEsDTO = new PostEsDTO();
            BeanUtils.copyProperties(post, postEsDTO);
            return postEsDTO;
        }).collect(Collectors.toList());

        return ResultUtils.success(postEsDTOList, "从Mysql查询成功 ");

    }

    //TODO 通过Redis查询
    @GetMapping("/byRedis")
    public BaseResponse<List<PostEsDTO>> searchByRedis(String keyword) {

        //通过Mysql查询
        List<Post> posts = postService.searchPostByKeywordUsingMysql(keyword);

        List<PostEsDTO> postEsDTOList = posts.stream().map(post -> {
            PostEsDTO postEsDTO = new PostEsDTO();
            BeanUtils.copyProperties(post, postEsDTO);
            return postEsDTO;
        }).collect(Collectors.toList());

        return ResultUtils.success(postEsDTOList, "从Redis 查询成功 ");

    }

}
