package com.gregori.member.mapper;

import java.util.Optional;

import com.gregori.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    Long insert(Member member);
    Long update(Member member);
    Optional<Member> findById(Long memberId);
    Optional<Member> findByEmail(String email);
}
