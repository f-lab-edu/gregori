package com.gregori.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.NoArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MemberTest {
	@Test
	@DisplayName("Member 객체의 필드를 수정한다.")
	void updateMemberInfo() {
		// given
		Member member = Member.builder()
			.name("name")
			.email("a@a.a")
			.password("aa11111!")
			.build();

		// when
		member.updateMemberInfo("name update", "bb22222@");

		// then
		assertEquals(member.getName(), "name update");
		assertEquals(member.getPassword(), "bb22222@");
	}

	@Test
	@DisplayName("Member 객체의 상태를 'ACTIAVTE'로 변경한다.")
	void activate() {
		// given
		Member member = Member.builder()
			.name("name")
			.email("a@a.a")
			.password("aa11111!")
			.build();
		member.deactivate();

		// when
		member.activate();

		// then
		assertEquals(member.getStatus().toString(), "ACTIVATE");
	}

	@Test
	@DisplayName("Member 객체의 상태를 'DEACTIVATE'로 변경한다.")
	void deactivate() {
		// given
		Member member = Member.builder()
			.name("name")
			.email("a@a.a")
			.password("aa11111!")
			.build();

		// when
		member.deactivate();

		// then
		assertEquals(member.getStatus().toString(), "DEACTIVATE");
	}


	@Test
	@DisplayName("Member 객체의 필드를 builder 패턴으로 생성하고 getter 메서드로 조회한다.")
	void getterTest() {
		// given
		Member member = Member.builder()
			.name("name")
			.email("a@a.a")
			.password("aa11111!")
			.build();

		// then
		assertNull(member.getId());
		assertEquals(member.getName(), "name");
		assertEquals(member.getEmail(), "a@a.a");
		assertEquals(member.getPassword(), "aa11111!");
		assertEquals(member.getStatus().toString(), "ACTIVATE");
		assertNull(member.getCreatedAt());
		assertNull(member.getUpdatedAt());
	}
}
