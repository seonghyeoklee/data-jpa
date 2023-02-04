package com.example.mysql.domain.member.repository;

import com.example.mysql.domain.member.entity.MemberNicknameHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberNicknameHistoryRepository {
    private final static String TABLE = "MemberNicknameHistory";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<MemberNicknameHistory> findAllByMemberId(Long memberId) {
        String sql = String.format("SELECT * FROM %s WHERE memberId = :memberId", TABLE);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId);

        RowMapper<MemberNicknameHistory> rowMapper = (rs, rowNum) -> MemberNicknameHistory.builder()
                .id(rs.getLong("id"))
                .memberId(rs.getLong("memberId"))
                .nickname(rs.getString("nickname"))
                .createdAt(rs.getObject("createdAt", LocalDateTime.class))
                .build();

        MemberNicknameHistory memberNicknameHistory = namedParameterJdbcTemplate.queryForObject(sql, params, rowMapper);
        return Optional.ofNullable(memberNicknameHistory);
    }

    public MemberNicknameHistory save(MemberNicknameHistory memberNicknameHistory) {
        if (memberNicknameHistory.getId() == null) {
            return insert(memberNicknameHistory);
        }
        throw new UnsupportedOperationException("MemberNicknameHistory는 갱신을 지원하지 않습니다.");
    }

    private MemberNicknameHistory insert(MemberNicknameHistory memberNicknameHistory) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(memberNicknameHistory);
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return MemberNicknameHistory.builder()
                .id(id)
                .memberId(memberNicknameHistory.getMemberId())
                .nickname(memberNicknameHistory.getNickname())
                .createdAt(memberNicknameHistory.getCreatedAt())
                .build();
    }
}
