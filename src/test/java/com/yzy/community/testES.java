package com.yzy.community;

import com.yzy.community.esdao.PostRepository;
import com.yzy.community.model.dto.post.PostEsDTO;
import com.yzy.community.service.PostService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.annotation.Resource;

/**
 * @author: yzy
 **/
@SpringBootTest
public class testES {
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private PostRepository postRepository;

    @Test
    public void test() {
    }

    @Resource
    private PostService postService;

    @Test
    public void test02() {
        postService.list().stream().map(post -> {
            PostEsDTO postEsDTO = new PostEsDTO(post);
            return postEsDTO;
        }).forEach(postEsDTO -> {
            postRepository.save(postEsDTO);
        });
    }

    @Test
    public void testSearch() {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        sortBuilder = SortBuilders.fieldSort("createTime").order(SortOrder.ASC);
        NativeSearchQuery query = nativeSearchQueryBuilder
                .withQuery(QueryBuilders.multiMatchQuery("ullamco", "title", "content"))
                .withSorts(sortBuilder).build();
        elasticsearchRestTemplate.search(query, PostEsDTO.class).forEach(searchHit -> {

        });


    }
}
