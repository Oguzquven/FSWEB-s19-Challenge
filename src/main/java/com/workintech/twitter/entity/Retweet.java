package com.workintech.twitter.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

// Retweet Entity - "retweets" tablosuna karşılık gelir
// UniqueConstraint: Aynı kullanıcı aynı tweeti iki kez retweet edemez
@Entity
@Table(
        name = "retweets",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "original_tweet_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Retweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Retweeti yapan kullanıcı
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Retweet edilen orijinal tweet
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_tweet_id", nullable = false)
    private Tweet originalTweet;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}