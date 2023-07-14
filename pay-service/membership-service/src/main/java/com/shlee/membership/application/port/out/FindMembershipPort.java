package com.shlee.membership.application.port.out;

import com.shlee.membership.adapter.out.persistence.MembershipJpaEntity;
import com.shlee.membership.domain.Membership;

public interface FindMembershipPort {

    MembershipJpaEntity findMembership(
            Membership.MembershipId membershipId
    );
}
