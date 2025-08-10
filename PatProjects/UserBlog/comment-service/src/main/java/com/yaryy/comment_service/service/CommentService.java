package com.yaryy.comment_service.service;

import com.yaryy.comment_service.DAO.CommentDAO;
import com.yaryy.comment_service.ExceptionHandler.CustomeExceptions.NoCommentsException;
import com.yaryy.comment_service.ExceptionHandler.CustomeExceptions.ResourceNotFoundException;
import com.yaryy.comment_service.Feign.PostInterface;
import com.yaryy.comment_service.common.ResponseMapper;
import com.yaryy.comment_service.model.Comment;
import com.yaryy.comment_service.model.CommentRequest;
import com.yaryy.comment_service.model.CommentResponse;
import com.yaryy.comment_service.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CommentService {

    private final CommentDAO commentDAO;
    private final PostInterface postInterface;

    @Autowired
    public CommentService(CommentDAO commentDAO, PostInterface postInterface){
        this.commentDAO = commentDAO;
        this.postInterface = postInterface;
    }

    public ResponseEntity<List<CommentResponse>> getAllPostComments(int postId) {
            var postResponse = postInterface.getPostById(postId);

            if (!postResponse.getStatusCode().is2xxSuccessful() || postResponse.getBody() == null) {
                throw new ResourceNotFoundException("Post " + postId + " not found");
            }

            List<Comment> comments = commentDAO.findAllByPostId(postId);

            if (comments.isEmpty()) {
                throw new NoCommentsException("Post " + postId + " not found");
            }

            List<CommentResponse> commentResponses = comments.stream()
                    .map(c -> CommentResponse.builder()
                            .date(c.getDate())
                            .content(c.getContent())
                            .id(c.getId())
                            .build())
                    .toList();

            return ResponseEntity.ok(commentResponses);
    }

    @Transactional
    public CommentResponse createComment(int postId, CommentRequest commentRequest) {
        var postResponse = postInterface.getPostById(postId);

        if (!postResponse.getStatusCode().is2xxSuccessful() || postResponse.getBody() == null) {
            throw new ResourceNotFoundException("Post with id " + postId + " not found");
        }

        var comment = Comment.builder()
                .content(commentRequest.getContent())
                .postId(postId)
                .build();

        var savedComment = commentDAO.save(comment);

        return CommentResponse.builder()
                .content(savedComment.getContent())
                .date(savedComment.getDate())
                .id(savedComment.getId())
                .build();
    }

    public CommentResponse editComment(int postId, long commentId, CommentRequest commentRequest) {

        var postResponse = postInterface.getPostById(postId);

        if(!postResponse.getStatusCode().is2xxSuccessful() || !postResponse.hasBody()){
            throw new ResourceNotFoundException("Post with id " + postId + " not found");
        }

        Comment comment = commentDAO.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment with id " + commentId + " not found"));

        comment.setContent(commentRequest.getContent());

        Comment savedComment = commentDAO.save(comment);

        return CommentResponse.builder()
                .content(savedComment.getContent())
                .date(savedComment.getDate())
                .id(savedComment.getId())
                .build();
    }

    public String deleteComment(int postId, long commentId) {

        var postResponse = postInterface.getPostById(postId);

        if(!postResponse.getStatusCode().is2xxSuccessful() || !postResponse.hasBody()){
            throw new ResourceNotFoundException("Post with id " + postId + " not found");
        }

        Comment comment = commentDAO.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("No such comment."));

        commentDAO.delete(comment);

        return "Deleted";
    }

    public CommentResponse findCommentById(int postId, long commentId) {

        var postResponse = postInterface.getPostById(postId);

        if(!postResponse.getStatusCode().is2xxSuccessful() || !postResponse.hasBody()){
            throw new ResourceNotFoundException("Post with id " + postId + " not found");
        }

        Comment comment = commentDAO.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("No such comment with " + commentId));

        return CommentResponse.builder()
                .id(comment.getId())
                .date(comment.getDate())
                .content(comment.getContent())
                .build();
    }
}
