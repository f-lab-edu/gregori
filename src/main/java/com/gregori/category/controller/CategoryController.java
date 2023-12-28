package com.gregori.category.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gregori.auth.domain.LoginCheck;
import com.gregori.category.domain.Category;
import com.gregori.category.dto.CategoryRequestDto;
import com.gregori.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

import static com.gregori.auth.domain.Authority.ADMIN_MEMBER;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

	private final CategoryService categoryService;

	@LoginCheck(ADMIN_MEMBER)
	@PostMapping
	public ResponseEntity<Void> createCategory(@RequestBody @Validated CategoryRequestDto dto) {

		Long categoryId = categoryService.saveCategory(dto.getName());

		return ResponseEntity.created(URI.create("/category/" + categoryId)).build();
	}

	@LoginCheck(ADMIN_MEMBER)
	@PostMapping("/{categoryId}")
	public ResponseEntity<Void> updateCategoryName(@PathVariable Long categoryId,
		@RequestBody @Validated CategoryRequestDto dto) {

		categoryService.updateCategoryName(categoryId, dto.getName());

		return ResponseEntity.noContent().build();
	}

	@LoginCheck(ADMIN_MEMBER)
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {

		categoryService.deleteCategory(categoryId);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<Category> getCategory(@PathVariable Long categoryId) {

		Category category = categoryService.getCategory(categoryId);

		return ResponseEntity.ok().body(category);
	}

	@GetMapping
	public ResponseEntity<List<Category>> getCategories(
		@RequestParam(defaultValue = "1") int page) {

		List<Category> categories = categoryService.getCategories(page);

		return ResponseEntity.ok().body(categories);
	}
}
