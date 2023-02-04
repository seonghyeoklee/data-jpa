package com.example.mysql.controller;

import com.example.mysql.domain.member.dto.MemberDto;
import com.example.mysql.domain.member.dto.MemberNicknameHistoryDto;
import com.example.mysql.domain.member.dto.RegisterMemberCommand;
import com.example.mysql.domain.member.entity.Member;
import com.example.mysql.domain.member.service.MemberReadService;
import com.example.mysql.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberWriteService memberWriteService;
    private final MemberReadService memberReadService;

    @PostMapping("/members")
    public MemberDto register(@RequestBody RegisterMemberCommand command) {
        Member member = memberWriteService.create(command);
        return memberReadService.toDto(member);
    }

    @GetMapping("/members/{id}")
    public MemberDto getMember(@PathVariable Long id) {
        return memberReadService.getMember(id);
    }

    @PostMapping("/members/{id}")
    public MemberDto changeNickname(@PathVariable Long id, @RequestBody String nickname) {
        memberWriteService.changeNickname(id, nickname);
        return memberReadService.getMember(id);
    }

    @GetMapping("/{memberId}")
    public List<MemberNicknameHistoryDto> getNicknameHistories(@PathVariable Long memberId) {
        return memberReadService.getNicknameHistories(memberId);
    }
}
