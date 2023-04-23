package com.yzy.community.esdao;

import com.yzy.community.model.dto.post.PostEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: yzy
 * 第二个参数是主键类型
 **/
@Repository
public interface PostRepository extends ElasticsearchRepository<PostEsDTO, Integer> {

}
