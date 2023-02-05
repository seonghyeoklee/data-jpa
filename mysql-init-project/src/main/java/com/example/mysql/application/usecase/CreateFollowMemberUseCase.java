package com.example.mysql.application.usecase;

import com.example.mysql.domain.follow.service.FollowWriteService;
import com.example.mysql.domain.member.dto.MemberDto;
import com.example.mysql.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateFollowMemberUseCase {
    private final MemberReadService memberReadService;
    private final FollowWriteService followWriteService;

    public void execute(Long fromMemberId, Long toMemberId) {
        MemberDto fromMember = memberReadService.getMember(fromMemberId);
        MemberDto toMember = memberReadService.getMember(toMemberId);

        followWriteService.create(fromMember, toMember);
    }
}
