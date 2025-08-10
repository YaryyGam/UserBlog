package com.yaryy.user_service.Feign;

import com.yaryy.user_service.Model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("POST-SERVICE")
public interface PostInterface {

    @PostMapping("post/createArticle")
    public ResponseEntity<Post> createPost(@RequestBody Post post);

    @DeleteMapping("post/deletePost/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id);

    @GetMapping("post/getPost/{title}")
    public ResponseEntity<List<Post>> getPostByTitle(@PathVariable String title);

    @PutMapping("post/updatePost/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int id, @RequestBody Post postRequest);

    @GetMapping("post/getPostById/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable int id);


    @PostMapping("post/createPostWithWeather/{city}")
    public ResponseEntity<Post> createPostWithWeather(@PathVariable String city, @RequestBody Post postRequest);
}
