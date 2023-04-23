package com.yzy.community.service;

import com.yzy.community.esdao.PostRepository;
import com.yzy.community.model.dto.post.PostEsDTO;
import com.yzy.community.model.entity.Post;
import com.yzy.community.service.LikeService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: yzy
 **/


public interface ElasticSearchService {


    /**
     * 保存帖子到es
     *
     * @param post
     */
    void savePostEsDTO(Post post);

    /**
     * 删除帖子
     *
     * @param id
     */
    void deletePostEsDTO(Integer id);

    /**
     * 更新帖子
     *
     * @param post
     */
    void updatePostEsDTO(Post post);

    List<PostEsDTO> searchPost(String keyword);
}
