package com.gregori.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.member.domain.Member;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static com.gregori.member.domain.Member.Status.ACTIVATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemberRegisterDtoTest {
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("toEntity 메서드를 호출하면 Member 객체를 builder 패턴으로 생성하는데 성공한다.")
	void should_buildMemberSuccess_when_toEntityMethodOccurs() {
		// given
		MemberRegisterDto dto = new MemberRegisterDto("이름", "a@a.a", "aa11111!");

		// when
		Member member = dto.toEntity(dto.getPassword());

		// then
		assertNotNull(member);
		assertEquals(member.getStatus(), ACTIVATE);
	}

	@Test
	@DisplayName("name 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankName() {
		// given
		MemberRegisterDto dto1 = new MemberRegisterDto(null, "a@a.a", "aa11111!");
		MemberRegisterDto dto2 = new MemberRegisterDto("", "a@a.a", "aa11111!");
		MemberRegisterDto dto3 = new MemberRegisterDto(" ", "a@a.a", "aa11111!");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);

		// then
		assertFalse(result1.isEmpty());
		assertFalse(result2.isEmpty());
		assertFalse(result3.isEmpty());
	}

	@Test
	@DisplayName("name 필드의 패턴이 불일치하면 에러가 발생한다.")
	void should_ValidException_when_mismatchedName() {
		// given
		MemberRegisterDto dto1 = new MemberRegisterDto("김", "email", "aa11111!");
		MemberRegisterDto dto2 = new MemberRegisterDto("kimchulsu", "email", "aa11111!");
		MemberRegisterDto dto3 = new MemberRegisterDto("김철수1", "email", "aa11111!");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);

		// then
		assertFalse(result1.isEmpty());
		assertFalse(result2.isEmpty());
		assertFalse(result3.isEmpty());
	}

	@Test
	@DisplayName("email 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankEmail() {
		// given
		MemberRegisterDto dto1 = new MemberRegisterDto("이름", null, "aa11111!");
		MemberRegisterDto dto2 = new MemberRegisterDto("이름", "", "aa11111!");
		MemberRegisterDto dto3 = new MemberRegisterDto("이름", " ", "aa11111!");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);

		// then
		assertFalse(result1.isEmpty());
		assertFalse(result2.isEmpty());
		assertFalse(result3.isEmpty());
	}

	@Test
	@DisplayName("email 필드의 패턴이 불일치하면 에러가 발생한다.")
	void should_ValidException_when_mismatchedEmail() {
		// given
		MemberRegisterDto dto1 = new MemberRegisterDto("이름", "a", "aa11111!");
		MemberRegisterDto dto2 = new MemberRegisterDto("이름", "a@a.", "aa11111!");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);


		// then
		assertFalse(result1.isEmpty());
		assertFalse(result2.isEmpty());
	}

	@Test
	@DisplayName("password 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankPassword() {
		// given
		MemberRegisterDto dto1 = new MemberRegisterDto("이름", "a@a.a", null);
		MemberRegisterDto dto2 = new MemberRegisterDto("이름","a@a.a",  "");
		MemberRegisterDto dto3 = new MemberRegisterDto("이름", "a@a.a", " ");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);

		// then
		assertFalse(result1.isEmpty());
		assertFalse(result2.isEmpty());
		assertFalse(result3.isEmpty());
	}

	@Test
	@DisplayName("password 필드의 패턴이 불일치하면 에러가 발생한다.")
	void should_ValidException_when_mismatchedPassword() {
		// given
		MemberRegisterDto dto1 = new MemberRegisterDto("이름", "email", "pass%5");
		MemberRegisterDto dto2 = new MemberRegisterDto("이름", "email", "passwordpassword%5");
		MemberRegisterDto dto3 = new MemberRegisterDto("이름", "email", "374833e**");
		MemberRegisterDto dto4 = new MemberRegisterDto("이름", "email", "password!");
		MemberRegisterDto dto5 = new MemberRegisterDto("이름", "email", "pass5324");
		MemberRegisterDto dto6 = new MemberRegisterDto("이름", "email", "pass 5324");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);
		var result4 = validator.validate(dto4);
		var result5 = validator.validate(dto5);
		var result6 = validator.validate(dto6);

		// then
		assertFalse(result1.isEmpty());
		assertFalse(result2.isEmpty());
		assertFalse(result3.isEmpty());
		assertFalse(result4.isEmpty());
		assertFalse(result5.isEmpty());
		assertFalse(result6.isEmpty());
	}


	@Test
	@DisplayName("올바른 입력값이면 MemberRegisterDto 객체 생성에 성공한다.")
	void should_createMemberRegisterDto_when_validInput() {
		// given
		MemberRegisterDto dto = new MemberRegisterDto("이름", "a@a.a", "aa11111!");

		// when
		var result = validator.validate(dto);

		// then
		assertTrue(result.isEmpty());
	}
}
