package com.aydinmustafa.dynamicauthproject.access;

import com.aydinmustafa.dynamicauthproject.models.User;
import com.aydinmustafa.dynamicauthproject.models.Permission;
import com.aydinmustafa.dynamicauthproject.models.Role;
import com.aydinmustafa.dynamicauthproject.repository.PermissionRepository;
import com.aydinmustafa.dynamicauthproject.repository.RoleRepository;
import com.aydinmustafa.dynamicauthproject.repository.UserRepository;
import com.aydinmustafa.dynamicauthproject.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;




@Component
public class UrlAccessAuthorization {



    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleRepository roleRepository;

    public boolean isAbleToAccess(@org.jetbrains.annotations.NotNull HttpServletRequest request){

        String test = request.getRequestURI();
        String jwtToken = request.getHeader("Authorization").substring(7);//TODO replace with .replace("Bearer ")
        System.out.println("authorization header is ");
        System.out.println(jwtToken);
        String username = jwtUtils.getUserNameFromJwtToken(jwtToken);
        System.out.println("username is --> "+username);
        User currUser = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("No user with username"));
        Set<Permission> permSet = currUser.getPermissions();
        Set<String> stringPermSet = new HashSet<>();

        Set<Role> roleSet = currUser.getRoles();
        for (Role eachRole : roleSet){
            permSet.addAll(eachRole.getPermissions());
        }
        for (Permission eachPerm : permSet){
            stringPermSet.add(eachPerm.getPermissionName());
        }

        System.out.println(" Permission set of the user is :::::::::::");
        System.out.println(stringPermSet);
        System.out.println("requested url is ");
        System.out.println(test);
        for(String eachUrl: stringPermSet){
            System.out.println(eachUrl +" == "+test);
            if(eachUrl.equals(test)){

                return true;
            }
        }


        return false;
    }

}

