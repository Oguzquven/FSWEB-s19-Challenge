package com.workintech.twitter.service;

import com.workintech.twitter.entity.Retweet;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.BusinessException;
import com.workintech.twitter.exception.ResourceNotFoundException;
import com.workintech.twitter.exception.UnauthorizedException;
import com.workintech.twitter.repository.RetweetRepository;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// RetweetService - Retweet iş mantığını yönetir
@Service
public class RetweetService {

    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    // Constructor Injection
    @Autowired
    public RetweetService(RetweetRepository retweetRepository,
                          TweetRepository tweetRepository,
                          UserRepository userRepository) {
        this.retweetRepository = retweetRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    // Tweeti retweet et
    public String retweet(Long tweetId, Long userId) {
        // Tweet var mı kontrol et
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tweet bulunamadı: " + tweetId));

        // Kullanıcı var mı kontrol et
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Kullanıcı bulunamadı: " + userId));

        // Aynı tweeti iki kez retweet edilemez
        if (retweetRepository.existsByUserIdAndOriginalTweetId(userId, tweetId)) {
            throw new BusinessException("Bu tweeti zaten retweet ettiniz");
        }

        // Retweet oluştur ve kaydet
        Retweet retweet = new Retweet();
        retweet.setOriginalTweet(tweet);
        retweet.setUser(user);
        retweetRepository.save(retweet);

        // Güncel retweet sayısını döndür
        long retweetCount = retweetRepository.countByOriginalTweetId(tweetId);
        return "Tweet retweet edildi! Toplam retweet: " + retweetCount;
    }

    // Retweeti sil
    public String deleteRetweet(Long retweetId, Long userId) {
        // Retweet var mı kontrol et
        Retweet retweet = retweetRepository.findById(retweetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Retweet bulunamadı: " + retweetId));

        // Sadece retweeti yapan kişi silebilir
        if (!retweet.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Bu retweeti silme yetkiniz yok");
        }

        retweetRepository.delete(retweet);

        long retweetCount = retweetRepository.countByOriginalTweetId(
                retweet.getOriginalTweet().getId());
        return "Retweet silindi! Toplam retweet: " + retweetCount;
    }
}