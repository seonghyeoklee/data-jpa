package com.example.mysql.controller;

import com.example.mysql.domain.member.dto.RegisterMemberCommand;
import com.example.mysql.domain.member.entity.Member;
import com.example.mysql.domain.member.service.MemberReadService;
import com.example.mysql.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberWriteService memberWriteService;
    private final MemberReadService memberReadService;

    @PostMapping("/members")
    public Member register(@RequestBody RegisterMemberCommand command) {
        return memberWriteService.create(command);
    }

    @GetMapping("/members/{id}")
    public Member getMember(@PathVariable Long id) {
        return memberReadService.getMember(id);
    }
}
