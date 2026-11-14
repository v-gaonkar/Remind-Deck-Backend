package com.dev.remindeck.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CardResponse {
    private Long id;
    private Long deckId;
    private String question;
    private String answer;
    private String notes;
    private Integer box;
    private LocalDate nextReviewDate;
    private DueStatus dueStatus;
    private Instant createdAt;
}