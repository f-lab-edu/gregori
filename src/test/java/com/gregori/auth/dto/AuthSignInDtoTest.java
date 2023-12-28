package com.gregori.auth.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

class AuthSignInDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("올바른 입력값이면 AuthSignInDto 객체 생성에 성공한다.")
	void should_createAuthSignInDto_when_validInput() {

		// given
		AuthSignInDto dto = new AuthSignInDto("a@a.a", "aa11111!");

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("email 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankEmail() {

		// given
		AuthSignInDto dto1 = new AuthSignInDto(null, "aa11111!");
		AuthSignInDto dto2 = new AuthSignInDto("", "aa11111!");
		AuthSignInDto dto3 = new AuthSignInDto(" ", "aa11111!");

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
	@DisplayName("email 필드의 패턴이 불일치하면 에러가 발생한다.")
	void should_ValidException_when_mismatchedEmail() {

		// given
		AuthSignInDto dto1 = new AuthSignInDto("a", "aa11111!");
		AuthSignInDto dto2 = new AuthSignInDto("a@a.", "aa11111!");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);


		// then
		assertThat(result1.isEmpty()).isFalse();
		assertThat(result2.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("password 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankPassword() {

		// given
		AuthSignInDto dto1 = new AuthSignInDto("a@a.a", null);
		AuthSignInDto dto2 = new AuthSignInDto("a@a.a",  "");
		AuthSignInDto dto3 = new AuthSignInDto("a@a.a", " ");

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
	@DisplayName("password 필드의 패턴이 불일치하면 에러가 발생한다.")
	void should_ValidException_when_mismatchedPassword() {

		// given
		AuthSignInDto dto1 = new AuthSignInDto("email", "pass%5");
		AuthSignInDto dto2 = new AuthSignInDto("email", "passwordpassword%5");
		AuthSignInDto dto3 = new AuthSignInDto("email", "374833e**");
		AuthSignInDto dto4 = new AuthSignInDto("email", "password!");
		AuthSignInDto dto5 = new AuthSignInDto("email", "pass5324");
		AuthSignInDto dto6 = new AuthSignInDto("email", "pass 5324");

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
