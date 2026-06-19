package com.workintech.twitter.service;

import com.workintech.twitter.dto.Dtos;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.BusinessException;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// AuthService - Kayıt ve giriş işlemlerini yönetir
// @Service: Bu sınıf bir servis katmanı bean'idir
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Constructor Injection - Dependency Injection kuralı
    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // Yeni kullanıcı kaydı
    public Dtos.AuthResponse register(Dtos.RegisterRequest request) {
        // Kullanıcı adı daha önce alınmış mı kontrol et
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Bu kullanıcı adı zaten kullanılıyor: " + request.getUsername());
        }
        // Email daha önce kullanılmış mı kontrol et
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Bu email zaten kullanılıyor: " + request.getEmail());
        }

        // Yeni kullanıcı oluştur
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        // Şifreyi BCrypt ile hashle - düz metin şifre veritabanına kaydedilmez
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return new Dtos.AuthResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                "Kayıt başarılı!"
        );
    }

    // Kullanıcı girişi
    public Dtos.AuthResponse login(Dtos.LoginRequest request) {
        // Spring Security ile kimlik doğrulama
        // Yanlış bilgi girilirse BadCredentialsException fırlatır
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Kullanıcıyı veritabanından getir
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("Kullanıcı bulunamadı"));

        return new Dtos.AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                "Giriş başarılı!"
        );
    }
}