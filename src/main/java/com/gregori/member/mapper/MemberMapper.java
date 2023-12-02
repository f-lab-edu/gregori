package com.gregori.member.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.member.domain.Member;

@Mapper
public interface MemberMapper {
    Long insert(Member member);
    Long update(Member member);
    Long deleteByEmails(List<String> memberEmails);
    Optional<Member> findById(Long memberId);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndPassword(String email, String password);
}
