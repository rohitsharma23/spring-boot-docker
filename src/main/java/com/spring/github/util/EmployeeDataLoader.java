package com.spring.github.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.github.entity.EmployeeEntity;
import com.spring.github.repository.EmployeeRepository;

@Component
public class EmployeeDataLoader {
	@Autowired
	private EmployeeRepository employeeRepository;

	@PostConstruct
	public void loadData() {
		EmployeeEntity entity = new EmployeeEntity();
		entity.setId(1l);
		entity.setName("rohit");
		entity.setType("admin");
		employeeRepository.save(entity);

	}
}
