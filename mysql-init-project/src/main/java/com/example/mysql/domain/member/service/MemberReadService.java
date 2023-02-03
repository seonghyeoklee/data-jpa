package com.example.mysql.domain.member.service;

import com.example.mysql.domain.member.entity.Member;
import com.example.mysql.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberReadService {

    private final MemberRepository memberRepository;

    public Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }
}
