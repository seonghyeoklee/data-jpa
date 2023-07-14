package com.shlee.membership.application.service;

import com.shlee.membership.adapter.out.persistence.MembershipJpaEntity;
import com.shlee.membership.adapter.out.persistence.MembershipMapper;
import com.shlee.membership.application.port.in.RegisterMembershipCommand;
import com.shlee.membership.application.port.in.RegisterMembershipUseCase;
import com.shlee.membership.application.port.out.RegisterMembershipPort;
import com.shlee.membership.domain.Membership;
import common.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class RegisterMembershipService implements RegisterMembershipUseCase {

    private final MembershipMapper membershipMapper;
    private final RegisterMembershipPort registerMembershipPort;

    @Override
    public Membership registerMembership(RegisterMembershipCommand command) {

        MembershipJpaEntity membershipJpaEntity = registerMembershipPort.createMembership(
                new Membership.MembershipName(command.getName()),
                new Membership.MembershipEmail(command.getEmail()),
                new Membership.MembershipAddress(command.getAddress()),
                new Membership.MembershipIsValid(command.isValid()),
                new Membership.MembershipIsCorp(command.isCorp())
        );

        return membershipMapper.mapToDomainEntity(membershipJpaEntity);
    }
}
