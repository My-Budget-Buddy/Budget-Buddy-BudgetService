package com.skillstorm.budgetservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.budgetservice.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
