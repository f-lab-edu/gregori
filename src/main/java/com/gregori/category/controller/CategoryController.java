package com.gregori.category.controller;

import java.net.URI;
import java.util.List;

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

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

	private final CategoryService categoryService;

	@PostMapping
	public ResponseEntity<Void> createCategory(@RequestBody String name) {

		Long categoryId = categoryService.saveCategory(name);

		return ResponseEntity.created(URI.create("/category/" + categoryId)).build();
	}

	@PostMapping("/{categoryId}")
	public ResponseEntity<Void> updateCategoryName(@PathVariable Long categoryId, @RequestBody String name) {

		categoryService.updateCategoryName(categoryId, name);

		return ResponseEntity.noContent().build();
	}

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
