package com.workintech.twitter.security;

import com.workintech.twitter.entity.User;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Spring Security'nin kullanıcı yükleme mekanizması
// UserDetailsService: Spring Security'nin kullanıcıyı veritabanından yüklemesi için implement edilir
// Login sırasında Spring Security bu sınıfı çağırır
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Dependency Injection - UserRepository'yi inject ediyoruz
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Spring Security login sırasında bu metodu çağırır
    // Kullanıcı adına göre veritabanından kullanıcıyı bulur
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Kullanıcı bulunamadı: " + username));

        // Spring Security'nin anlayacağı UserDetails formatına çeviriyoruz
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // BCrypt encoded şifre
                .roles("USER") // Tüm kullanıcılara USER rolü veriyoruz
                .build();
    }
}