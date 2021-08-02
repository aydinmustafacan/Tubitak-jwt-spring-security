package com.aydinmustafa.dynamicauthproject.repository;



import com.aydinmustafa.dynamicauthproject.models.Permission;
import com.aydinmustafa.dynamicauthproject.models.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPermissionName(String permissionName);

    Boolean existsByPermissionName(String permissionName);


}

