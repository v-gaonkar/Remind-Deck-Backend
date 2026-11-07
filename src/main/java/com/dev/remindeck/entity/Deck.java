package com.dev.remindeck.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "decks",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "name",
                        "user_id"
                })
        }
)
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String description;

    @Column(nullable = false, check = @CheckConstraint( name = "chk_decks_color_hex",
            constraint = "color ~ '^#[0-9A-F]{6}$'"), length = 10)
    private String color;

    @Column(length = 16)
    private String icon;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

}
