package com.gregori.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemberNameUpdateDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("올바른 입력값이면 MemberNameUpdateDto 객체 생성에 성공한다.")
	void should_createMemberNameUpdateDto_when_validInput() {

		// given
		MemberNameUpdateDto dto = new MemberNameUpdateDto(1L, "일호");

		// when
		var result = validator.validate(dto);

		// then
		assertTrue(result.isEmpty());
	}

	@Test
	@DisplayName("id 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullId() {

		// given
		MemberNameUpdateDto dto = new MemberNameUpdateDto(null, "이름");

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("name 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankName() {

		// given
		MemberNameUpdateDto dto1 = new MemberNameUpdateDto(1L, null);
		MemberNameUpdateDto dto2 = new MemberNameUpdateDto(1L, "");
		MemberNameUpdateDto dto3 = new MemberNameUpdateDto(1L, " ");

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
		MemberNameUpdateDto dto1 = new MemberNameUpdateDto(1L, "김");
		MemberNameUpdateDto dto2 = new MemberNameUpdateDto(1L, "kimchulsu");
		MemberNameUpdateDto dto3 = new MemberNameUpdateDto(1L, "김철수1");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);

		// then
		assertFalse(result1.isEmpty());
		assertFalse(result2.isEmpty());
		assertFalse(result3.isEmpty());
	}
}