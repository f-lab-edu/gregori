package com.gregori.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gregori.member.domain.Member.Status.ACTIVATE;
import static com.gregori.member.domain.Member.Status.DEACTIVATE;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
