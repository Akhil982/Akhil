package com.capg.ow.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capg.ow.security.model.entity.Role;
import com.capg.ow.security.model.entity.RoleName;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName roleName);
}