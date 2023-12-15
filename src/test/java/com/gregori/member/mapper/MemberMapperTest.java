package com.gregori.member.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gregori.common.CustomMybatisTest;
import com.gregori.member.domain.Member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CustomMybatisTest
class MemberMapperTest {
	@Autowired
	private MemberMapper memberMapper;

	List<Long> memberIds = new ArrayList<>();

	@AfterEach
	void afterEach() {
		if (!memberIds.isEmpty()) {
			memberMapper.deleteByIds(memberIds);
			memberIds.clear();
		}
	}

	@Test
	@DisplayName("DB에 새로운 회원을 추가한다.")
	void should_insert_when_validMember() {
		// given
		Member member = new Member("name", "new email", "password");

		// when
		memberMapper.insert(member);
		memberIds.add(member.getId());

		// then
		Optional<Member> result = memberMapper.findById(member.getId());
		assertTrue(result.isPresent());
	}

	@Test
	@DisplayName("DB에서 id가 일치하는 회원의 회원 정보를 갱신한다.")
	void should_update_when_memberIdMatch() {
		// given
		Member member = new Member("name", "email", "password");
		memberMapper.insert(member);
		memberIds.add(member.getId());
		member.updateMemberInfo("new name", "new password");

		// when
		memberMapper.update(member);

		// then
		Optional<Member> result = memberMapper.findById(member.getId());
		assertTrue(result.isPresent());
		assertEquals(result.get().getName(), "new name");
	}

	@Test
	@DisplayName("DB에서 id가 일치하는 회원을 삭제한다.")
	void should_delete_when_memberIdMatch() {
		// given
		Member member = new Member("name", "email", "password");
		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		memberMapper.deleteById(member.getId());

		// then
		Optional<Member> result = memberMapper.findById(member.getId());
		assertTrue(result.isEmpty());
	}

	@Test
	@DisplayName("DB에서 Id 목록과 일치하는 회원을 전부 삭제한다.")
	void should_delete_when_memberIdsMatch() {
		// given
		Member member1 = new Member("name", "email1", "password");
		Member member2 = new Member("name", "email2", "password");
		memberMapper.insert(member1);
		memberMapper.insert(member2);
		memberIds.add(member1.getId());
		memberIds.add(member2.getId());

		// when
		memberMapper.deleteByIds(memberIds);

		// then
		Optional<Member> result1 = memberMapper.findById(member1.getId());
		Optional<Member> result2 = memberMapper.findById(member2.getId());
		assertTrue(result1.isEmpty());
		assertTrue(result2.isEmpty());
	}

	@Test
	@DisplayName("DB에서 ID가 일치하는 회원을 조회한다.")
	void should_find_when_memberIdMatch() {
		// given
		Member member = new Member("name", "email", "password");
		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		Optional<Member> result = memberMapper.findById(member.getId());

		// then
		assertTrue(result.isPresent());
		assertEquals(result.get().getId(), member.getId());
	}

	@Test
	@DisplayName("DB에서 email이 일치하는 회원을 조회한다.")
	void should_find_when_memberEmailMatch() {
		// given
		Member member = new Member("name", "email", "password");
		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		Optional<Member> result = memberMapper.findByEmail(member.getEmail());

		// then
		assertTrue(result.isPresent());
		assertEquals(result.get().getEmail(), member.getEmail());
	}
}
