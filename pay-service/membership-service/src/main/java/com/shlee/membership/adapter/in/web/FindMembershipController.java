package com.shlee.membership.adapter.in.web;

import com.shlee.membership.application.port.in.FindMembershipCommand;
import com.shlee.membership.application.port.in.FindMembershipUseCase;
import com.shlee.membership.domain.Membership;
import common.WebAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@WebAdaptor
@RestController
@RequiredArgsConstructor
public class FindMembershipController {
    private final FindMembershipUseCase findMembershipUseCase;

    @GetMapping("/membership/{membershipId}")
    public ResponseEntity<Membership> findMembershipByMembershipId(@PathVariable String membershipId) {

        FindMembershipCommand command = FindMembershipCommand.builder()
                .membershipId(membershipId)
                .build();

        Membership membership = findMembershipUseCase.findMembership(command);

        return ResponseEntity.ok(membership);
    }
}
