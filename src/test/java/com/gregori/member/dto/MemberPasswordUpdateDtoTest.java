package com.gregori.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

class MemberPasswordUpdateDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("올바른 입력값이면 MemberPasswordUpdateDto 객체 생성에 성공한다.")
	void should_createMemberPasswordUpdateDto_when_validInput() {

		// given
		MemberPasswordUpdateDto dto = new MemberPasswordUpdateDto("aa11111!", "aa11111!");

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("oldPassword 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankOldPassword() {

		// given
		MemberPasswordUpdateDto dto1 = new MemberPasswordUpdateDto(null, "aa11111!");
		MemberPasswordUpdateDto dto2 = new MemberPasswordUpdateDto("", "aa11111!");
		MemberPasswordUpdateDto dto3 = new MemberPasswordUpdateDto(" ", "aa11111!");

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
	@DisplayName("oldPassword 필드의 패턴이 불일치하면 에러가 발생한다.")
	void should_ValidException_when_mismatchedOldPassword() {

		// given
		MemberPasswordUpdateDto dto1 = new MemberPasswordUpdateDto("pass%5", "aa11111!");
		MemberPasswordUpdateDto dto2 = new MemberPasswordUpdateDto("passwordpassword%5", "aa11111!");
		MemberPasswordUpdateDto dto3 = new MemberPasswordUpdateDto("374833e**", "aa11111!");
		MemberPasswordUpdateDto dto4 = new MemberPasswordUpdateDto("password!", "aa11111!");
		MemberPasswordUpdateDto dto5 = new MemberPasswordUpdateDto("pass5324", "aa11111!");
		MemberPasswordUpdateDto dto6 = new MemberPasswordUpdateDto("pass 5324", "aa11111!");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);
		var result4 = validator.validate(dto4);
		var result5 = validator.validate(dto5);
		var result6 = validator.validate(dto6);

		// then
		assertThat(result1.isEmpty()).isFalse();
		assertThat(result2.isEmpty()).isFalse();
		assertThat(result3.isEmpty()).isFalse();
		assertThat(result4.isEmpty()).isFalse();
		assertThat(result5.isEmpty()).isFalse();
		assertThat(result6.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("newPassword 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankNewPassword() {

		// given
		MemberPasswordUpdateDto dto1 = new MemberPasswordUpdateDto("aa11111!", null);
		MemberPasswordUpdateDto dto2 = new MemberPasswordUpdateDto("aa11111!", "");
		MemberPasswordUpdateDto dto3 = new MemberPasswordUpdateDto("aa11111!", " ");

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
	@DisplayName("newPassword 필드의 패턴이 불일치하면 에러가 발생한다.")
	void should_ValidException_when_mismatchedNewPassword() {

		// given
		MemberPasswordUpdateDto dto1 = new MemberPasswordUpdateDto("aa11111!", "pass%5");
		MemberPasswordUpdateDto dto2 = new MemberPasswordUpdateDto("aa11111!", "passwordpassword%5");
		MemberPasswordUpdateDto dto3 = new MemberPasswordUpdateDto("aa11111!", "374833e**");
		MemberPasswordUpdateDto dto4 = new MemberPasswordUpdateDto("aa11111!", "password!");
		MemberPasswordUpdateDto dto5 = new MemberPasswordUpdateDto("aa11111!", "pass5324");
		MemberPasswordUpdateDto dto6 = new MemberPasswordUpdateDto("aa11111!", "pass 5324");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);
		var result4 = validator.validate(dto4);
		var result5 = validator.validate(dto5);
		var result6 = validator.validate(dto6);

		// then
		assertThat(result1.isEmpty()).isFalse();
		assertThat(result2.isEmpty()).isFalse();
		assertThat(result3.isEmpty()).isFalse();
		assertThat(result4.isEmpty()).isFalse();
		assertThat(result5.isEmpty()).isFalse();
		assertThat(result6.isEmpty()).isFalse();
	}
}
