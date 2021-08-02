package com.aydinmustafa.dynamicauthproject.controllers;



import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import com.aydinmustafa.dynamicauthproject.models.*;
import com.aydinmustafa.dynamicauthproject.payload.request.*;
import com.aydinmustafa.dynamicauthproject.repository.*;
import com.aydinmustafa.dynamicauthproject.payload.response.*;
import com.aydinmustafa.dynamicauthproject.security.*;
import com.aydinmustafa.dynamicauthproject.security.jwt.JwtUtils;
import com.aydinmustafa.dynamicauthproject.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println("GetAuthorities method returns -->> ");
        System.out.println(userDetails.getAuthorities());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {


        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<String> stringPermissions = signUpRequest.getPermission();
        // set for the roles of the user
        Set<Role> roles = new HashSet<>();
        Set<Permission> permissions= new HashSet<>();

//		if (strRoles == null) {
//			Role userRole = roleRepository.findByName("ROLE_USER")
//					.orElseThrow(() -> new RuntimeException("Error: Role is not found. Error in AuthController.java :)"));
//			roles.add(userRole);
//		} else {
//			strRoles.forEach(role -> {
//				switch (role) {
//				case "admin":
//					Role adminRole = roleRepository.findByName("ROLE_ADMIN")
//							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//					roles.add(adminRole);
//
//					break;
//				case "mod":
//					Role modRole = roleRepository.findByName("ROLE_MODERATOR")
//							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//					roles.add(modRole);
//					break;
//				default:
//					Role userRole = roleRepository.findByName("ROLE_USER")
//							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//					roles.add(userRole);
//				}
//			});
//		}
        if (strRoles == null){
            permissions = null;
        }
        else{
            for ( String eachRole: strRoles){
                Role rol = roleRepository.findByName(eachRole)
                        .orElseThrow(() -> new RuntimeException("Error role not found in database error in AuthController.java ;)"));
                roles.add(rol);
            }
        }



        if (stringPermissions == null){
            permissions = null;
        }
        else{
            for ( String eachPermission: stringPermissions){
                Permission perm = permissionRepository.findByPermissionName(eachPermission)
                        .orElseThrow(() -> new RuntimeException("Error permission not found in database error in AuthController.java ;)"));
                permissions.add(perm);
            }
        }



        user.setRoles(roles);
        user.setPermissions(permissions);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }



}

