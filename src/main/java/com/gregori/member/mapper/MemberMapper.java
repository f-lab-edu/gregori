package com.gregori.member.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.auth.domain.Authority;
import com.gregori.common.domain.IsDeleted;
import com.gregori.member.domain.Member;

@Mapper
public interface MemberMapper {

    Long insert(Member member);
    void updateName(Long id, String name);
    void updatePassword(Long id, String password);
    void updateAuthority(Long id, Authority authority);
    void updateIsDeleted(Long id, IsDeleted isDeleted);
    void deleteById(Long id);
    void deleteByIds(List<Long> ids);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
}
