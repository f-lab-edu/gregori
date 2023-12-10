package com.gregori.member.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.gregori.common.exception.NotFoundException;
import com.gregori.member.domain.Member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
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
	@DisplayName("Members 테이블에 새로운 셀러를 추가한다.")
	void insert() {
		// given
		Member member = Member.builder()
			.name("김일호")
			.email("a@a.a")
			.password("aa11111!")
			.build();

		// when
		memberMapper.insert(member);
		memberIds.add(member.getId());
		Member result = memberMapper.findById(member.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), member.getId());
		assertEquals(result.getName(), member.getName());
	}

	@Test
	@DisplayName("Members 테이블의 상품을 수정한다.")
	void update() {
		// given
		Member member = Member.builder()
			.name("김일호")
			.email("a@a.a")
			.password("aa11111!")
			.build();

		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		member.updateMemberInfo("김이호", "bb22222@");
		memberMapper.update(member);
		Member result = memberMapper.findById(member.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), member.getId());
		assertEquals(result.getName(), member.getName());
		assertEquals(result.getPassword(), member.getPassword());
	}

	@Test
	@DisplayName("Id 목록과 일치하는 Member 테이블의 셀러를 전부 삭제한다.")
	void deleteByIds() {
		// given
		Member member = Member.builder()
			.name("김일호")
			.email("a@a.a")
			.password("aa11111!")
			.build();

		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		memberMapper.deleteByIds(List.of(member.getId()));
		Member result = memberMapper.findById(member.getId()).orElse(null);

		// then
		assertNull(result);
	}

	@Test
	@DisplayName("Members 테이블에서 id가 일치하는 셀러를 조회한다.")
	void findById() {
		// given
		Member member = Member.builder()
			.name("김일호")
			.email("a@a.a")
			.password("aa11111!")
			.build();

		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		Member result = memberMapper.findById(member.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), member.getId());
		assertEquals(result.getName(), member.getName());
		assertEquals(result.getEmail(), member.getEmail());
		assertEquals(result.getPassword(), member.getPassword());
		assertEquals(result.getStatus(), member.getStatus());
	}

	@Test
	@DisplayName("Members 테이블에서 email이 일치하는 셀러를 조회한다.")
	void findByEmail() {
		// given
		Member member = Member.builder()
			.name("김일호")
			.email("a@a.a")
			.password("aa11111!")
			.build();

		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		Member result = memberMapper.findByEmail(member.getEmail()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), member.getId());
		assertEquals(result.getName(), member.getName());
		assertEquals(result.getEmail(), member.getEmail());
		assertEquals(result.getPassword(), member.getPassword());
		assertEquals(result.getStatus(), member.getStatus());
	}
}
