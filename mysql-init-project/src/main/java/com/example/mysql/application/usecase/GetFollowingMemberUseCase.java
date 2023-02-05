package com.example.mysql.application.usecase;

import com.example.mysql.domain.follow.entity.Follow;
import com.example.mysql.domain.follow.service.FollowReadService;
import com.example.mysql.domain.member.dto.MemberDto;
import com.example.mysql.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetFollowingMemberUseCase {
    private final MemberReadService memberReadService;
    private final FollowReadService followReadService;

    public List<MemberDto> execute(Long memberId) {
        List<Follow> follows = followReadService.getFollows(memberId);
        return memberReadService.getMembers(follows.stream().map(Follow::getToMemberId).toList());
    }
}
