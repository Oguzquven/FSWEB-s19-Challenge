package com.workintech.twitter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// DTO (Data Transfer Object) sınıfları
// Entity'leri direkt dışarı açmak yerine DTO kullanırız:
// - Güvenlik: şifre gibi alanlar dışarı çıkmaz
// - Esneklik: API şekli entity'den bağımsız olur
// Hepsini tek dosyada iç sınıf olarak topladık
public class Dtos {

    // ---------- AUTH ----------

    // Kayıt isteği için - kullanıcıdan gelen veriyi taşır
    @Data
    public static class RegisterRequest {
        @NotBlank(message = "Kullanıcı adı boş olamaz")
        @Size(min = 3, max = 50)
        private String username;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 6, message = "Şifre en az 6 karakter")
        private String password;
    }

    // Login isteği için
    @Data
    public static class LoginRequest {
        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    // Başarılı login/register sonrası kullanıcıya dönülen yanıt
    @Data
    public static class AuthResponse {
        private Long id;
        private String username;
        private String email;
        private String message;

        public AuthResponse(Long id, String username, String email, String message) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.message = message;
        }
    }

    // ---------- TWEET ----------

    // Tweet oluşturma/güncelleme isteği
    @Data
    public static class TweetRequest {
        @NotBlank(message = "Tweet boş olamaz")
        @Size(max = 280)
        private String content;
        private Long userId;
    }

    // Tweet yanıtı - API'den dışarıya dönen tweet verisi
    @Data
    public static class TweetResponse {
        private Long id;
        private String content;
        private String username;
        private Long userId;
        private int likeCount;
        private int commentCount;
        private int retweetCount;
        private String createdAt;
        private String updatedAt;
    }

    // ---------- COMMENT ----------

    @Data
    public static class CommentRequest {
        @NotBlank(message = "Yorum boş olamaz")
        @Size(max = 500)
        private String content;
        private Long tweetId;   // Hangi tweete yorum yapılıyor
        private Long userId;    // Yorumu yapan kullanıcı
    }

    @Data
    public static class CommentResponse {
        private Long id;
        private String content;
        private String username;
        private Long userId;
        private Long tweetId;
        private String createdAt;
    }

    // ---------- LIKE ----------

    @Data
    public static class LikeRequest {
        private Long tweetId;
        private Long userId;
    }

    // ---------- RETWEET ----------

    @Data
    public static class RetweetRequest {
        private Long tweetId;
        private Long userId;
    }
}