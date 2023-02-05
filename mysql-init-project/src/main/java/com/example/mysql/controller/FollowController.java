package com.example.mysql.controller;

import com.example.mysql.application.usecase.CreateFollowMemberUseCase;
import com.example.mysql.application.usecase.GetFollowingMemberUseCase;
import com.example.mysql.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowController {
    private final CreateFollowMemberUseCase createFollowMemberUseCase;
    private final GetFollowingMemberUseCase getFollowingMemberUseCase;

    @PostMapping("/follow/{fromId}/{toId}")
    public void create(@PathVariable Long fromId, @PathVariable Long toId) {
        createFollowMemberUseCase.execute(fromId, toId);
    }

    @PostMapping("/follow/members/{fromId}")
    public List<MemberDto> create(@PathVariable Long fromId) {
        return getFollowingMemberUseCase.execute(fromId);
    }
}
