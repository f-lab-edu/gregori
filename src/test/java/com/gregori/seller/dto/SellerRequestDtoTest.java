package com.gregori.seller.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.seller.domain.Seller;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static com.gregori.seller.domain.Seller.Status.OPERATING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SellerRequestDtoTest {
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("Seller 객체를 builder 패턴으로 생성한다.")
	void toEntity() {
		// given
		SellerRequestDto dto = new SellerRequestDto(1L, "000-00-00000", "name");

		// when
		Seller seller = dto.toEntity();

		// then
		assertEquals(seller.getStatus(), OPERATING);
	}

	@Test
	@DisplayName("memberId 필드가 비어 있으면 에러가 발생한다.")
	void nullMemberIdInputFailsTest() {
		// given
		SellerRequestDto dto = new SellerRequestDto(null, "000-00-00000", "name");

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("businessNo 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void blankBusinessNoInputFailsTest() {
		// given
		SellerRequestDto dto1 = new SellerRequestDto(1L, null, "name");
		SellerRequestDto dto2 = new SellerRequestDto(1L, "", "name");
		SellerRequestDto dto3 = new SellerRequestDto(1L, " ", "name");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto1);
		var result3 = validator.validate(dto1);

		// then
		assertFalse(result1.isEmpty());
		assertFalse(result2.isEmpty());
		assertFalse(result3.isEmpty());
	}

	@Test
	@DisplayName("businessNo 필드의 패턴이 불일치하면 에러가 발생한다.")
	void mismatchedBusinessNoInputFailsTest() {
		// given
		SellerRequestDto dto = new SellerRequestDto(1L, "00-00-00", "name");

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("businessName 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void blankBusinessNameInputFailsTest() {
		// given
		SellerRequestDto dto1 = new SellerRequestDto(1L, "000-00-00000", null);
		SellerRequestDto dto2 = new SellerRequestDto(1L, "000-00-00000", "");
		SellerRequestDto dto3 = new SellerRequestDto(1L, "000-00-00000", " ");

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
	@DisplayName("올바른 입력값이면 SellerRegisterDto 객체 생성에 성공한다.")
	void validInputSucceedsTest() {
		// given
		SellerRequestDto dto = new SellerRequestDto(1L, "000-00-00000", "name");

		// when
		var result = validator.validate(dto);

		// then
		assertTrue(result.isEmpty());
	}
}
