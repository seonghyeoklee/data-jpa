package com.example.mysql.domain.member.repository;

import com.example.mysql.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final static String TABLE = "member";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<Member> findById(Long id) {
        String sql = String.format("SELECT * FROM %s WHERE id = :id", TABLE);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        RowMapper<Member> rowMapper = (rs, rowNum) -> Member.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .nickname(rs.getString("nickname"))
                .birthday(rs.getObject("birthday", LocalDate.class))
                .createdAt(rs.getObject("createdAt", LocalDateTime.class))
                .build();

        Member member = namedParameterJdbcTemplate.queryForObject(sql, params, rowMapper);
        return Optional.ofNullable(member);
    }

    public Member save(Member member) {
        if (member.getId() == null) {
            return insert(member);
        }
        return update(member);
    }

    private Member insert(Member member) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("Member")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return Member.builder()
                .id(id)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .createdAt(member.getCreatedAt())
                .build();
    }

    private Member update(Member member) {
        // TODO
        return member;
    }
}
