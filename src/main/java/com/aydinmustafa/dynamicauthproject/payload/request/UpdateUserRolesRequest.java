package com.aydinmustafa.dynamicauthproject.payload.request;



import javax.validation.constraints.NotBlank;
import java.util.Set;

public class UpdateUserRolesRequest {

    @NotBlank
    private String username;

    private Set<String> role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}

