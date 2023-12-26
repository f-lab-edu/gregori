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
import static com.gregori.common.domain.IsDeleted.TRUE;
import static org.assertj.core.api.Assertions.assertThat;

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
		Optional<Member> result = memberMapper.findById(member.getId());

		// then
		assertThat(member.getId()).isNotNull();
		assertThat(result.isPresent()).isTrue();
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
		Optional<Member> result = memberMapper.findById(member.getId());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(member.getName()).isEqualTo("name");
		assertThat(result.get().getName()).isEqualTo("new name");
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
		Optional<Member> result = memberMapper.findById(member.getId());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(member.getPassword()).isEqualTo("password");
		assertThat(result.get().getPassword()).isEqualTo("new password");
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
		Optional<Member> result = memberMapper.findById(member.getId());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(member.getAuthority()).isEqualTo(GENERAL_MEMBER);
		assertThat(result.get().getAuthority()).isEqualTo(SELLING_MEMBER);
	}

	@Test
	@DisplayName("id로 상품을 논리적으로 삭제한다")
	void should_updateIsDeleted() {

		// given
		Member member = new Member("name", "email", "password");
		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		memberMapper.updateIsDeleted(member.getId(), TRUE);
		Optional<Member> result = memberMapper.findById(member.getId());

		// then
		assertThat(result.isPresent()).isFalse();
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
		Optional<Member> result = memberMapper.findById(member.getId());

		// then
		assertThat(result.isEmpty()).isTrue();
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
		Optional<Member> result1 = memberMapper.findById(member1.getId());
		Optional<Member> result2 = memberMapper.findById(member2.getId());

		// then
		assertThat(result1.isEmpty()).isTrue();
		assertThat(result2.isEmpty()).isTrue();
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
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getId()).isEqualTo(member.getId());
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
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getEmail()).isEqualTo(member.getEmail());
	}
}
