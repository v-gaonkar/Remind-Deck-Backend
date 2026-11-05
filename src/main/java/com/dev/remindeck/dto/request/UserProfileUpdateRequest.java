package com.dev.remindeck.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateRequest {
    private String username;
    private String email;
    private String timezone;
}
