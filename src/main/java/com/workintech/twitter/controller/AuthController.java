package com.workintech.twitter.controller;

import com.workintech.twitter.dto.Dtos;
import com.workintech.twitter.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// AuthController - Kayıt ve giriş endpoint'lerini yönetir
// @RestController: @Controller + @ResponseBody - JSON döner
// @RequestMapping: Bu controller'ın base URL'i
@RestController
public class AuthController {

    private final AuthService authService;

    // Constructor Injection
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /register - Yeni kullanıcı kaydı
    // @Valid: RegisterRequest içindeki validasyonları çalıştırır
    // @RequestBody: JSON body'yi Java nesnesine çevirir
    @PostMapping("/register")
    public ResponseEntity<Dtos.AuthResponse> register(
            @Valid @RequestBody Dtos.RegisterRequest request) {
        Dtos.AuthResponse response = authService.register(request);
        // 201 Created - yeni kayıt oluşturuldu
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // POST /login - Kullanıcı girişi
    @PostMapping("/login")
    public ResponseEntity<Dtos.AuthResponse> login(
            @Valid @RequestBody Dtos.LoginRequest request) {
        Dtos.AuthResponse response = authService.login(request);
        // 200 OK - başarılı giriş
        return ResponseEntity.ok(response);
    }
}