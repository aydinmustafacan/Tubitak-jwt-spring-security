package com.aydinmustafa.dynamicauthproject.payload.request;

import javax.validation.constraints.NotBlank;

public class UserInformationRequest {

    @NotBlank
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
