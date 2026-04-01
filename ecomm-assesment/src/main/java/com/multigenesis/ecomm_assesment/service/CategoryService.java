package com.multigenesis.ecomm_assesment.service;

import java.util.List;

import com.multigenesis.ecomm_assesment.payload.CategoryDTO;
import com.multigenesis.ecomm_assesment.payload.CategoryResponse;

public interface CategoryService {
	
	CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy, String sortOrder);
	CategoryDTO createCategory(CategoryDTO categoryDTO);
	CategoryDTO deleteCategory(Long categoryId);
	CategoryDTO updateCategory(CategoryDTO categoryDTO,Long categoryId);

}
