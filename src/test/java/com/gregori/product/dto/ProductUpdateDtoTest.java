package com.gregori.product.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static com.gregori.product.domain.Product.Status.PRE_SALE;
import static org.assertj.core.api.Assertions.assertThat;

class ProductUpdateDtoTest {
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("입력값이 올바르면 ProductUpdateDto 객체 생성에 성공한다.")
	void should_createProductUpdateDto_when_validInput() {

		// given
		ProductUpdateDto dto = new ProductUpdateDto(1L, 1L, "name", 1L, 1L, PRE_SALE);

		//when
		var result = validator.validate(dto);

		//then
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("id 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullId() {

		// given
		ProductUpdateDto dto = new ProductUpdateDto(null, 1L, "name", 1L, 1L, PRE_SALE);

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("categoryId 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullCategoryId() {

		// given
		ProductUpdateDto dto = new ProductUpdateDto(1L, null, "name", 1L, 1L, PRE_SALE);

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("name 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankName() {

		// given
		ProductUpdateDto dto1 = new ProductUpdateDto(1L, 1L, null, 1L, 1L, PRE_SALE);
		ProductUpdateDto dto2 = new ProductUpdateDto(1L, 1L, "", 1L, 1L, PRE_SALE);
		ProductUpdateDto dto3 = new ProductUpdateDto(1L, 1L, " ", 1L, 1L, PRE_SALE);

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
	@DisplayName("price 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullPrice() {

		// given
		ProductUpdateDto dto = new ProductUpdateDto(1L, 1L, "name", null, 1L, PRE_SALE);

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("inventory 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullInventory() {

		// given
		ProductUpdateDto dto = new ProductUpdateDto(1L, 1L, "name", 1L, null, PRE_SALE);

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("status 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullStatus() {

		// given
		ProductUpdateDto dto = new ProductUpdateDto(1L, 1L, "name", 1L, 1L, null);

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}
}
