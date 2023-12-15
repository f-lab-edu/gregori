package com.gregori.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.member.domain.Member;

import static com.gregori.member.domain.Member.Status.ACTIVATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MemberResponseDtoTest {
	@Test
	@DisplayName("toEntity 메서드를 호출하면 MemberResponseDto 객체를 t한다.")
	void should_createMemberResponseDtoSuccess_when_toEntityMethodOccurs() {
		// given
		Member member = Member.builder()
			.name("일호")
			.email("a@a.a")
			.password("aa11111!")
			.build();

		// when
		MemberResponseDto result = new MemberResponseDto().toEntity(member);

		// then
		assertNotNull(result);
		assertEquals(result.getName(), member.getName());
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
