package com.dev.remindeck.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeckRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max=300)
    private String description;

    @Size(max = 10)
    private String color;

    @Size(max = 16)
    private String icon;

}
