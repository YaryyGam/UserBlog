package com.yaryy.user_service.Controller;


import com.yaryy.user_service.Model.Post;
import com.yaryy.user_service.Model.User;
import com.yaryy.user_service.Model.UserRequest;
import com.yaryy.user_service.Model.UserResponse;
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

    @PostMapping("/editUser/{id}")
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

    @PostMapping("/{userId}/updatePost/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int userId, @PathVariable int id, @RequestBody Post post){
        return userService.updatePost(userId, id, post);
    }
}
