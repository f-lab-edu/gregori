package com.gregori.item.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.gregori.item.domain.Item;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@SpringBootTest
@ActiveProfiles("test")
class ItemCreateDtoTest {
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("Item 객체를 builder 패턴으로 생성한다.")
	void toEntity() {
		// given
		ItemCreateDto dto = new ItemCreateDto("name", 1L, 1L);

		// when
		Item item = dto.toEntity();

		// then
		assertEquals(item.getStatus().toString(), "PRE_SALE");
	}

	@Test
	@DisplayName("name 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void blankNameInputFailsTest() {
		// given
		ItemCreateDto dto1 = new ItemCreateDto(null, 1L, 1L);
		ItemCreateDto dto2 = new ItemCreateDto("", 1L, 1L);
		ItemCreateDto dto3 = new ItemCreateDto(" ", 1L, 1L);

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
		ItemCreateDto dto = new ItemCreateDto("name", null, 1L);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("inventory 필드가 비어 있으면 에러가 발생한다.")
	void nullInventoryInputFailsTest() {
		// given
		ItemCreateDto dto = new ItemCreateDto("name", 1L, null);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("올바른 입력값이면 ItemCreateDto 객체 생성에 성공한다.")
	void validInputSucceedsTest() {
		// given
		ItemCreateDto dto = new ItemCreateDto("name", 1L, 1L);

		//when
		var result = validator.validate(dto);

		//then
		assertTrue(result.isEmpty());
	}
}
