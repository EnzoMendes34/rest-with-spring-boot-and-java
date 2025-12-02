package com.github.EnzoMendes34.repository;

import com.github.EnzoMendes34.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {}
