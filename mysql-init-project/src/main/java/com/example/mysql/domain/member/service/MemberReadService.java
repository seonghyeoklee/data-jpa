package com.example.mysql.domain.member.service;

import com.example.mysql.domain.member.dto.MemberDto;
import com.example.mysql.domain.member.entity.Member;
import com.example.mysql.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberReadService {

    private final MemberRepository memberRepository;

    public MemberDto getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return toDto(member);
    }

    public MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getNickname(), member.getBirthday());
    }
}
