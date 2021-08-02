package com.aydinmustafa.dynamicauthproject.controllers;


import com.aydinmustafa.dynamicauthproject.repository.*;
import com.aydinmustafa.dynamicauthproject.models.*;
import com.aydinmustafa.dynamicauthproject.payload.request.*;
import com.aydinmustafa.dynamicauthproject.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @PostMapping("/addPermission")
    public ResponseEntity<?> addNewPermission(@RequestBody NewPermissionRequest newPermissionRequest) {
        String nameOfPermission = newPermissionRequest.getPermission_name();

        if (nameOfPermission.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Name of the permission is empty "));
        }

        if (permissionRepository.existsByPermissionName(nameOfPermission)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Permission already exist! Try to update it "));
        }

        Permission permission = new Permission(nameOfPermission);

        permissionRepository.save(permission);

        return ResponseEntity.ok(new MessageResponse("Permission saved successfully!"));
    }

    @PostMapping("deletePermission")
    public ResponseEntity<?> deletePermission(@RequestBody NewPermissionRequest newPermissionRequest){
        String nameOfPermission = newPermissionRequest.getPermission_name();

        if (nameOfPermission.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Name of the permission is empty "));
        }

        if (!permissionRepository.existsByPermissionName(nameOfPermission)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Permission does not exist! You cannot delete it! "));
        }

        Permission perm = permissionRepository.findByPermissionName(nameOfPermission).orElseThrow(
                ()-> new RuntimeException("Permission does not found with the given name!")
        );

        permissionRepository.delete(perm);

        return ResponseEntity.ok(new MessageResponse("Permission deleted successfully!"));

    }

    @PostMapping("updatePermission")
    public ResponseEntity<?> updatePermission(@RequestBody PermissionUpdateRequest permissionUpdateRequest){
        String nameOfPermission = permissionUpdateRequest.getPermission_name();

        if (nameOfPermission.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Name of the permission is empty "));
        }

        if (!permissionRepository.existsByPermissionName(nameOfPermission)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Permission does not exist! You cannot update it! "));
        }

        Permission perm = permissionRepository.findByPermissionName(nameOfPermission).orElseThrow(
                ()-> new RuntimeException("Permission does not found with the given name!")
        );

        String new_permission_name = permissionUpdateRequest.getNew_permission_name();
        perm.setPermissionName(new_permission_name);
        permissionRepository.save(perm);


        return ResponseEntity.ok(new MessageResponse("Permission updated successfully!"));

    }

    @PostMapping("retrievePermission")
    public ResponseEntity<?> retreivePermission(@RequestBody NewPermissionRequest newPermissionRequest){
        String nameOfPermission = newPermissionRequest.getPermission_name();

        if (nameOfPermission.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Name of the permission is empty "));
        }

        if (!permissionRepository.existsByPermissionName(nameOfPermission)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Permission does not exist! You cannot retrieve it! "));
        }

        Permission perm = permissionRepository.findByPermissionName(nameOfPermission).orElseThrow(
                ()-> new RuntimeException("Permission does not found with the given name!")
        );

        String message = "permission_id: "+perm.getPermissionId()+", permission_name:"+perm.getPermissionName();


        return ResponseEntity.ok(new MessageResponse(message));

    }

}

