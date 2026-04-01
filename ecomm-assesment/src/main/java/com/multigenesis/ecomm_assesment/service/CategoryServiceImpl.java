package com.multigenesis.ecomm_assesment.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.multigenesis.ecomm_assesment.exceptions.APIException;
import com.multigenesis.ecomm_assesment.exceptions.ResourceNotFoundException;
import com.multigenesis.ecomm_assesment.model.Category;
import com.multigenesis.ecomm_assesment.payload.CategoryDTO;
import com.multigenesis.ecomm_assesment.payload.CategoryResponse;
import com.multigenesis.ecomm_assesment.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize, String sortBy, String sortOrder) {
		
		Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
				?Sort.by(sortBy).ascending()
				:Sort.by(sortBy).descending();
		Pageable pageDetails=PageRequest.of(pageNumber, pageSize,sortByAndOrder);
		Page<Category> categoryPage=categoryRepository.findAll(pageDetails);
		List<Category> categories= categoryPage.getContent();
		if(categories.isEmpty()) {
			throw new APIException("No Category created till now");
		}
		List<CategoryDTO> categoryDTOs=categories.stream()
				.map(category->modelMapper.map(category, CategoryDTO.class))
				.toList();
		CategoryResponse categoryResponse=new CategoryResponse();
		categoryResponse.setContent(categoryDTOs);
		categoryResponse.setPageNumber(categoryPage.getNumber());
		categoryResponse.setPageSize(categoryPage.getSize());
		categoryResponse.setTotalElements(categoryPage.getTotalElements());
		categoryResponse.setTotalPages(categoryPage.getTotalPages());
		categoryResponse.setLastPage(categoryPage.isLast());
		return categoryResponse;
	}

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		Category category=modelMapper.map(categoryDTO,Category.class);
		Category existingCategory=categoryRepository.findByCategoryName(category.getCategoryName());
		if(existingCategory!=null) {
			throw new APIException("Category with the name "+category.getCategoryName()+" already exists !!");
		}
		Category savedCategory=categoryRepository.save(category);
		return modelMapper.map(savedCategory,CategoryDTO.class);
		
	}

	@Override
	public CategoryDTO deleteCategory(Long categoryId) {
		Category category=categoryRepository.findById(categoryId)
				.orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
		     categoryRepository.delete(category);
		return modelMapper.map(category,CategoryDTO.class);
	}

	@Override
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
		 Category category=modelMapper.map(categoryDTO,Category.class);
		 Category existingCategory=categoryRepository.findById(categoryId)
				 .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
		 category.setCategoryId(categoryId);
		 existingCategory=categoryRepository.save(category);
	     return modelMapper.map(existingCategory, CategoryDTO.class);
	}

}
