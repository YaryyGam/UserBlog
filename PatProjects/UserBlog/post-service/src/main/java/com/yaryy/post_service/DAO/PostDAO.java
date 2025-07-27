package com.yaryy.post_service.DAO;

import com.yaryy.post_service.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDAO extends JpaRepository<Post, Integer> {

    List<Post> findByTitle(String title);
}
