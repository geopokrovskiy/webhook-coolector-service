CREATE TABLE IF NOT EXISTS payment_provider_callbacks_outbox
(
    uid                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at               TIMESTAMP        DEFAULT now() NOT NULL,
    updated_at               TIMESTAMP        DEFAULT now() NOT NULL,
    payment_provider_callback_uid UUID REFERENCES payment_provider_callbacks(uid) on delete cascade,
    provider_transaction_uid uuid,
    type                     VARCHAR(255),
    provider                 VARCHAR(255),
    transaction_status       varchar(32),
    event_status varchar(32)
)