package com.dev.remindeck.service;

import com.dev.remindeck.dto.request.DeckRequest;
import com.dev.remindeck.dto.response.DeckResponse;
import com.dev.remindeck.entity.Deck;
import com.dev.remindeck.entity.User;
import com.dev.remindeck.repository.DeckRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeckServiceTest {

    @Mock private DeckRepository deckRepository;
    @Mock private CurrentUserProvider currentUserProvider;

    private static final String DEFAULT_COLOR = "#B8C0FF";

    @InjectMocks DeckService deckService;


    @Test
    void getMyDecks_withNoDecks_returnsEmptyArray() {
        User user = new User();
        user.setId(1L);
        user.setEmail("xyz@unknown.com");
        when(currentUserProvider.getCurrentUser())
                .thenReturn(user);
        assertEquals(1L, user.getId());
        when(deckRepository.findByUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(new ArrayList<>());
        assertEquals(new ArrayList<>(), deckService.getMyDecks());
    }

    @Test
    void getMyDecks_withDecks_returnsDeckList() {
        User user = new User();
        user.setId(1L);

        Deck deck1 = new Deck();
        deck1.setId(10L);
        deck1.setUser(user);
        deck1.setName("Java");
        deck1.setDescription("Core Java");
        deck1.setColor("#B8C0FF");
        deck1.setIcon("icon-java");
        deck1.setCreatedAt(Instant.parse("2026-11-02T10:00:00Z"));
        deck1.setUpdatedAt(Instant.parse("2026-11-03T10:00:00Z"));

        Deck deck2 = new Deck();
        deck2.setId(11L);
        deck2.setUser(user);
        deck2.setName("Spring");
        deck2.setDescription(null);
        deck2.setColor("#CDEAC0");
        deck2.setIcon(null);
        deck2.setCreatedAt(Instant.parse("2026-11-01T09:00:00Z"));
        deck2.setUpdatedAt(Instant.parse("2026-11-01T09:00:00Z"));

        when(currentUserProvider.getCurrentUser()).thenReturn(user);
        when(deckRepository.findByUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(deck1, deck2));

        List<DeckResponse> result = deckService.getMyDecks();

        assertThat(result)
                .extracting(DeckResponse::getId, DeckResponse::getName,
                        DeckResponse::getDescription, DeckResponse::getColor,
                        DeckResponse::getIcon, DeckResponse::getCreatedAt,
                        DeckResponse::getUpdatedAt)
                .containsExactly(
                        tuple(10L, "Java", "Core Java", "#B8C0FF", "icon-java",
                                Instant.parse("2026-11-02T10:00:00Z"),
                                Instant.parse("2026-11-03T10:00:00Z")),
                        tuple(11L, "Spring", null, "#CDEAC0", null,
                                Instant.parse("2026-11-01T09:00:00Z"),
                                Instant.parse("2026-11-01T09:00:00Z")));

        verify(deckRepository).findByUserIdOrderByCreatedAtDesc(1L);
    }
}
