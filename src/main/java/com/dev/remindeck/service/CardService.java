package com.dev.remindeck.service;

import com.dev.remindeck.dto.request.CardRequest;
import com.dev.remindeck.dto.response.CardResponse;
import com.dev.remindeck.dto.response.DueStatus;
import com.dev.remindeck.dto.response.PageResponse;
import com.dev.remindeck.entity.Card;
import com.dev.remindeck.entity.Deck;
import com.dev.remindeck.exception.CardNotFoundException;
import com.dev.remindeck.exception.DeckNotFoundException;
import com.dev.remindeck.repository.CardRepository;
import com.dev.remindeck.repository.DeckRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class CardService {

    private static final int MAX_PAGE_SIZE = 50;

    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;
    private final CurrentUserProvider currentUserProvider;
    private final Clock clock;

    public CardService(CardRepository cardRepository,
                       DeckRepository deckRepository,
                       CurrentUserProvider currentUserProvider,
                       Clock clock) {
        this.cardRepository = cardRepository;
        this.deckRepository = deckRepository;
        this.currentUserProvider = currentUserProvider;
        this.clock = clock;
    }

    public PageResponse<CardResponse> getCards(Long deckId, String search, Integer box,
                                               boolean dueOnly, String sort, int page, int size) {
        Long userId = currentUserProvider.getCurrentUser().getId();
        findOwnedDeck(deckId, userId);

        LocalDate today = LocalDate.now(clock);
        String pattern = "%" + (search == null ? "" : search.trim().toLowerCase()) + "%";
        int boxFilter = (box == null) ? 0 : box;
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), MAX_PAGE_SIZE),
                toSort(sort));

        Page<Card> result = cardRepository.search(deckId, pattern, boxFilter, dueOnly, today, pageable);
        return PageResponse.from(result.map(card -> toResponse(card, today)));
    }

    public CardResponse createCard(Long deckId, CardRequest request) {
        Long userId = currentUserProvider.getCurrentUser().getId();
        Deck deck = findOwnedDeck(deckId, userId);

        LocalDate today = LocalDate.now(clock);
        Card card = new Card();
        card.setDeck(deck);
        card.setQuestion(request.getQuestion().trim());
        card.setAnswer(request.getAnswer().trim());
        card.setNotes(cleanNotes(request.getNotes()));
        card.setBox(1);
        card.setNextReviewDate(today);

        Card saved = cardRepository.save(card);
        return toResponse(saved, today);
    }

    public CardResponse updateCard(Long id, CardRequest request) {
        Long userId = currentUserProvider.getCurrentUser().getId();
        Card card = findOwnedCard(id, userId);

        card.setQuestion(request.getQuestion().trim());
        card.setAnswer(request.getAnswer().trim());
        card.setNotes(cleanNotes(request.getNotes()));

        Card saved = cardRepository.save(card);
        return toResponse(saved, LocalDate.now(clock));
    }

    public void deleteCard(Long id) {
        Long userId = currentUserProvider.getCurrentUser().getId();
        Card card = findOwnedCard(id, userId);
        cardRepository.delete(card);
    }

    private Deck findOwnedDeck(Long deckId, Long userId) {
        Deck deck = deckRepository.findByIdAndUserId(deckId, userId);
        if (deck == null) {
            throw new DeckNotFoundException("Deck with given id not found");
        }
        return deck;
    }

    private Card findOwnedCard(Long id, Long userId) {
        Card card = cardRepository.findByIdAndDeckUserId(id, userId);
        if (card == null) {
            throw new CardNotFoundException("Card with given id not found");
        }
        return card;
    }

    private String cleanNotes(String notes) {
        if (notes == null || notes.isBlank()) {
            return null;
        }
        return notes.trim();
    }

    private Sort toSort(String sort) {
        String key = (sort == null) ? "" : sort;
        return switch (key) {
            case "oldest" -> Sort.by(Sort.Order.asc("createdAt"), Sort.Order.asc("id"));
            case "nextReview" -> Sort.by(Sort.Order.asc("nextReviewDate"), Sort.Order.asc("id"));
            default -> Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id"));
        };
    }

    private CardResponse toResponse(Card card, LocalDate today) {
        return new CardResponse(
                card.getId(),
                card.getDeck().getId(),
                card.getQuestion(),
                card.getAnswer(),
                card.getNotes(),
                card.getBox(),
                card.getNextReviewDate(),
                dueStatusOf(card.getNextReviewDate(), today),
                card.getCreatedAt());
    }

    private DueStatus dueStatusOf(LocalDate dueDate, LocalDate today) {
        if (dueDate.isBefore(today)) {
            return DueStatus.OVERDUE;
        }
        if (dueDate.isEqual(today)) {
            return DueStatus.DUE_TODAY;
        }
        return DueStatus.UPCOMING;
    }
}