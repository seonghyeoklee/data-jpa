package com.shlee.membership.application.port.in;

import com.shlee.membership.domain.Membership;

public interface FindMembershipUseCase {
    Membership findMembership(FindMembershipCommand command);
}
