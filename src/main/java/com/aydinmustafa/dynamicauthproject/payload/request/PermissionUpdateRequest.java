package com.aydinmustafa.dynamicauthproject.payload.request;

import javax.validation.constraints.NotBlank;

public class PermissionUpdateRequest {

    @NotBlank
    private String permission_name;

    @NotBlank
    private String new_permission_name;

    public String getPermission_name() {
        return permission_name;
    }

    public void setPermission_name(String permission_name) {
        this.permission_name = permission_name;
    }

    public String getNew_permission_name() {
        return new_permission_name;
    }

    public void setNew_permission_name(String new_permission_name) {
        this.new_permission_name = new_permission_name;
    }
}
