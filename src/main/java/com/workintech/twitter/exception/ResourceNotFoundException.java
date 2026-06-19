package com.workintech.twitter.exception;

// Kayıt bulunamadı hatası → 404 Not Found
// RuntimeException: checked exception zorunluluğu yok
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}