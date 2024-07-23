package com.ashokit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ashokit.entity.FormData;

public interface FormDataRepository extends JpaRepository<FormData, Long> {
	
	FormData findTopByOrderByIdDesc();

}
