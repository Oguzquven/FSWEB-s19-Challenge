package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// TweetRepository - Tweet veritabanı işlemleri
@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    // Kullanıcının tüm tweetlerini getirir - /tweet/findByUserId endpoint'i için
    List<Tweet> findByUserId(Long userId);

    // JPQL sorgusu: tweet'i yorumları ve like'larıyla birlikte çeker
    // LEFT JOIN FETCH: N+1 problemini önler, tek sorguda tüm veriyi getirir
    @Query("SELECT t FROM Tweet t LEFT JOIN FETCH t.comments LEFT JOIN FETCH t.likes WHERE t.id = :id")
    Optional<Tweet> findByIdWithDetails(Long id);
}