package com.yaryy.post_service.Service;

import com.yaryy.post_service.DAO.PostDAO;
import com.yaryy.post_service.Model.Post;
import com.yaryy.post_service.Model.PostRequest;
import com.yaryy.post_service.Model.PostResponse;
import com.yaryy.post_service.common.ResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostDAO postDAO;

    @Autowired
    public PostService(PostDAO postDAO){
        this.postDAO = postDAO;
    }

    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postDAO.findAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> createPost(PostRequest PostRequest) {
        try {
            Post post = new Post();
            post.setTitle(PostRequest.getTitle());
            post.setContent(PostRequest.getContent());

            Post savedPost = postDAO.save(post);

            PostResponse postResponse = new PostResponse();

            postResponse.setContent(savedPost.getContent());
            postResponse.setArticleDate(savedPost.getArticleDate());
            postResponse.setId(savedPost.getId());
            postResponse.setTitle(savedPost.getTitle());
            return ResponseEntity.ok(postResponse);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Wrong data: " + e.getMessage());

        }
    }
    @Transactional
    public ResponseEntity<List<Post>> getPostByTitle(String title) {
        try {
            return new ResponseEntity<>(postDAO.findByTitle(title), HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }
    @Transactional
    public ResponseEntity<String> deletePost(int id) {
        try {
            postDAO.deleteById(id);
            return new ResponseEntity<>("Object deleted", HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Fail", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<PostResponse> updatePost(int id, PostRequest request) {
        try {
            Optional<Post> optionalPost = postDAO.findById(id);
            if (optionalPost.isPresent()) {
                Post postToUpdate = optionalPost.get();
                postToUpdate.setTitle(request.getTitle());
                postToUpdate.setContent(request.getContent());

                Post updatedPost = postDAO.save(postToUpdate);

                PostResponse response = new PostResponse();
                response.setId(updatedPost.getId());
                response.setTitle(updatedPost.getTitle());
                response.setContent(updatedPost.getContent());
                response.setArticleDate(updatedPost.getArticleDate());

                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    public ResponseEntity<Post> getPostById(int id) {
        try {
            Optional<Post> postOptional = postDAO.findById(id);
            if(postOptional.isPresent()){
                Post post = postOptional.get();
                return ResponseEntity.status(HttpStatus.OK).body(post);
            }
            return ResponseMapper.notFoundRequest();
        }catch (Exception e){
            return ResponseMapper.badRequest();
        }
    }
}
