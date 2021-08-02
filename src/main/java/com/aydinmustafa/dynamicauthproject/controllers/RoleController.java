package com.aydinmustafa.dynamicauthproject.controllers;



import com.aydinmustafa.dynamicauthproject.payload.request.NewRoleRequest;
import com.aydinmustafa.dynamicauthproject.payload.request.UpdateRoleRequest;
import com.aydinmustafa.dynamicauthproject.payload.response.MessageResponse;
import com.aydinmustafa.dynamicauthproject.repository.*;
import com.aydinmustafa.dynamicauthproject.models.*;
import com.aydinmustafa.dynamicauthproject.payload.*;
import com.aydinmustafa.dynamicauthproject.controllers.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/addRole")
    public ResponseEntity<?> addNewRole(@RequestBody NewRoleRequest newRoleRequest) {
        System.out.println(newRoleRequest.getRole_name());

        String nameOfRole = newRoleRequest.getRole_name();
        if (nameOfRole.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Name of the role is empty "));
        }

        if (roleRepository.existsByName(nameOfRole)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Role already exist! Try to update it "));
        }
        System.out.println("Entered into adding new role function and the role name is---> ");
        System.out.println(newRoleRequest.getRole_name());

        //create a new role with the name given
        Role role = new Role(nameOfRole);

        Set<String> stringPermissions = newRoleRequest.getPermissions();
        Set<Permission> permissions= new HashSet<>();

        if (stringPermissions.isEmpty()){
            permissions = null;
        }

        for(String eachPermission:stringPermissions ){
            System.out.println("permission name is "+ eachPermission);
            Permission perm = permissionRepository.findByPermissionName(eachPermission)
                    .orElseThrow(() -> new RuntimeException("Error permission not found error in RoleController.java ;)"));
            permissions.add(perm);
        }
        role.setPermissions(permissions);
        roleRepository.save(role);

        return ResponseEntity.ok(new MessageResponse("Role saved successfully!"));
    }

    @PostMapping("/deleteRole")
    public ResponseEntity<?> deleteRole(@RequestBody NewRoleRequest newRoleRequest){
        String nameOfRole = newRoleRequest.getRole_name();

        if (nameOfRole.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Name of the role is empty "));
        }

        if (!roleRepository.existsByName(nameOfRole)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Role does not exist! You cannot delete it! "));
        }

        Role role = roleRepository.findByName(nameOfRole).orElseThrow(
                ()-> new RuntimeException("Role does not found with the given name!")
        );

        System.out.println("entity we try to delete is "+role.getName());
        deleteRoleFromAllUsers(role);
        roleRepository.delete(role);

        return ResponseEntity.ok(new MessageResponse("Role deleted successfully!"));

    }

    @PostMapping("/updateRole")
    public ResponseEntity<?> updateRole(@RequestBody UpdateRoleRequest updateRoleRequest){
        String nameOfRole = updateRoleRequest.getRole_name();
        System.out.println("role to be updated found! "+nameOfRole);
        if (nameOfRole.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Name of the role is empty "));
        }

        if (!roleRepository.existsByName(nameOfRole)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Role does not exist! You cannot update it! "));
        }

        Role role = roleRepository.findByName(nameOfRole).orElseThrow(
                ()-> new RuntimeException("Role to update does not found with the given name!")
        );

        Set<String> permissionSet = updateRoleRequest.getNew_permission_set();

        String new_name = updateRoleRequest.getNew_role_name();
        if (new_name.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: New name of the role is empty "));
        }
        Set<Permission> permissions= new HashSet<>();

        if (permissionSet.isEmpty()){
            permissions = null;
        }
        for(String eachPermission:permissionSet ){
            System.out.println("permission name is "+ eachPermission);
            Permission perm = permissionRepository.findByPermissionName(eachPermission)
                    .orElseThrow(() -> new RuntimeException("Error permission not found error in RoleController.java ;)"));
            permissions.add(perm);
        }

        role.setPermissions(permissions);
        role.setName(new_name);
        roleRepository.save(role);
        return ResponseEntity.ok(new MessageResponse("Role updated successfully!"));

    }

    @PostMapping("/retrieveRole")
    public ResponseEntity<?> retrieveRole(@RequestBody NewRoleRequest newRoleRequest){
        String nameOfRole = newRoleRequest.getRole_name();

        if (nameOfRole.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Name of the role is empty "));
        }

        if (!roleRepository.existsByName(nameOfRole)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Role does not exist! You cannot delete it! "));
        }

        Role role = roleRepository.findByName(nameOfRole).orElseThrow(
                ()-> new RuntimeException("Role does not found with the given name!")
        );

        Set<String> stringPermissionSet = new HashSet<>();
        Set<Permission> permissionSet = role.getPermissions();

        for( Permission each : permissionSet){
            System.out.println(each.getPermissionName());
            stringPermissionSet.add(each.getPermissionName());
        }

        String msg = "role_id: "+role.getId()+", role_name: "+role.getName()+", role_permissions:"+stringPermissionSet;

        return ResponseEntity.ok(new MessageResponse(msg));

    }

    public void deleteRoleFromAllUsers(Role role){
        List<User> allUsers = userRepository.findAll();
        for (User u: allUsers){
            Set<Role> setRole = u.getRoles();
            for(Role r: setRole){
                if(r.equals(role)){
                    setRole.remove(r);
                    System.out.println("role removed with the name"+r.getName());
                }
            }
            u.setRoles(setRole);
            userRepository.save(u);
        }

    }
}

