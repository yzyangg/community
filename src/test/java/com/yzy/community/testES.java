package com.yzy.community;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yzy.community.esdao.PostRepository;
import com.yzy.community.model.dto.post.PostEsDTO;
import com.yzy.community.model.entity.Post;
import com.yzy.community.model.vo.PostVO;
import com.yzy.community.service.PostService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    public void test01() {
        PostVO postVoById = postService.getPostVoById(2L);
        PostEsDTO postEsDTO = new PostEsDTO();
        BeanUtils.copyProperties(postVoById, postEsDTO);
        postRepository.save(postEsDTO);

    }

    @Test
    public void test02() {
        List<Post> collect = postService.list().stream().collect(Collectors.toList());
        for (Post post : collect) {
            new Thread(() -> {
                PostEsDTO postEsDTO = new PostEsDTO();
                BeanUtils.copyProperties(post, postEsDTO);
                postRepository.save(postEsDTO);
            }).start();
        }
        //线程睡三十秒
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    @Test
    public void syncData() {
        for (int i = 28001; i <= 38000; i++) {
            LambdaUpdateWrapper<Post> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Post::getId, i);
            wrapper.setSql("comment_count = comment_count + 99");
            postService.update(wrapper);
        }

    }
}
