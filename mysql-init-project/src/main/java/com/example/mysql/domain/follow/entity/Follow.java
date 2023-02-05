package com.example.mysql.domain.follow.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Follow {
    private Long id;
    private Long fromMemberId;
    private Long toMemberId;
    private LocalDateTime createdAt;

    @Builder
    public Follow(Long id, Long fromMemberId, Long toMemberId, LocalDateTime createdAt) {
        this.id = id;
        this.fromMemberId = Objects.requireNonNull(fromMemberId);
        this.toMemberId = Objects.requireNonNull(toMemberId);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }
}
