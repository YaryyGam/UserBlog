package com.yaryy.post_service.Controller;

import com.yaryy.post_service.Model.Post;
import com.yaryy.post_service.Model.PostRequest;
import com.yaryy.post_service.Model.PostResponse;
import com.yaryy.post_service.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<List<Post>> getAllPosts(){
        return postService.getAllPosts();
    }

    @PostMapping("/createArticle")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest){
        return postService.createPost(postRequest);
    }

    @DeleteMapping("/deletePost/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id){
        return postService.deletePost(id);
    }

    @GetMapping("/getPost/{title}")
    public ResponseEntity<List<Post>> getPostByTitle(@PathVariable String title){
        return postService.getPostByTitle(title);
    }

    @GetMapping("/getPostById/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable int id){
        return postService.getPostById(id);
    }

    @PutMapping("/updatePost/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable int id, @RequestBody PostRequest postRequest){
        return postService.updatePost(id, postRequest);
    }
}
