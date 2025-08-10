package com.yaryy.user_service.Controller;


import com.yaryy.user_service.Model.*;
import com.yaryy.user_service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/allUsersSuperData")
    public ResponseEntity<List<User>> getAllUsersSuperData(){
        return userService.getAllUsersSuperData();
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserRequest> createUser(@RequestBody UserRequest userRequest){
        return userService.createUser(userRequest);
    }

    @GetMapping("/getUserSuper/{id}")
    public ResponseEntity<User> findUserByIdSuperData(@PathVariable int id){
        return userService.findUserById(id);
    }

    @PutMapping("/editUser/{id}")
    public ResponseEntity<User> editUser(@PathVariable int id, @RequestBody UserRequest userRequest){
        return userService.editUser(id, userRequest);
    }

    @PostMapping("/{userId}/createPost")
    public ResponseEntity<Post> createPost(@PathVariable int userId, @RequestBody Post post){
        return userService.createPost(userId, post);
    }

    @DeleteMapping("/{userId}/deletePost/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int userId, @PathVariable int id){
        return userService.deletePost(userId, id);
    }

    @GetMapping("/{userId}/getPostsByTitle/{title}")
    public ResponseEntity<List<Post>> getPostsByTitle(@PathVariable int userId, @PathVariable String title){
        return userService.getPostsByTitle(userId, title);
    }

    @GetMapping("/{userId}/getPostById/{id}")
    public ResponseEntity<Post> getPostByTitle(@PathVariable int userId, @PathVariable int id){
        return userService.getPostById(userId, id);
    }

    @PutMapping("/{userId}/updatePost/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int userId, @PathVariable int id, @RequestBody Post post){
        return userService.updatePost(userId, id, post);
    }

    @PostMapping("/{userId}/createPostWithWeather/{city}")
    public ResponseEntity<Post> createPostWithWeather(@PathVariable int userId, @PathVariable String city, @RequestBody Post post){
        return userService.createPostWithWeather(userId, city, post);
    }

    @PostMapping("/{userId}/createComment/{postId}")
    public ResponseEntity<Comment> createComment(@PathVariable int userId, @PathVariable int postId, @RequestBody Comment comment){
        return userService.createComment(userId, postId, comment);
    }

    @PutMapping("/{userId}/editComment/{postId}/{commentId}")
    public ResponseEntity<Comment> editComment(@PathVariable int userId, @PathVariable int postId, @PathVariable long commentId, @RequestBody Comment comment){
        return userService.editComment(userId, postId, commentId, comment);
    }

    @DeleteMapping("/{userId}/deleteComment/{postId}/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable int userId, @PathVariable int postId, @PathVariable long commentId){
        return userService.deleteComment(userId, postId, commentId);
    }

    @GetMapping("/{userId}/findAllPostComments/{postId}")
    public ResponseEntity<List<Comment>> findAllPostComments(@PathVariable int userId, @PathVariable int postId){
        return userService.findAllPostComments(userId, postId);
    }
}
