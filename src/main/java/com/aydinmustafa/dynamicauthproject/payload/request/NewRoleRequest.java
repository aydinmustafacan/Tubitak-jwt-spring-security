package com.aydinmustafa.dynamicauthproject.payload.request;


import com.aydinmustafa.dynamicauthproject.models.Permission;
import java.util.Set;
import javax.validation.constraints.*;

public class NewRoleRequest {
    @NotBlank
    private String role_name;


    private Set<String> permissions;


    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
