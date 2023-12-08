package com.gregori.item.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class ItemUpdateDtoTest {
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("id 필드가 비어 있으면 에러가 발생한다.")
	void nullIdInputFailsTest() {
		// given
		ItemUpdateDto dto = new ItemUpdateDto(null, "name", 1L, 1L);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("name 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void blankNameInputFailsTest() {
		// given
		ItemUpdateDto dto1 = new ItemUpdateDto(1L, null, 1L, 1L);
		ItemUpdateDto dto2 = new ItemUpdateDto(1L, "", 1L, 1L);
		ItemUpdateDto dto3 = new ItemUpdateDto(1L, " ", 1L, 1L);

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
	@DisplayName("price 필드가 비어 있으면 에러가 발생한다.")
	void nullPriceInputFailsTest() {
		// given
		ItemUpdateDto dto = new ItemUpdateDto(1L, "name", null, 1L);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("inventory 필드가 비어 있으면 에러가 발생한다.")
	void nullInventoryInputFailsTest() {
		// given
		ItemUpdateDto dto = new ItemUpdateDto(1L, "name", 1L, null);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("입력값이 올바르면 ItemUpdateDto 객체 생성에 성공한다.")
	void validInputSucceedsTest() {
		// given
		ItemUpdateDto dto = new ItemUpdateDto(1L, "name", 1L, 1L);

		//when
		var result = validator.validate(dto);

		//then
		assertTrue(result.isEmpty());
	}
}
