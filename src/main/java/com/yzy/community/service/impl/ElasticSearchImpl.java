package com.yzy.community.service.impl;

import com.yzy.community.esdao.PostRepository;
import com.yzy.community.model.dto.post.PostEsDTO;
import com.yzy.community.model.entity.Post;
import com.yzy.community.service.ElasticSearchService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: yzy
 **/
@Service
public class ElasticSearchImpl implements ElasticSearchService {
    @Resource
    private PostRepository postRepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    /**
     * 保存帖子到es
     *
     * @param post
     */
    @Override
    public void savePostEsDTO(Post post) {
        PostEsDTO postEsDTO = new PostEsDTO(post);
        postRepository.save(postEsDTO);
        elasticsearchRestTemplate.save(postEsDTO);
    }

    /**
     * 删除帖子
     *
     * @param id
     */
    @Override
    public void deletePostEsDTO(Integer id) {
        postRepository.deleteById(id);
    }

    /**
     * 更新帖子
     *
     * @param post
     */
    @Override
    public void updatePostEsDTO(Post post) {
        PostEsDTO postEsDTO = new PostEsDTO(post);
        postRepository.save(postEsDTO);
    }

    /**
     * 搜索帖子
     *
     * @param keyword
     * @return
     */
    @Override
    public List<PostEsDTO> searchPost(String keyword) {

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        sortBuilder = SortBuilders.fieldSort("createTime").order(SortOrder.ASC);
        NativeSearchQuery query = nativeSearchQueryBuilder
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withSorts(sortBuilder).build();
        List<PostEsDTO> collect = elasticsearchRestTemplate.search(query, PostEsDTO.class)
                .stream().map(SearchHit::getContent)
                .collect(Collectors.toList());
        return collect;
    }
}
