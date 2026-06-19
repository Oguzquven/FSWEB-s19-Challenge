package com.workintech.twitter.controller;

import com.workintech.twitter.dto.Dtos;
import com.workintech.twitter.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// DislikeController - Like kaldırma endpoint'ini yönetir
@RestController
@RequestMapping("/dislike")
public class DislikeController {

    private final LikeService likeService;

    @Autowired
    public DislikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // POST /dislike - Tweetten like kaldır
    @PostMapping
    public ResponseEntity<String> dislikeTweet(
            @RequestBody Dtos.LikeRequest request) {
        String result = likeService.dislikeTweet(
                request.getTweetId(),
                request.getUserId());
        return ResponseEntity.ok(result);
    }
}