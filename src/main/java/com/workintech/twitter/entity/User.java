package com.workintech.twitter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// User Entity - "users" tablosuna karşılık gelir
// "user" PostgreSQL'de rezerve kelime olduğu için tablo adı "users" yapıldı
@Entity
@Table(name = "users")
@Data               // Lombok: getter, setter, equals, hashCode, toString üretir
@NoArgsConstructor  // JPA için parametresiz constructor zorunlu
@AllArgsConstructor // Tüm alanları alan constructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increment id
    private Long id;

    @NotBlank(message = "Kullanıcı adı boş olamaz")
    @Size(min = 3, max = 50)
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli email giriniz")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Column(nullable = false)
    private String password; // BCrypt ile şifrelenmiş hali tutulur

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Cascade: user silinirse tweetleri de silinir
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tweet> tweets = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Retweet> retweets = new ArrayList<>();

    // Kaydedilmeden önce çalışır, tarihi otomatik set eder
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}