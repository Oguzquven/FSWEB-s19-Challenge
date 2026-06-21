package com.workintech.twitter.controller;

import com.workintech.twitter.dto.Dtos;
import com.workintech.twitter.repository.LikeRepository;
import com.workintech.twitter.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// LikeController - Like/Dislike endpoint'lerini yönetir
@RestController
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;
    private final LikeRepository likeRepository;

    @Autowired
    public LikeController(LikeService likeService, LikeRepository likeRepository) {
        this.likeService = likeService;
        this.likeRepository = likeRepository;
    }

    // POST /like - Tweete like at
    @PostMapping
    public ResponseEntity<String> likeTweet(
            @RequestBody Dtos.LikeRequest request) {
        String result = likeService.likeTweet(
                request.getTweetId(),
                request.getUserId());
        return ResponseEntity.ok(result);
    }

    // GET /like/check?tweetId=1&userId=1 - Kullanıcı bu tweeti beğenmiş mi?
    // Sayfa yenilenince like durumunu kontrol etmek için kullanılır
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkLike(
            @RequestParam Long tweetId,
            @RequestParam Long userId) {
        boolean isLiked = likeRepository.existsByUserIdAndTweetId(userId, tweetId);
        return ResponseEntity.ok(isLiked);
    }
}