package com.workintech.twitter.controller;

import com.workintech.twitter.dto.Dtos;
import com.workintech.twitter.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// CommentController - Yorum endpoint'lerini yönetir
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // POST /comment - Tweete yorum yap
    @PostMapping
    public ResponseEntity<Dtos.CommentResponse> createComment(
            @Valid @RequestBody Dtos.CommentRequest request) {
        Dtos.CommentResponse response = commentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /comment/tweet/1 - Bir tweet'in tüm yorumlarını getir
    @GetMapping("/tweet/{tweetId}")
    public ResponseEntity<List<Dtos.CommentResponse>> getCommentsByTweetId(
            @PathVariable Long tweetId) {
        List<Dtos.CommentResponse> comments = commentService.getCommentsByTweetId(tweetId);
        return ResponseEntity.ok(comments);
    }

    // PUT /comment/1 - Yorumu güncelle
    @PutMapping("/{id}")
    public ResponseEntity<Dtos.CommentResponse> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody Dtos.CommentRequest request) {
        Dtos.CommentResponse response = commentService.updateComment(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE /comment/1?userId=1 - Yorumu sil
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long id,
            @RequestParam Long userId) {
        commentService.deleteComment(id, userId);
        return ResponseEntity.ok("Yorum basariyla silindi");
    }
}