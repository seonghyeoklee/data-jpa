package com.example.mysql.domain.follow.service;

import com.example.mysql.domain.follow.entity.Follow;
import com.example.mysql.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowReadService {
    private final FollowRepository followRepository;

    public List<Follow> getFollows(Long fromMemberId) {
        return followRepository.findAllByFromMemberId(fromMemberId);
    }

    public List<Follow> getFollowers(Long toMemberId) {
        return followRepository.findAllByToMemberId(toMemberId);
    }
}
