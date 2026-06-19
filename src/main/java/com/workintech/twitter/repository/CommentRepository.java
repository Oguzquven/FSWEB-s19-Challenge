package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// CommentRepository - Yorum veritabanı işlemleri
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Bir tweet'in tüm yorumlarını getirir
    List<Comment> findByTweetId(Long tweetId);
}