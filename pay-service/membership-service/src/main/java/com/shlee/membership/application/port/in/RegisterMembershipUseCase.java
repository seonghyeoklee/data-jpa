package com.shlee.membership.application.port.in;

import com.shlee.membership.domain.Membership;

public interface RegisterMembershipUseCase {
    Membership registerMembership(RegisterMembershipCommand command);
}
