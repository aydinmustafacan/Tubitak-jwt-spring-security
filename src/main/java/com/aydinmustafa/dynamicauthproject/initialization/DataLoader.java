package com.aydinmustafa.dynamicauthproject.initialization;

import com.aydinmustafa.dynamicauthproject.models.Role;
import com.aydinmustafa.dynamicauthproject.models.User;
import com.aydinmustafa.dynamicauthproject.repository.RoleRepository;
import com.aydinmustafa.dynamicauthproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    @Autowired
    public DataLoader(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository= roleRepository;
    }

    public void run(ApplicationArguments args) {
        if(userRepository.findByUsername("admin").isPresent()){
            //Do nothing
        }
        else{
            if (roleRepository.findByName("ROLE_ADMIN").isPresent()){
                Role role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(()-> new RuntimeException("Role with the name ROLE_ADMIN isnt found "));
                User user = new User("admin", "admin@gmail.com", "$2a$10$4L8UkuhPqasWK17phdP3s.UyaVxgmDDJEkqAHS3crmCbq4eJxPIMK");
                Set<Role> roleSet = new HashSet<>();
                roleSet.add(role);
                user.setRoles(roleSet);
                System.out.println("In user part we are ");
                userRepository.save(user);
            }
            else{
                Role role = new Role("ROLE_ADMIN");
                roleRepository.save(role);
                User user = new User("admin", "admin@gmail.com", "$2a$10$4L8UkuhPqasWK17phdP3s.UyaVxgmDDJEkqAHS3crmCbq4eJxPIMK");
                Set<Role> roleSet = new HashSet<>();
                roleSet.add(role);
                user.setRoles(roleSet);
                System.out.println("Here we are");
                System.out.println(role);
                System.out.println(role.getId() + "  " +role.getName());
                userRepository.save(user);
            }
        }
        if(roleRepository.findByName("ROLE_USER").isPresent()){

        }
        else{
            Role role = new Role("ROLE_USER");
            roleRepository.save(role);
        }


    }
}