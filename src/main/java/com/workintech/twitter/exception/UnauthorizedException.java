package com.workintech.twitter.exception;

// Yetkisiz işlem hatası → 403 Forbidden
// Örn: Başkasının tweetini silmeye çalışmak
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}