package com.yzy.community.controller;

import com.yzy.community.common.BaseResponse;
import com.yzy.community.common.ResultUtils;
import com.yzy.community.model.dto.post.PostEsDTO;
import com.yzy.community.service.ElasticSearchService;
import com.yzy.community.service.PostService;
import com.yzy.community.service.UserService;
import javafx.scene.control.TableView;
import org.springframework.jdbc.core.metadata.PostgresTableMetaDataProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executors;

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
    @GetMapping("")
    public BaseResponse<List<PostEsDTO>> search(String keyword) {
        List<PostEsDTO> postEsDTOS = elasticSearchService.searchPost(keyword);
        return ResultUtils.success(postEsDTOS, "查询成功 ");

    }

}
