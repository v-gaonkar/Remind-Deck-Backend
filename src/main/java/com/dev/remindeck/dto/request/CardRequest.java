package com.dev.remindeck.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequest {

    @NotBlank
    @Size(max = 200)
    private String question;

    @NotBlank
    @Size(max = 400)
    private String answer;

    @Size(max = 1000)
    private String notes;
}