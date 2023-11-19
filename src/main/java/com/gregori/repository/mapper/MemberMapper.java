package com.gregori.repository.mapper;

import java.util.Optional;

import com.gregori.domain.member.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    Long insert(Member member);
    Long update(Member member);
    Optional<Member> findById(Long memberId);
}
