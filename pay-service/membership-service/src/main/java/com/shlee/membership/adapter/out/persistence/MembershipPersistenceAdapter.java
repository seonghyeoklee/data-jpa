package com.shlee.membership.adapter.out.persistence;

import com.shlee.membership.application.port.out.FindMembershipPort;
import com.shlee.membership.application.port.out.RegisterMembershipPort;
import com.shlee.membership.domain.Membership;
import common.PersistenceAdaptor;
import lombok.RequiredArgsConstructor;

@PersistenceAdaptor
@RequiredArgsConstructor
public class MembershipPersistenceAdapter implements RegisterMembershipPort, FindMembershipPort {

    private final SpringDataMembershipRepository membershipRepository;

    @Override
    public MembershipJpaEntity createMembership(Membership.MembershipName membershipName, Membership.MembershipEmail membershipEmail, Membership.MembershipAddress membershipAddress, Membership.MembershipIsValid membershipIsValid, Membership.MembershipIsCorp membershipIsCorp) {
        return membershipRepository.save(
                new MembershipJpaEntity(
                        membershipName.getName(),
                        membershipEmail.getEmail(),
                        membershipAddress.getAddress(),
                        membershipIsValid.isValid(),
                        membershipIsCorp.isCorp()
                )
        );
    }

    @Override
    public MembershipJpaEntity findMembership(Membership.MembershipId membershipId) {
        return membershipRepository.findById(Long.parseLong(membershipId.getMembershipId()))
                .orElseThrow();
    }
}
