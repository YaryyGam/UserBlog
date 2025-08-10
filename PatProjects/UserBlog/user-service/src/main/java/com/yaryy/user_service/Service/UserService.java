package com.yaryy.user_service.Service;

import com.yaryy.user_service.DAO.UserDAO;
import com.yaryy.user_service.ExceptionHandler.CustomeExceptions.*;
import com.yaryy.user_service.Feign.CommentInterface;
import com.yaryy.user_service.Feign.PostInterface;
import com.yaryy.user_service.Model.*;
import com.yaryy.user_service.common.ResponseMapper;
import com.yaryy.user_service.mapper.UserMapper;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final PostInterface postInterface;
    private final CommentInterface commentInterface;

    @Autowired
    public UserService(UserDAO userDAO, PostInterface postInterface, CommentInterface commentInterface) {
        this.userDAO = userDAO;
        this.postInterface = postInterface;
        this.commentInterface = commentInterface;
    }

    public ResponseEntity<List<User>> getAllUsersSuperData() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userDAO.findAll());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<>());
        }
    }

    public ResponseEntity<List<UserResponse>> getAllUsers() {
        try {
            List<User> user = userDAO.findAll();
            List<UserResponse> userResponse = new ArrayList<>();
            for (User u : user) {
                userResponse.add(UserMapper.toResponse(u));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userResponse);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<>());
        }
    }

    @Transactional
    public ResponseEntity<UserRequest> createUser(UserRequest userRequest) {
        try {
            // Створюємо юзера без userName
            User user = new User();
            user.setUserName("temp"); // тимчасовий, щоб не було null
            user = userDAO.save(user); // отримуємо id

            // Генеруємо userName
            String generatedUserName = userRequest.getUserName() + "-" + user.getId();
            user.setUserName(generatedUserName);
            userDAO.save(user); // оновлюємо

            // Повертаємо відповідь
            userRequest.setUserName(generatedUserName);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userRequest);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    public ResponseEntity<User> findUserById(int id) {
        return userDAO.findById(id)
                .map(user -> {
                    User resp = new User();
                    resp.setId(user.getId());
                    resp.setUserName(user.getUserName());
                    resp.setRole(user.getRole());
                    resp.setCreatedAt(user.getCreatedAt());
                    resp.setPostIds(user.getPostIds());
                    return ResponseEntity.ok(resp);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Transactional
    public ResponseEntity<User> editUser(int id, UserRequest userRequest) {
        try {
            Optional<User> userOptional = userDAO.findById(id);

            if (userOptional.isPresent()) {
                User user = userOptional.get(); // отримуємо існуючого юзера

                // Оновлюємо лише потрібні поля
                user.setUserName(userRequest.getUserName());

                // Зберігаємо оновленого юзера
                User updatedUser = userDAO.save(user);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(updatedUser);
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null); // краще null ніж новий порожній об'єкт
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @Transactional
    public ResponseEntity<Post> createPost(int userId, Post post) {
        try {
            Optional<User> user = userDAO.findById(userId);
            if(user.isPresent()){
                ResponseEntity<Post> postResult = postInterface.createPost(post);
                User userToUpdate = user.get();
                List<Integer> list = userToUpdate.getPostIds();
                list.add(Objects.requireNonNull(postResult.getBody()).getId());
                userToUpdate.setPostIds(list);
                userDAO.save(userToUpdate);
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(postResult.getBody());
            }
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @Transactional
    public ResponseEntity deletePost(int userId, int id) {
        try {
            Optional<User> userOptional = userDAO.findById(userId);
            if(userOptional.isPresent()){

                User user = userOptional.get();

                if (user.getPostIds().contains(id)) {
                    postInterface.deletePost(id);
                    user.getPostIds().remove(Integer.valueOf(id)); // видаляємо id з локального списку
                    userDAO.save(user); // оновлюємо користувача

                    return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not have this post");
                }
            }
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    public ResponseEntity<List<Post>> getPostsByTitle(int userId, String title) {
        try {
            Optional<User> userOptional = userDAO.findById(userId);
            if(userOptional.isPresent()){
                User user = userOptional.get();
                List<Post> post = postInterface.getPostByTitle(title).getBody();
                List<Post> postToDisplay = new ArrayList<>();
                for(Post p : post){
                    if(user.getPostIds().contains(p.getId())){
                        postToDisplay.add(p);
                    }
                }
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(postToDisplay);
            }
            return ResponseMapper.notFoundRequest();
        }catch (Exception e){
            return ResponseMapper.badRequest();
        }
    }

    public ResponseEntity<Post> getPostById(int userId, int id) {
        try {
            Optional<User> userOptional = userDAO.findById(userId);
            if(userOptional.isEmpty()){return ResponseMapper.notFoundRequest();}

            ResponseEntity<Post> postResponse = postInterface.getPostById(id);
            if(!postResponse.getStatusCode().is2xxSuccessful() || postResponse.getBody() == null){return ResponseMapper.notFoundRequest();}

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(postResponse.getBody());
        }catch (Exception e){
            return ResponseMapper.badRequest();
        }
    }

    public ResponseEntity<Post> updatePost(int userId, int id, Post post) {
        try {
            Optional<User> userOptional = userDAO.findById(userId);
            if (userOptional.isEmpty()) {
                return ResponseMapper.notFoundRequest();
            }

            User user = userOptional.get();
            if (!user.getPostIds().contains(id)) {
                return ResponseMapper.notFoundRequest();
            }

            ResponseEntity<Post> postResponse = getPostById(userId, id);
            if (!postResponse.getStatusCode().is2xxSuccessful() || postResponse.getBody() == null) {
                return ResponseMapper.notFoundRequest();
            }

            Post postToUpdate = postResponse.getBody();
            postToUpdate.setTitle(post.getTitle());
            postToUpdate.setContent(post.getContent());

            Post updatedPost = postInterface.updatePost(id, postToUpdate).getBody();
            return ResponseEntity.status(HttpStatus.OK).body(updatedPost);

        } catch (Exception e) {
            return ResponseMapper.badRequest();
        }
    }

    @Transactional
    public ResponseEntity<Post> createPostWithWeather(int userId, String city, Post post) {
        try {
            User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("User " + userId + " not found."));

            Post postSaved = postInterface.createPostWithWeather(city, post).getBody();

            List<Integer> postIds = user.getPostIds();
            if(postSaved == null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
            postIds.add(postSaved.getId());

            user.setPostIds(postIds);

            userDAO.save(user);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(postSaved);
        }catch (Exception e){
            return ResponseMapper.badRequest();
        }
    }

    @Transactional
    public ResponseEntity<Comment> createComment(int userId, int postId, Comment comment) {
            User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("User " + userId + " not found."));

            var commentId = commentInterface.createComment(postId, comment);

            if(!commentId.getStatusCode().is2xxSuccessful() || !commentId.hasBody()) {
                throw new BadRequestException("Failed to create comment.");
            }
            user.getCommentIds().add(commentId.getBody().getId());
            userDAO.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(commentId.getBody());

    }

    @Transactional
    public ResponseEntity<Comment> editComment(int userId, int postId, long commentId, Comment comment) {
        User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("User " + userId + " not found."));

        if(!user.getCommentIds().contains(commentId)){
            throw new CommentOwnershipException("User " + userId + " cannot edit this comment.");
        }

        var commentToSearch = commentInterface.findCommentById(postId, commentId);

        if(!commentToSearch.getStatusCode().is2xxSuccessful() || commentToSearch.getBody() == null) {
            throw new CommentNotFoundException("Failed to find comment " + commentId);
        }

        Comment commentToEdit = commentToSearch.getBody();
        commentToEdit.setContent(comment.getContent());

        var updateResponse = commentInterface.editComment(postId, commentId, commentToEdit);

        if (!updateResponse.getStatusCode().is2xxSuccessful() || !updateResponse.hasBody()) {
            throw new CommentUpdateException("Failed to update comment " + commentId);
        }

        return ResponseEntity.ok(updateResponse.getBody());
    }

    @Transactional
    public ResponseEntity<String> deleteComment(int userId, int postId, long commentId) {
        User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("User " + userId + " not found."));

        if(!user.getCommentIds().contains(commentId)){
            throw new CommentOwnershipException("User " + userId + " cannot delete this comment.");
        }

        var commentToSearch = commentInterface.findCommentById(postId, commentId);

        if(!commentToSearch.getStatusCode().is2xxSuccessful() || commentToSearch.getBody() == null) {
            throw new CommentNotFoundException("Failed to find comment " + commentId);
        }

        var commentRequest = commentInterface.deleteComment(postId, commentId);

        if(!commentRequest.getStatusCode().is2xxSuccessful()){
            throw new ResourceNotFoundException("Failed to find comment " + commentId);
        }

        user.getCommentIds().remove(commentId);
        userDAO.save(user);

        return ResponseEntity.ok("Comment " + commentId + " deleted successfully.");
    }

    public ResponseEntity<List<Comment>> findAllPostComments(int userId, int postId) {
        User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("User " + userId + " not found."));

        var commentResponse = commentInterface.getAllPostComments(postId);

        if(!commentResponse.getStatusCode().is2xxSuccessful() || commentResponse.getBody() == null) {
            throw new PostNotFoundException("Failed to find post " + postId);
        }

        List<Comment> commentList = commentResponse.getBody();

        return ResponseEntity.ok(commentList);

    }
}
