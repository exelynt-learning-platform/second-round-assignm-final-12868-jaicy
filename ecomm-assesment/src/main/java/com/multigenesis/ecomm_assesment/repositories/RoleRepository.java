package com.multigenesis.ecomm_assesment.repositories;

import org.springframework.stereotype.Repository;

import com.multigenesis.ecomm_assesment.model.AppRole;
import com.multigenesis.ecomm_assesment.model.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{

	Optional<Role> findByRoleName(AppRole roleUser);

}
