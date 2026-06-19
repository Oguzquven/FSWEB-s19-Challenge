package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// RetweetRepository - Retweet veritabanı işlemleri
@Repository
public interface RetweetRepository extends JpaRepository<Retweet, Long> {

    // Kullanıcının belirli bir tweeti retweet edip etmediğini bulur
    Optional<Retweet> findByUserIdAndOriginalTweetId(Long userId, Long tweetId);

    // Retweet var mı kontrolü - aynı tweeti iki kez retweet etmesin diye
    boolean existsByUserIdAndOriginalTweetId(Long userId, Long tweetId);

    // Bir tweet'in toplam retweet sayısını döner
    long countByOriginalTweetId(Long tweetId);
}