package com.yaryy.user_service.Feign;

import com.yaryy.user_service.Model.Comment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("COMMENT-SERVICE")
public interface CommentInterface {

    @PostMapping("comments/{postId}/createComment")
    public ResponseEntity<Comment> createComment(@PathVariable int postId, @RequestBody Comment commentRequest);

    @PutMapping("comments/{postId}/editComment/{commentId}")
    public ResponseEntity<Comment> editComment(@PathVariable int postId, @PathVariable long commentId, @RequestBody Comment commentRequest);

    @DeleteMapping("comments/{postId}/deleteComment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable int postId, @PathVariable long commentId);

    @GetMapping("comments/{postId}/findCommentById/{commentId}")
    public ResponseEntity<Comment> findCommentById(@PathVariable int postId, @PathVariable long commentId);

    @GetMapping("comments/{postId}/postComments")
    public ResponseEntity<List<Comment>> getAllPostComments(@PathVariable int postId);
}
