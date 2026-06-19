package com.workintech.twitter.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

// Spring Security konfigürasyonu
// @Configuration: Bu sınıf bir konfigürasyon sınıfıdır
// @EnableWebSecurity: Spring Security'yi aktif eder
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Hangi endpoint'lerin korumalı olduğunu belirler
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF: REST API'lerde kapatılır (stateless yapı)
                .csrf(csrf -> csrf.disable())
                // CORS ayarlarını aktif et - React frontend için gerekli
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Register ve login herkese açık
                        .requestMatchers("/register", "/login").permitAll()
                        // GET istekleri herkese açık (tweet okuma)
                        .requestMatchers(HttpMethod.GET, "/tweet/**").permitAll()
                        // Diğer tüm istekler login gerektirir
                        .anyRequest().authenticated()
                )
                // HTTP Basic Auth - kullanıcı adı/şifre ile giriş
                .httpBasic(basic -> {});

        return http.build();
    }

    // CORS ayarları - React uygulamasının 3200 portundan istek atmasına izin verir
    // CORS hatası: farklı port/domain'den gelen istekler tarayıcı tarafından engellenir
    // Bu Bean ile 3200 portuna izin veriyoruz
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // React uygulamasının çalıştığı port - README'de 3200 isteniyor
        config.setAllowedOrigins(List.of("http://localhost:3200"));
        // İzin verilen HTTP metodları
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // İzin verilen header'lar
        config.setAllowedHeaders(List.of("*"));
        // Cookie/Auth bilgisinin gönderilmesine izin ver
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Tüm endpoint'lere bu CORS ayarını uygula
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // BCrypt: Şifreleri güvenli şekilde hashler
    // Aynı şifre her seferinde farklı hash üretir (salt kullanır)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DaoAuthenticationProvider: Veritabanından kullanıcı doğrulama
    // UserDetailsService + PasswordEncoder birleştirir
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // AuthenticationManager: Login işlemini yönetir
    // AuthController'da kullanılacak
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}