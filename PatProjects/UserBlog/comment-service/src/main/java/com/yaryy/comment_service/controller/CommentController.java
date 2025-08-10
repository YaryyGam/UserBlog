package com.yaryy.comment_service.controller;

import com.yaryy.comment_service.model.CommentRequest;
import com.yaryy.comment_service.model.CommentResponse;
import com.yaryy.comment_service.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping("/{postId}/postComments")
    public ResponseEntity<List<CommentResponse>> getAllPostComments(@PathVariable int postId){
        return commentService.getAllPostComments(postId);
    }

    @PostMapping("/{postId}/createComment")
    public ResponseEntity<CommentResponse> createComment(@PathVariable int postId, @RequestBody CommentRequest commentRequest){
        CommentResponse response = commentService.createComment(postId, commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{postId}/editComment/{commentId}")
    public ResponseEntity<CommentResponse> editComment(@PathVariable int postId, @PathVariable long commentId, @RequestBody CommentRequest commentRequest){
        CommentResponse response = commentService.editComment(postId, commentId, commentRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping("/{postId}/deleteComment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable int postId, @PathVariable long commentId){
        String result = commentService.deleteComment(postId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{postId}/findCommentById/{commentId}")
    public ResponseEntity<CommentResponse> findCommentById(@PathVariable int postId, @PathVariable long commentId){
        CommentResponse result = commentService.findCommentById(postId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
