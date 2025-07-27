package com.yaryy.user_service.Service;

import com.yaryy.user_service.DAO.UserDAO;
import com.yaryy.user_service.Feing.PostInterface;
import com.yaryy.user_service.Model.Post;
import com.yaryy.user_service.Model.User;
import com.yaryy.user_service.Model.UserRequest;
import com.yaryy.user_service.Model.UserResponse;
import com.yaryy.user_service.common.ResponseMapper;
import com.yaryy.user_service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final PostInterface postInterface;

    @Autowired
    public UserService(UserDAO userDAO, PostInterface postInterface) {
        this.userDAO = userDAO;
        this.postInterface = postInterface;
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
                    .status(HttpStatus.OK)
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
                        .status(HttpStatus.OK)
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
}
