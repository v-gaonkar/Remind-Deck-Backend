package com.dev.remindeck.controller;

import com.dev.remindeck.dto.request.DeckRequest;
import com.dev.remindeck.dto.response.DeckResponse;
import com.dev.remindeck.service.DeckService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decks")
public class DeckController {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping
    public ResponseEntity<List<DeckResponse>> getMyDecks() {
        return ResponseEntity.ok(deckService.getMyDecks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeckResponse> getDeckById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(deckService.getMyDeckById(id));
    }

    @PostMapping
    public ResponseEntity<DeckResponse> createDeck(@Valid @RequestBody DeckRequest request) {
        return new ResponseEntity<>(deckService.createDeck(request), HttpStatus.CREATED );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeckResponse> updateDeck(@Valid @RequestBody DeckRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(deckService.updateDeck(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeck(@PathVariable Long id) {
        deckService.deleteDeck(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
