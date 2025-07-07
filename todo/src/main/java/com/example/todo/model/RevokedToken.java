package com.example.todo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class RevokedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000, nullable = false, unique = true)
    private String token;

    private Instant expiry;

    public RevokedToken() {}

    public RevokedToken(String token, Instant expiry) {
        this.token = token;
        this.expiry = expiry;
    }

}
