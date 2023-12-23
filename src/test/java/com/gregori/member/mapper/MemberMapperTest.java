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

import static com.gregori.auth.domain.Authority.GENERAL_MEMBER;
import static com.gregori.auth.domain.Authority.SELLING_MEMBER;
import static com.gregori.member.domain.Member.Status.ACTIVATE;
import static com.gregori.member.domain.Member.Status.DEACTIVATE;
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
	@DisplayName("새로운 회원을 추가한다.")
	void should_insert() {

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
	@DisplayName("회원 이름을 갱신한다.")
	void should_updateName() {

		// given
		Member member = new Member("name", "email", "password");
		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		memberMapper.updateName(member.getId(), "new name");

		// then
		Optional<Member> result = memberMapper.findById(member.getId());
		assertTrue(result.isPresent());
		assertEquals(member.getName(), "name");
		assertEquals(result.get().getName(), "new name");
	}

	@Test
	@DisplayName("회원 비밀번호를 갱신한다.")
	void should_updatePassword() {

		// given
		Member member = new Member("name", "email", "password");
		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		memberMapper.updatePassword(member.getId(), "new password");

		// then
		Optional<Member> result = memberMapper.findById(member.getId());
		assertTrue(result.isPresent());
		assertEquals(member.getPassword(), "password");
		assertEquals(result.get().getPassword(), "new password");
	}

	@Test
	@DisplayName("회원 상태를 갱신한다.")
	void should_updateStatus() {

		// given
		Member member = new Member("name", "email", "password");
		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		memberMapper.updateStatus(member.getId(), DEACTIVATE);

		// then
		Optional<Member> result = memberMapper.findById(member.getId());
		assertTrue(result.isPresent());
		assertEquals(member.getStatus(), ACTIVATE);
		assertEquals(result.get().getStatus(), DEACTIVATE);
	}

	@Test
	@DisplayName("회원 권한을 갱신한다.")
	void should_updateAuthority() {

		// given
		Member member = new Member("name", "email", "password");
		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		memberMapper.updateAuthority(member.getId(), SELLING_MEMBER);

		// then
		Optional<Member> result = memberMapper.findById(member.getId());
		assertTrue(result.isPresent());
		assertEquals(member.getAuthority(), GENERAL_MEMBER);
		assertEquals(result.get().getAuthority(), SELLING_MEMBER);
	}

	@Test
	@DisplayName("id로 회원을 삭제한다.")
	void should_deleteById() {

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
	@DisplayName("id 목록으로 회원을 삭제한다.")
	void should_deleteByIds() {

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
	@DisplayName("id로 회원을 조회한다.")
	void should_findById() {

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
	@DisplayName("email로 회원을 조회한다.")
	void should_findByEmail() {

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
