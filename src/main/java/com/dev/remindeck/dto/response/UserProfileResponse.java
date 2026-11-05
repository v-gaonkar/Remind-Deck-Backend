package com.dev.remindeck.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponse {
    private String username;
    private String email;
    private String timezone;
}
