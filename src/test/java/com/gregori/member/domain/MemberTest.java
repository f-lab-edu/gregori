package com.gregori.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gregori.member.domain.Member.Status.ACTIVATE;
import static com.gregori.member.domain.Member.Status.DEACTIVATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MemberTest {

	@Test
	@DisplayName("Member 객체의 상태를 'ACTIAVTE'로 변경한다.")
	void should_activate() {

		// given
		Member member = new Member("name", "email", "password");
		member.deactivate();
		Member.Status status = member.getStatus();

		// when
		member.activate();
		Member.Status result = member.getStatus();

		// then
		assertEquals(status, DEACTIVATE);
		assertEquals(result, ACTIVATE);

	}

	@Test
	@DisplayName("Member 객체의 상태를 'DEACTIVATE'로 변경한다.")
	void should_deactivate() {

		// given
		Member member = new Member("name", "email", "password");
		Member.Status status = member.getStatus();

		// when
		member.deactivate();
		Member.Status result = member.getStatus();

		// then
		assertEquals(status, ACTIVATE);
		assertEquals(result, DEACTIVATE);
	}

	@Test
	@DisplayName("Member 객체의 필드를 builder 패턴으로 생성하고 getter 메서드로 조회한다.")
	void should_getFields_when_buildMember() {

		// given
		Member member = Member.builder()
			.name("name")
			.email("email")
			.password("password")
			.build();

		// when, then
		assertNull(member.getId());
		assertEquals(member.getName(), "name");
		assertEquals(member.getEmail(), "email");
		assertEquals(member.getPassword(), "password");
		assertEquals(member.getStatus(), ACTIVATE);
		assertNull(member.getCreatedAt());
		assertNull(member.getUpdatedAt());
	}
}
