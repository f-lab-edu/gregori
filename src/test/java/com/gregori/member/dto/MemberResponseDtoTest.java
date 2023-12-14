package com.gregori.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.member.domain.Member;

import static com.gregori.member.domain.Member.Status.ACTIVATE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberResponseDtoTest {
	@Test
	@DisplayName("Member를 파라미터로 받아 MemberResponseDto 객체를 builder 패턴으로 생성한다.")
	void should_buildMemberResponseDto_when_memberParameter() {
		// given
		Member member = Member.builder()
			.name("일호")
			.email("a@a.a")
			.password("aa11111!")
			.build();

		// when
		MemberResponseDto dto = new MemberResponseDto().toEntity(member);

		// then
		assertEquals(member.getName(), dto.getName());
		assertEquals(member.getEmail(), dto.getEmail());
	}

	@Test
	@DisplayName("SellerResponseDto 객체의 필드를 getter 메서드로 조회한다.")
	void should_getFields() {
		// given
		MemberResponseDto dto = new MemberResponseDto(1L, "a@a.a", "일호", ACTIVATE);

		// then
		assertEquals(dto.getId(), 1L);
		assertEquals(dto.getEmail(), "a@a.a");
		assertEquals(dto.getName(), "일호");
		assertEquals(dto.getStatus(), ACTIVATE);
	}
}
