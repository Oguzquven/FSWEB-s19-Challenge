package com.workintech.twitter.controller;

import com.workintech.twitter.dto.Dtos;
import com.workintech.twitter.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// LikeController - Like/Dislike endpoint'lerini yönetir
@RestController
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // POST /like - Tweete like at
    // @RequestBody: JSON body'den tweetId ve userId alır
    @PostMapping
    public ResponseEntity<String> likeTweet(
            @RequestBody Dtos.LikeRequest request) {
        String result = likeService.likeTweet(
                request.getTweetId(),
                request.getUserId());
        return ResponseEntity.ok(result);
    }
}