package com.yaryy.comment_service.Feign;

import com.yaryy.comment_service.model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("POST-SERVICE")
public interface PostInterface {
    @GetMapping("post/getPostById/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable int id);

}
