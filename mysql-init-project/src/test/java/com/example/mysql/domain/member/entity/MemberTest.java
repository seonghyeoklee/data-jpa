package com.example.mysql.domain.member.entity;

import com.example.mysql.util.MemberFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberTest {

    @DisplayName("회원은 닉네임을 변경할 수 있다.")
    @Test
    void testChangeNickname() {
        // given
        Member member = MemberFixtureFactory.create();

        // when
        member.changeNickname("shlee");

        // then
        assertEquals("shlee", member.getNickname());
    }

    @DisplayName("회원의 닉네임은 10자를 초과할 수 없다.")
    @Test
    void testChangeNicknameMaxLength() {
        // given
        Member member = MemberFixtureFactory.create();

        // when, then
        assertThrows(IllegalArgumentException.class, () -> member.changeNickname("aaaaaaaaaaaaa"));
    }

}
