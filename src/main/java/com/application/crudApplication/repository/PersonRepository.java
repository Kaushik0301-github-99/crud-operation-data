package com.application.crudApplication.repository;

import com.application.crudApplication.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person,Long> {
}
