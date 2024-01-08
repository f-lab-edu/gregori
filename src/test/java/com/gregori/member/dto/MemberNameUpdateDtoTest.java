package com.gregori.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

class MemberNameUpdateDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("올바른 입력값이면 MemberNameUpdateDto 객체 생성에 성공한다.")
	void should_createMemberNameUpdateDto_when_validInput() {

		// given
		MemberNameUpdateDto dto = new MemberNameUpdateDto("일호");

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("name 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankName() {

		// given
		MemberNameUpdateDto dto1 = new MemberNameUpdateDto(null);
		MemberNameUpdateDto dto2 = new MemberNameUpdateDto("");
		MemberNameUpdateDto dto3 = new MemberNameUpdateDto(" ");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);

		// then
		assertThat(result1.isEmpty()).isFalse();
		assertThat(result2.isEmpty()).isFalse();
		assertThat(result3.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("name 필드의 패턴이 불일치하면 에러가 발생한다.")
	void should_ValidException_when_mismatchedName() {

		// given
		MemberNameUpdateDto dto1 = new MemberNameUpdateDto("김");
		MemberNameUpdateDto dto2 = new MemberNameUpdateDto("kimchulsu");
		MemberNameUpdateDto dto3 = new MemberNameUpdateDto("김철수1");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);

		// then
		assertThat(result1.isEmpty()).isFalse();
		assertThat(result2.isEmpty()).isFalse();
		assertThat(result3.isEmpty()).isFalse();
	}
}