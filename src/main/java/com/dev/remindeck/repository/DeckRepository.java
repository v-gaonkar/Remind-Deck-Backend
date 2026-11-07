package com.dev.remindeck.repository;

import com.dev.remindeck.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndName(Long id, String name);

    Deck findByIdAndUserId(Long id, Long userId);

    boolean existsByUserIdAndNameAndIdNot(Long id, String name, Long id1);
}
