package com.workintech.twitter.repository;

import com.workintech.twitter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// UserRepository - Kullanıcı veritabanı işlemleri
// JpaRepository: findAll, findById, save, delete gibi hazır CRUD metodları sağlar
// Metod adından Spring otomatik SQL üretir: findByUsername → SELECT * FROM users WHERE username=?
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Kullanıcı adına göre kullanıcı bulur - login için kullanılır
    Optional<User> findByUsername(String username);

    // Kullanıcı adı var mı kontrolü - register için
    boolean existsByUsername(String username);

    // Email var mı kontrolü - register için
    boolean existsByEmail(String email);
}