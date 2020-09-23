package com.capg.ow.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capg.ow.security.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

	Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    
	User findByUsername(String username);
	
	Optional<User> findByUsernameOrEmail(String username, String email);

}
