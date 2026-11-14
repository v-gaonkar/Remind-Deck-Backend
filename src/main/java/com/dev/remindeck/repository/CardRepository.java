package com.dev.remindeck.repository;

import com.dev.remindeck.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByIdAndDeckUserId(Long id, Long userId);

    @Query("""
            select c from Card c
            where c.deck.id = :deckId
              and lower(c.question) like :pattern
              and (:box = 0 or c.box = :box)
              and (:dueOnly = false or c.nextReviewDate <= :today)
            """)
    Page<Card> search(@Param("deckId") Long deckId,
                      @Param("pattern") String pattern,
                      @Param("box") int box,
                      @Param("dueOnly") boolean dueOnly,
                      @Param("today") LocalDate today,
                      Pageable pageable);
}