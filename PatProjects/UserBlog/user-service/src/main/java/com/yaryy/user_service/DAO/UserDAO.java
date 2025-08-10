package com.yaryy.user_service.DAO;

import com.yaryy.user_service.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    List<Long> findCommentIdsById(Integer userId);
}
