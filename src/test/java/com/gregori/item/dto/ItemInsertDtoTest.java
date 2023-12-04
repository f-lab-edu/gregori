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
class ItemInsertDtoTest {
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("Item 객체 build 테스트")
	void toEntity() {
		// given
		ItemInsertDto dto = new ItemInsertDto("name", 1L, 1L);

		// when
		Item item = dto.toEntity();

		// then
		assertEquals(item.getStatus().toString(), "PRE_SALE");
	}

	@Test
	@DisplayName("name 필드가 비어 있거나 빈 문자열이면 에러 발생")
	void blankNameInputFailsTest() {
		// given
		ItemInsertDto dto1 = new ItemInsertDto(null, 1L, 1L);
		ItemInsertDto dto2 = new ItemInsertDto("", 1L, 1L);
		ItemInsertDto dto3 = new ItemInsertDto(" ", 1L, 1L);

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
	@DisplayName("price 필드가 비어 있으면 에러 발생")
	void nullPriceInputFailsTest() {
		// given
		ItemInsertDto dto = new ItemInsertDto("name", null, 1L);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("inventory 필드가 비어 있으면 에러 발생")
	void nullInventoryInputFailsTest() {
		// given
		ItemInsertDto dto = new ItemInsertDto("name", 1L, null);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("유효한 input이면 성공")
	void validInputSucceedsTest() {
		// given
		ItemInsertDto dto = new ItemInsertDto("name", 1L, 1L);

		//when
		var result = validator.validate(dto);

		//then
		assertTrue(result.isEmpty());
	}
}
