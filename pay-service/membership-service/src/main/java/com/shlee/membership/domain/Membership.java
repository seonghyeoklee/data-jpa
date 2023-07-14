package com.shlee.membership.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Membership {
    private final String membershipId;
    private final String name;
    private final String email;
    private final String address;
    private final boolean isValid;
    private final boolean isCorp;

    public static Membership generateMembership(
            MembershipId membershipId,
            MembershipName membershipName,
            MembershipEmail membershipEmail,
            MembershipAddress membershipAddress,
            MembershipIsValid membershipIsValid,
            MembershipIsCorp membershipIsCorp
    ) {
        return new Membership(
                membershipId.membershipId,
                membershipName.name,
                membershipEmail.email,
                membershipAddress.address,
                membershipIsValid.isValid,
                membershipIsCorp.isCorp
        );
    }

    @Value
    public static class MembershipId {
        public MembershipId(String value) {
            this.membershipId = value;
        }

        String membershipId;
    }

    @Value
    public static class MembershipName {
        public MembershipName(String value) {
            this.name = value;
        }

        String name;
    }

    @Value
    public static class MembershipEmail {
        public MembershipEmail(String value) {
            this.email = value;
        }

        String email;
    }

    @Value
    public static class MembershipAddress {
        public MembershipAddress(String value) {
            this.address = value;
        }

        String address;
    }

    @Value
    public static class MembershipIsValid {
        public MembershipIsValid(boolean value) {
            this.isValid = value;
        }

        boolean isValid;
    }

    @Value
    public static class MembershipIsCorp {
        public MembershipIsCorp(boolean value) {
            this.isCorp = value;
        }

        boolean isCorp;
    }
}
