package com.shlee.membership.application.service;

import com.shlee.membership.adapter.out.persistence.MembershipJpaEntity;
import com.shlee.membership.adapter.out.persistence.MembershipMapper;
import com.shlee.membership.application.port.in.FindMembershipCommand;
import com.shlee.membership.application.port.in.FindMembershipUseCase;
import com.shlee.membership.application.port.out.FindMembershipPort;
import com.shlee.membership.domain.Membership;
import common.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class FindMembershipService implements FindMembershipUseCase {

    private final MembershipMapper membershipMapper;
    private final FindMembershipPort findMembershipPort;

    @Override
    public Membership findMembership(FindMembershipCommand command) {
        MembershipJpaEntity membershipJpaEntity = findMembershipPort.findMembership(new Membership.MembershipId(command.getMembershipId()));
        return membershipMapper.mapToDomainEntity(membershipJpaEntity);
    }
}
