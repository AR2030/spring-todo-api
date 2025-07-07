package com.example.todo.service;

import com.example.todo.model.RevokedToken;
import com.example.todo.repository.RevokedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TokenBlacklistService {

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    public void blacklistToken(String token, Instant expiry) {
        if (!revokedTokenRepository.existsByToken(token)) {
            revokedTokenRepository.save(new RevokedToken(token, expiry));
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return revokedTokenRepository.existsByToken(token);
    }

    @Scheduled(cron = "0 0 * * * *") // كل ساعة
    public void cleanExpiredTokens() {
        List<RevokedToken> all = revokedTokenRepository.findAll();
        Instant now = Instant.now();

        revokedTokenRepository.deleteAll(
                all.stream().filter(t -> t.getExpiry().isBefore(now)).toList()
        );
    }
}
