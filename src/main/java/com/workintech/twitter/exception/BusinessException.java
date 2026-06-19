package com.workintech.twitter.exception;

// İş kuralı ihlali → 400 Bad Request
// Örn: Aynı tweete iki kez like atmak
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}