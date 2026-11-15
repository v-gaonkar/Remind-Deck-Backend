package com.dev.remindeck.controller;

import com.dev.remindeck.dto.request.CardRequest;
import com.dev.remindeck.dto.response.CardResponse;
import com.dev.remindeck.dto.response.PageResponse;
import com.dev.remindeck.service.CardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/decks/{deckId}/cards")
    public ResponseEntity<PageResponse<CardResponse>> getCards(
            @PathVariable @Positive Long deckId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @Min(1) @Max(5) Integer box,
            @RequestParam(defaultValue = "false") boolean dueOnly,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return ResponseEntity.ok(cardService.getCards(deckId, search, box, dueOnly, sort, page, size));
    }

    @PostMapping("/decks/{deckId}/cards")
    public ResponseEntity<CardResponse> createCard(@PathVariable @Positive Long deckId,
                                                   @Valid @RequestBody CardRequest request) {
        return new ResponseEntity<>(cardService.createCard(deckId, request), HttpStatus.CREATED);
    }

    @PutMapping("/cards/{id}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable @Positive Long id,
                                                   @Valid @RequestBody CardRequest request) {
        return ResponseEntity.ok(cardService.updateCard(id, request));
    }

    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable @Positive Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}