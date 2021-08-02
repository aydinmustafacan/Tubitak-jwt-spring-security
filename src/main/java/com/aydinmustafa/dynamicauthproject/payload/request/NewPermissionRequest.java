package com.aydinmustafa.dynamicauthproject.payload.request;



import javax.validation.constraints.NotBlank;

public class NewPermissionRequest {

    @NotBlank
    private String permission_name;

    public String getPermission_name() {
        return permission_name;
    }

    public void setPermission_name(String permission_name) {
        this.permission_name = permission_name;
    }
}

