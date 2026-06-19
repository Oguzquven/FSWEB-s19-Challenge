package com.workintech.twitter.controller;

import com.workintech.twitter.dto.Dtos;
import com.workintech.twitter.service.TweetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// TweetController - Tweet endpoint'lerini yönetir
// @RequestMapping("/tweet"): Tüm endpoint'ler /tweet ile başlar
@RestController
@RequestMapping("/tweet")
public class TweetController {

    private final TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    // POST /tweet - Tweet oluştur
    // @Valid: TweetRequest validasyonlarını çalıştırır
    @PostMapping
    public ResponseEntity<Dtos.TweetResponse> createTweet(
            @Valid @RequestBody Dtos.TweetRequest request) {
        Dtos.TweetResponse response = tweetService.createTweet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /tweet/findByUserId?userId=1 - Kullanıcının tüm tweetleri
    // @RequestParam: URL'deki ?userId=1 parametresini alır
    @GetMapping("/findByUserId")
    public ResponseEntity<List<Dtos.TweetResponse>> getTweetsByUserId(
            @RequestParam Long userId) {
        List<Dtos.TweetResponse> tweets = tweetService.getTweetsByUserId(userId);
        return ResponseEntity.ok(tweets);
    }

    // GET /tweet/findById?id=1 - Tek bir tweet
    @GetMapping("/findById")
    public ResponseEntity<Dtos.TweetResponse> getTweetById(
            @RequestParam Long id) {
        Dtos.TweetResponse tweet = tweetService.getTweetById(id);
        return ResponseEntity.ok(tweet);
    }

    // PUT /tweet/1 - Tweet güncelle
    // @PathVariable: URL'deki {id} değişkenini alır
    @PutMapping("/{id}")
    public ResponseEntity<Dtos.TweetResponse> updateTweet(
            @PathVariable Long id,
            @Valid @RequestBody Dtos.TweetRequest request) {
        Dtos.TweetResponse response = tweetService.updateTweet(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE /tweet/1?userId=1 - Tweet sil
    // Sadece tweet sahibi silebilir
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTweet(
            @PathVariable Long id,
            @RequestParam Long userId) {
        tweetService.deleteTweet(id, userId);
        return ResponseEntity.ok("Tweet başarıyla silindi");
    }
}