package com.shlee.membership.application.port.in;

import common.SelfValidating;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class FindMembershipCommand extends SelfValidating<RegisterMembershipCommand> {
    private final String membershipId;
}
