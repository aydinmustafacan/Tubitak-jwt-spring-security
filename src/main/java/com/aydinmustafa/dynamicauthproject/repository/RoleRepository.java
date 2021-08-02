package com.aydinmustafa.dynamicauthproject.repository;



import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aydinmustafa.dynamicauthproject.models.Role;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {


    Optional<Role> findByName(String name);

    @Override
    Optional<Role> findById(Long aLong);

    Boolean existsByName(String name);


}

