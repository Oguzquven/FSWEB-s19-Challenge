package com.workintech.twitter.controller;

import com.workintech.twitter.dto.Dtos;
import com.workintech.twitter.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // PUT /comment/1 - Yorumu güncelle
    // Sadece yorum sahibi güncelleyebilir
    @PutMapping("/{id}")
    public ResponseEntity<Dtos.CommentResponse> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody Dtos.CommentRequest request) {
        Dtos.CommentResponse response = commentService.updateComment(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE /comment/1?userId=1 - Yorumu sil
    // Yorum sahibi veya tweet sahibi silebilir
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long id,
            @RequestParam Long userId) {
        commentService.deleteComment(id, userId);
        return ResponseEntity.ok("Yorum başarıyla silindi");
    }
}