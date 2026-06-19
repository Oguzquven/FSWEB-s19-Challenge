package com.workintech.twitter.service;

import com.workintech.twitter.dto.Dtos;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.ResourceNotFoundException;
import com.workintech.twitter.exception.UnauthorizedException;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

// TweetService - Tweet iş mantığını yönetir
@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Autowired
    public TweetService(TweetRepository tweetRepository,
                        UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    // Tweet oluştur - @Transactional: işlem bitmeden veritabanına yazılmaz
    @Transactional
    public Dtos.TweetResponse createTweet(Dtos.TweetRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Kullanıcı bulunamadı: " + request.getUserId()));

        Tweet tweet = new Tweet();
        tweet.setContent(request.getContent());
        tweet.setUser(user);

        Tweet savedTweet = tweetRepository.save(tweet);
        return convertToResponse(savedTweet);
    }

    // Kullanıcının tüm tweetleri
    // @Transactional: lazy loading için session açık kalır
    @Transactional
    public List<Dtos.TweetResponse> getTweetsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Kullanıcı bulunamadı: " + userId);
        }
        return tweetRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Tek tweet getir
    @Transactional
    public Dtos.TweetResponse getTweetById(Long id) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tweet bulunamadı: " + id));
        return convertToResponse(tweet);
    }

    // Tweet güncelle
    @Transactional
    public Dtos.TweetResponse updateTweet(Long id, Dtos.TweetRequest request) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tweet bulunamadı: " + id));

        if (!tweet.getUser().getId().equals(request.getUserId())) {
            throw new UnauthorizedException("Bu tweeti güncelleme yetkiniz yok");
        }

        tweet.setContent(request.getContent());
        Tweet updatedTweet = tweetRepository.save(tweet);
        return convertToResponse(updatedTweet);
    }

    // Tweet sil
    @Transactional
    public void deleteTweet(Long id, Long userId) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tweet bulunamadı: " + id));

        if (!tweet.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Bu tweeti silme yetkiniz yok");
        }

        tweetRepository.delete(tweet);
    }

    // Entity'yi DTO'ya çevirir - lazy loading güvenli hale getirildi
    private Dtos.TweetResponse convertToResponse(Tweet tweet) {
        Dtos.TweetResponse response = new Dtos.TweetResponse();
        response.setId(tweet.getId());
        response.setContent(tweet.getContent());
        response.setUsername(tweet.getUser().getUsername());
        response.setUserId(tweet.getUser().getId());
        // try-catch: lazy loading hatalarına karşı güvenli
        try {
            response.setLikeCount(tweet.getLikes().size());
            response.setCommentCount(tweet.getComments().size());
            response.setRetweetCount(tweet.getRetweets().size());
        } catch (Exception e) {
            response.setLikeCount(0);
            response.setCommentCount(0);
            response.setRetweetCount(0);
        }
        response.setCreatedAt(tweet.getCreatedAt().toString());
        response.setUpdatedAt(tweet.getUpdatedAt().toString());
        return response;
    }
}