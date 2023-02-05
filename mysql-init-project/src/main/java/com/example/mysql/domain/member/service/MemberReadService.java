package com.example.mysql.domain.member.service;

import com.example.mysql.domain.member.dto.MemberDto;
import com.example.mysql.domain.member.dto.MemberNicknameHistoryDto;
import com.example.mysql.domain.member.entity.Member;
import com.example.mysql.domain.member.entity.MemberNicknameHistory;
import com.example.mysql.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.mysql.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberReadService {

    private final MemberRepository memberRepository;
    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;

    public MemberDto getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return toDto(member);
    }

    public List<MemberDto> getMembers(List<Long> ids) {
        return memberRepository.findAllByIdIn(ids).stream()
                .map(this::toDto)
                .toList();
    }

    public List<MemberNicknameHistoryDto> getNicknameHistories(Long memberId) {
        return memberNicknameHistoryRepository.findAllByMemberId(memberId).stream()
                .map(this::toDto)
                .toList();
    }

    public MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getNickname(), member.getBirthday());
    }

    private MemberNicknameHistoryDto toDto(MemberNicknameHistory memberNicknameHistory) {
        return new MemberNicknameHistoryDto(
                memberNicknameHistory.getId(),
                memberNicknameHistory.getMemberId(),
                memberNicknameHistory.getNickname(),
                memberNicknameHistory.getCreatedAt()
        );
    }
}
