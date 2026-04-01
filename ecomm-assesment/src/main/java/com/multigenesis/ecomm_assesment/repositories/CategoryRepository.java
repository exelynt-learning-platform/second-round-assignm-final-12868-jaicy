package com.multigenesis.ecomm_assesment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.multigenesis.ecomm_assesment.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{
	
	Category findByCategoryName(String name);

}
