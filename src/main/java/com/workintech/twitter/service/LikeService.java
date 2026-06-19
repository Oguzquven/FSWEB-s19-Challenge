package com.workintech.twitter.service;

import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.BusinessException;
import com.workintech.twitter.exception.ResourceNotFoundException;
import com.workintech.twitter.repository.LikeRepository;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// LikeService - Like/Dislike iş mantığını yönetir
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    // Constructor Injection
    @Autowired
    public LikeService(LikeRepository likeRepository,
                       TweetRepository tweetRepository,
                       UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    // Tweete like at
    public String likeTweet(Long tweetId, Long userId) {
        // Tweet var mı kontrol et
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tweet bulunamadı: " + tweetId));

        // Kullanıcı var mı kontrol et
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Kullanıcı bulunamadı: " + userId));

        // Aynı tweete iki kez like atılamaz
        if (likeRepository.existsByUserIdAndTweetId(userId, tweetId)) {
            throw new BusinessException("Bu tweeti zaten beğendiniz");
        }

        // Like oluştur ve kaydet
        Like like = new Like();
        like.setTweet(tweet);
        like.setUser(user);
        likeRepository.save(like);

        // Güncel like sayısını döndür
        long likeCount = likeRepository.countByTweetId(tweetId);
        return "Tweet beğenildi! Toplam beğeni: " + likeCount;
    }

    // Tweetten like kaldır (dislike)
    public String dislikeTweet(Long tweetId, Long userId) {
        // Like var mı kontrol et
        Like like = likeRepository.findByUserIdAndTweetId(userId, tweetId)
                .orElseThrow(() -> new BusinessException(
                        "Bu tweeti daha önce beğenmediniz"));

        likeRepository.delete(like);

        long likeCount = likeRepository.countByTweetId(tweetId);
        return "Beğeni kaldırıldı! Toplam beğeni: " + likeCount;
    }
}