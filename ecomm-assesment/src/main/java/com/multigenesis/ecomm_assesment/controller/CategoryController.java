package com.multigenesis.ecomm_assesment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multigenesis.ecomm_assesment.config.PaginationSortingConstants;
import com.multigenesis.ecomm_assesment.payload.CategoryDTO;
import com.multigenesis.ecomm_assesment.payload.CategoryResponse;
import com.multigenesis.ecomm_assesment.service.CategoryService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
public class CategoryController {
	
	
	@Autowired
	private CategoryService categoryService;
	
	
	@GetMapping("/public/categories")
	public ResponseEntity<CategoryResponse> getAllCategories
	      (@RequestParam(name="pageNumber", defaultValue=PaginationSortingConstants.PAGE_NUMBER, required=false) Integer pageNumber,
			@RequestParam(name="pageSize", defaultValue=PaginationSortingConstants.PAGE_SIZE, required=false) Integer pageSize,
			@RequestParam(name="sortBy", defaultValue=PaginationSortingConstants.SORT_CATEGORIES_BY, required=false) String sortBy,
			@RequestParam(name="sortOrder", defaultValue=PaginationSortingConstants.SORT_DIRECTION, required=false) String sortOrder){
		CategoryResponse categoryResponse=categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
		return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
	}
	
	@PostMapping("/public/categories")
	public ResponseEntity<CategoryDTO> createCategories(@Valid @RequestBody CategoryDTO categoryDTO){
		CategoryDTO savedCategoryDTO=categoryService.createCategory(categoryDTO);
		return new ResponseEntity<CategoryDTO>(savedCategoryDTO,HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
		CategoryDTO deletedCategory=categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(deletedCategory,HttpStatus.OK);
	}
	
	@PutMapping("/public/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
			@PathVariable Long categoryId) {
		CategoryDTO updatedCategoryDTO=categoryService.updateCategory(categoryDTO, categoryId);
		return new ResponseEntity<CategoryDTO>(updatedCategoryDTO, HttpStatus.OK);
	}
	
	
	
	

}
