package com.dev.remindeck.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeckResponse {
    private Long id;
    private String name;
    private String description;
    private String color;
    private String icon;
    private Instant createdAt;
    private Instant updatedAt;

//    public DeckResponse(Long id, String name, String description, String color, String icon, Instant createdAt) {
//    }
}
