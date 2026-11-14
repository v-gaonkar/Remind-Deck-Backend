CREATE TABLE cards (
                       id BIGSERIAL PRIMARY KEY,
                       deck_id BIGINT NOT NULL,
                       question VARCHAR(200) NOT NULL,
                       answer VARCHAR(400) NOT NULL,
                       notes VARCHAR(1000),
                       box INTEGER NOT NULL DEFAULT 1,
                       next_review_date DATE NOT NULL,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT chk_cards_box_range CHECK (box BETWEEN 1 AND 5),
                       CONSTRAINT fk_cards_deck_id
                           FOREIGN KEY (deck_id)
                               REFERENCES decks(id) ON DELETE CASCADE
);

CREATE INDEX idx_cards_deck_due ON cards (deck_id, next_review_date);
CREATE INDEX idx_cards_deck_box ON cards (deck_id, box);