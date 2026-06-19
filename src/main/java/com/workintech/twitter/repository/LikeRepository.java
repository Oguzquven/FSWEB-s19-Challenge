package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// LikeRepository - Like veritabanı işlemleri
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Dislike için: kullanıcı + tweet kombinasyonundan like'ı bulur
    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);

    // Like var mı kontrolü - aynı tweete iki kez like atılmasın diye
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);

    // Bir tweet'in toplam like sayısını döner
    long countByTweetId(Long tweetId);
}