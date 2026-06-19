package com.workintech.twitter.service;

import com.workintech.twitter.dto.Dtos;
import com.workintech.twitter.entity.Comment;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.ResourceNotFoundException;
import com.workintech.twitter.exception.UnauthorizedException;
import com.workintech.twitter.repository.CommentRepository;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

// CommentService - Yorum iş mantığını yönetir
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    // Constructor Injection
    @Autowired
    public CommentService(CommentRepository commentRepository,
                          TweetRepository tweetRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    // Tweete yorum yap
    public Dtos.CommentResponse createComment(Dtos.CommentRequest request) {
        // Tweet var mı kontrol et
        Tweet tweet = tweetRepository.findById(request.getTweetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tweet bulunamadı: " + request.getTweetId()));

        // Kullanıcı var mı kontrol et
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Kullanıcı bulunamadı: " + request.getUserId()));

        // Yeni yorum oluştur
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTweet(tweet);
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);
        return convertToResponse(savedComment);
    }

    // Yorumu güncelle - sadece yorum sahibi güncelleyebilir
    public Dtos.CommentResponse updateComment(Long id, Dtos.CommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Yorum bulunamadı: " + id));

        // Yetki kontrolü - sadece yorum sahibi güncelleyebilir
        if (!comment.getUser().getId().equals(request.getUserId())) {
            throw new UnauthorizedException("Bu yorumu güncelleme yetkiniz yok");
        }

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return convertToResponse(updatedComment);
    }

    // Yorumu sil - yorum sahibi veya tweet sahibi silebilir
    public void deleteComment(Long id, Long userId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Yorum bulunamadı: " + id));

        // Yetki kontrolü: yorum sahibi VEYA tweet sahibi silebilir
        boolean isCommentOwner = comment.getUser().getId().equals(userId);
        boolean isTweetOwner = comment.getTweet().getUser().getId().equals(userId);

        if (!isCommentOwner && !isTweetOwner) {
            throw new UnauthorizedException("Bu yorumu silme yetkiniz yok");
        }

        commentRepository.delete(comment);
    }

    // Comment entity'sini CommentResponse DTO'ya çevirir
    private Dtos.CommentResponse convertToResponse(Comment comment) {
        Dtos.CommentResponse response = new Dtos.CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setUsername(comment.getUser().getUsername());
        response.setUserId(comment.getUser().getId());
        response.setTweetId(comment.getTweet().getId());
        response.setCreatedAt(comment.getCreatedAt().toString());
        return response;
    }
}