package com.gregori.category.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gregori.category.domain.Category;
import com.gregori.category.service.CategoryService;
import com.gregori.common.response.CustomResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.gregori.common.response.SuccessMessage.CREATE;
import static com.gregori.common.response.SuccessMessage.UPDATE;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

	private final CategoryService categoryService;

	@PostMapping
	public ResponseEntity<CustomResponse<Long>> createCategory(@RequestBody String name) {

		CustomResponse<Long> response = CustomResponse.success(categoryService.saveCategory(name), CREATE);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/{categoryId}")
	public ResponseEntity<CustomResponse<Long>> updateCategory(@PathVariable Long categoryId, @RequestBody String name) {

		CustomResponse<Long> response = CustomResponse.success(categoryService.updateCategoryName(categoryId, name), UPDATE);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<CustomResponse<Long>> deleteCategory(@PathVariable Long categoryId) {

		CustomResponse<Long> response = CustomResponse.success(categoryService.deleteCategory(categoryId), UPDATE);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<CustomResponse<Category>> getCategory(@PathVariable Long categoryId) {

		CustomResponse<Category> response = CustomResponse.success(categoryService.getCategory(categoryId), UPDATE);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping
	public ResponseEntity<CustomResponse<List<Category>>> getCategories(
		@RequestParam(defaultValue = "10") int limit,
		@RequestParam(defaultValue = "0") int offset) {

		CustomResponse<List<Category>> response = CustomResponse.success(categoryService.getCategories(limit, offset), UPDATE);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
