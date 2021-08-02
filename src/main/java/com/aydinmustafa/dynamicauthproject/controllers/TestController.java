package com.aydinmustafa.dynamicauthproject.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.aydinmustafa.dynamicauthproject.access.UrlAccessAuthorization;
import com.aydinmustafa.dynamicauthproject.security.jwt.JwtUtils;
import com.aydinmustafa.dynamicauthproject.models.*;
import com.aydinmustafa.dynamicauthproject.repository.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    UrlAccessAuthorization urlAuthorization;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }


    @GetMapping("/create")
    @PreAuthorize("hasAuthority('ACCOUNT_CREATE')")
    public String createAccess() {
        return "This user has the permission to create a account.";
    }


    /**
     * auth.principal.username=#username makes sure that each user is only able to access their personal pages and
     * not the pages of the other users on the other hand admins can access everyone's page
     * @param username username of the user currently accessing the page
     * @return personal page of the user
     */
    @GetMapping(path = "{username}")
    @PreAuthorize("authentication.principal.username == #username || hasRole('ADMIN')")
    public String getUser(@PathVariable("username") String username) {
        // TODO Implement the functionality so that only the logged in user can see his/her page
        System.out.println("here in test controller we try to print username from userRepository object");


        User searchedCurrentlyUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User is not found. Error in TestController.java :)"));
        Set<Permission> permSet = searchedCurrentlyUser.getPermissions();
        Set<String> stringPermSet = new HashSet<>();
        Set<Role> roleSet = searchedCurrentlyUser.getRoles();
        Set<String> stringRoleSet = new HashSet<>();
        for (Role eachRole : roleSet){
            stringRoleSet.add(eachRole.getName());
            System.out.println("roles permissions --> "+ eachRole.getPermissions());
            permSet.addAll(eachRole.getPermissions());
        }
        for (Permission eachPerm : permSet){
            System.out.println("permission name is ----------->"+eachPerm.getPermissionName());
            stringPermSet.add(eachPerm.getPermissionName());
        }
        String s = searchedCurrentlyUser.getUsername()+" \n email : "+searchedCurrentlyUser.getEmail()+
                "\n with the id : "+searchedCurrentlyUser.getId()+"\n roles : "+stringRoleSet+"\n authorities : "+stringPermSet;
        return s;
    }

    @GetMapping("/url")
    public String getURLValue(HttpServletRequest request){
        String test = request.getRequestURI();
        String jwtToken = request.getHeader("Authorization").substring(7);
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
                return test;
            }
        }


        return "Unauthorized for "+test;
    }



    @GetMapping("/url2")
    public String getURLValue2(HttpServletRequest request){
        boolean permissible = urlAuthorization.isAbleToAccess(request);
        if (permissible){
            return "Can access to url2 ";
        }else{
            return "CAnnot access to url2 ";
        }
    }

    @GetMapping("/url1")
    public String getURLValue1(HttpServletRequest request){
        boolean permissible = urlAuthorization.isAbleToAccess(request);
        if (permissible){
            return "Can access to url1 ";
        }else{
            return "CAnnot access to url1 ";
        }
    }
    @GetMapping("/url3")
    public String getURLValue3(HttpServletRequest request){
        boolean permissible = urlAuthorization.isAbleToAccess(request);
        if (permissible){
            return "Can access to url3";
        }else{
            return "CAnnot access to url3";
        }
    }
    @GetMapping("/url4")
    public String getURLValue4(HttpServletRequest request){
        boolean permissible = urlAuthorization.isAbleToAccess(request);
        if (permissible){
            return "Can access to url4 ";
        }else{
            return "CAnnot access to url4";
        }
    }
    @GetMapping("/url5")
    public String getURLValue5(HttpServletRequest request){
        boolean permissible = urlAuthorization.isAbleToAccess(request);
        if (permissible){
            return "Can access to url5 ";
        }else{
            return "CAnnot access to url5 ";
        }
    }
    @GetMapping("/url6")
    public String getURLValue6(HttpServletRequest request){
        boolean permissible = urlAuthorization.isAbleToAccess(request);
        if (permissible){
            return "Can access to url6 ";
        }else{
            return "CAnnot access to url6 ";
        }
    }
    @GetMapping("/url7")
    public String getURLValue7(HttpServletRequest request){
        boolean permissible = urlAuthorization.isAbleToAccess(request);
        if (permissible){
            return "Can access to url7";
        }else{
            return "CAnnot access to url7";
        }
    }
    @GetMapping("/url8")
    public String getURLValue8(HttpServletRequest request){
        boolean permissible = urlAuthorization.isAbleToAccess(request);
        if (permissible){
            return "Can access to url8";
        }else{
            return "CAnnot access to url8 ";
        }
    }
    @GetMapping("/url9")
    public String getURLValue9(HttpServletRequest request){
        boolean permissible = urlAuthorization.isAbleToAccess(request);
        if (permissible){
            return "Can access to url9";
        }else{
            return "CAnnot access to url9";
        }
    }
    @GetMapping("/url10")
    public String getURLValue10(HttpServletRequest request){
        boolean permissible = urlAuthorization.isAbleToAccess(request);
        if (permissible){
            return "Can access to url10";
        }else{
            return "CAnnot access to url10";
        }
    }@GetMapping("/url11")
    public String getURLValue11(HttpServletRequest request){
        boolean permissible = urlAuthorization.isAbleToAccess(request);
        if (permissible){
            return "Can access to url11";
        }else{
            return "CAnnot access to url11";
        }
    }


}

