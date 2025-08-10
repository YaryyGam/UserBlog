package com.yaryy.comment_service.DAO;

import com.yaryy.comment_service.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDAO extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(int postId);
}
