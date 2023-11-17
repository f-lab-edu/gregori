package com.gregori.repository.mapper;

import com.gregori.domain.member.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    Long insert(Member member);
    Long update(Member member);
    Member findById(Long memberId);
}
