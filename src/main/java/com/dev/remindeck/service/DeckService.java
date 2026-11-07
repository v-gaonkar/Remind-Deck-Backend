package com.dev.remindeck.service;

import com.dev.remindeck.dto.request.DeckRequest;
import com.dev.remindeck.dto.response.DeckResponse;
import com.dev.remindeck.entity.Deck;
import com.dev.remindeck.entity.User;
import com.dev.remindeck.exception.DeckAlreadyExistsException;
import com.dev.remindeck.exception.DeckNotFoundException;
import com.dev.remindeck.repository.DeckRepository;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {

    private final DeckRepository deckRepository;
    private final CurrentUserProvider currentUserProvider;

    private static final String DEFAULT_COLOR = "#B8C0FF";

    public DeckService(DeckRepository deckRepository,
                       CurrentUserProvider currentUserProvider) {
        this.deckRepository = deckRepository;
        this.currentUserProvider = currentUserProvider;
    }


    public List<DeckResponse> getMyDecks() {
        Long userId = currentUserProvider.getCurrentUser().getId();
        return deckRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DeckResponse getMyDeckById(@Positive Long id) {
        Long userId = currentUserProvider.getCurrentUser().getId();
        Deck deck = deckRepository.findByIdAndUserId(id, userId);
        if(deck != null) {
            return toResponse(deck);
        } else {
            throw new DeckNotFoundException("Deck with given id not found");
        }
    }

    public DeckResponse createDeck(DeckRequest request) {
        User user = currentUserProvider.getCurrentUser();
        String name = request.getName().trim();
        if(deckRepository.existsByUserIdAndName(user.getId(), name)) {
            throw new DeckAlreadyExistsException("Deck with name " + name + " already exists");
        }

        Deck deck = new Deck();

        deck.setName(name);
        deck.setUser(user);
        deck.setDescription(request.getDescription());
        if(request.getColor() != null &&  !request.getColor().isBlank()) {
            deck.setColor(request.getColor().toUpperCase());
        } else {
            deck.setColor(DEFAULT_COLOR);
        }
        deck.setIcon(request.getIcon());

        Deck saved = deckRepository.save(deck);
        return toResponse(saved);

    }

    public DeckResponse updateDeck(DeckRequest request, Long id){
        User user = currentUserProvider.getCurrentUser();
        String name = request.getName().trim();
        Deck deck = deckRepository.findByIdAndUserId(id, user.getId());
        if(deck != null) {
            if(deckRepository.existsByUserIdAndNameAndIdNot(user.getId(), name, id)) {
                throw new DeckAlreadyExistsException("Deck with name " + name + " already exists");
            }
            deck.setName(name);
            deck.setDescription(request.getDescription());
            if(request.getColor() != null &&  !request.getColor().isBlank()) {
                deck.setColor(request.getColor().toUpperCase());
            }
            deck.setIcon(request.getIcon());

            Deck saved = deckRepository.save(deck);
            return toResponse(saved);
        } else {
            throw new DeckNotFoundException("Deck with given id not found");
        }
    }

    public void deleteDeck(Long id) {
        User user = currentUserProvider.getCurrentUser();
        Deck deck = deckRepository.findByIdAndUserId(id, user.getId());
        if(deck != null) {
           deckRepository.delete(deck);
        } else {
            throw new DeckNotFoundException("Deck with given id not found");
        }
    }

    private DeckResponse toResponse(Deck deck) {
        return new DeckResponse(
                deck.getId(),
                deck.getName(),
                deck.getDescription(),
                deck.getColor(),
                deck.getIcon(),
                deck.getCreatedAt(),
                deck.getUpdatedAt());
    }


}
