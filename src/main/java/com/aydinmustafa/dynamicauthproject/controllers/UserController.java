package com.aydinmustafa.dynamicauthproject.controllers;


import com.aydinmustafa.dynamicauthproject.models.*;
import com.aydinmustafa.dynamicauthproject.payload.request.NewRoleRequest;
import com.aydinmustafa.dynamicauthproject.payload.request.UserInformationRequest;
import com.aydinmustafa.dynamicauthproject.repository.*;
import com.aydinmustafa.dynamicauthproject.payload.request.UpdateUserRolesRequest;
import com.aydinmustafa.dynamicauthproject.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/updateUserRole")
    public ResponseEntity<?> updateUserRole(@RequestBody UpdateUserRolesRequest updateUserRoles){


        String username = updateUserRoles.getUsername();
        System.out.println("user to be updated found! "+username);
        if (username.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Field of the username is empty "));
        }

        if (!userRepository.existsByUsername(username)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username does not exist! You cannot update it! "));
        }

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new RuntimeException("Role to update does not found with the given name!")
        );

        Set<String> roleSet = updateUserRoles.getRole();



        Set<Role> roles= new HashSet<>();

        if (roleSet.isEmpty()){
            roles = null;
        }
        for(String eachRole:roleSet ){
            System.out.println("role name is "+ eachRole);
            Role rol = roleRepository.findByName(eachRole)
                    .orElseThrow(() -> new RuntimeException("Error role not found error in UserController.java ;)"));
            roles.add(rol);
        }

        user.setRoles(roles);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User's roles updated successfully!"));
    }

    @PostMapping("/retrieveUser")
    public ResponseEntity<?> retrieveUser(@RequestBody UserInformationRequest userInformationRequest){
        String nameOfUser = userInformationRequest.getUsername();

        if (nameOfUser.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Name of the user is empty "));
        }

        if (!userRepository.existsByUsername(nameOfUser)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist! You cannot retrieve information about it! "));
        }

       User user = userRepository.findByUsername(nameOfUser).orElseThrow(
               ()->new RuntimeException("Runtime exception: User was not found with the given username! ")
       );

        Set<String> stringSetOfRoles = new HashSet<>();
        for(Role r : user.getRoles()){
            stringSetOfRoles.add(r.getName());
        }


        String msg = "user_id: "+user.getId()+", username: "+user.getUsername()+
                " user_email:"+user.getEmail()+ ", user_roles "+stringSetOfRoles;

        return ResponseEntity.ok(new MessageResponse(msg));

    }



}

