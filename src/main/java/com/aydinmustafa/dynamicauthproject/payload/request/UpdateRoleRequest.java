package com.aydinmustafa.dynamicauthproject.payload.request;



import javax.validation.constraints.NotBlank;
import java.util.Set;

public class UpdateRoleRequest {

    @NotBlank
    private String role_name;

    @NotBlank
    private String new_role_name;

    private Set<String> new_permission_set;

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getNew_role_name() {
        return new_role_name;
    }

    public void setNew_role_name(String new_role_name) {
        this.new_role_name = new_role_name;
    }

    public Set<String> getNew_permission_set() {
        return new_permission_set;
    }

    public void setNew_permission_set(Set<String> new_permission_set) {
        this.new_permission_set = new_permission_set;
    }
}

