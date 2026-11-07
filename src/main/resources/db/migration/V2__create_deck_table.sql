CREATE TABLE decks (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    color VARCHAR(10) NOT NULL DEFAULT '#B8C0FF',
    icon VARCHAR(16),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

        CONSTRAINT uq_decks_user_name UNIQUE (user_id, name),
        CONSTRAINT chk_decks_color_hex CHECK (color ~ '^#[0-9A-F]{6}$'),
        CONSTRAINT fk_decks_user_id
            FOREIGN KEY (user_id)
                  REFERENCES users(id) ON DELETE CASCADE

);