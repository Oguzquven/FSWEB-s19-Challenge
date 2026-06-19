package com.workintech.twitter.controller;

import com.workintech.twitter.dto.Dtos;
import com.workintech.twitter.service.RetweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// RetweetController - Retweet endpoint'lerini yönetir
@RestController
@RequestMapping("/retweet")
public class RetweetController {

    private final RetweetService retweetService;

    @Autowired
    public RetweetController(RetweetService retweetService) {
        this.retweetService = retweetService;
    }

    // POST /retweet - Tweeti retweet et
    @PostMapping
    public ResponseEntity<String> retweet(
            @RequestBody Dtos.RetweetRequest request) {
        String result = retweetService.retweet(
                request.getTweetId(),
                request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // DELETE /retweet/1?userId=1 - Retweeti sil
    // Sadece retweeti yapan kişi silebilir
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRetweet(
            @PathVariable Long id,
            @RequestParam Long userId) {
        String result = retweetService.deleteRetweet(id, userId);
        return ResponseEntity.ok(result);
    }
}