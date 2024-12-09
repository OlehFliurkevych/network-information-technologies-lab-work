package com.okta.developer.jugtours.repository;

import com.okta.developer.jugtours.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
