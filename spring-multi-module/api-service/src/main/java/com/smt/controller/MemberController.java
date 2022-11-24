package com.smt.controller;

import com.smt.domain.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @GetMapping("/")
    public Member hello() {
        Member member = new Member();
        member.setName("test");

        return member;
    }
}
