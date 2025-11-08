CREATE TABLE IF NOT EXISTS payment_provider_callbacks (
                                            uid  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                            created_at TIMESTAMP DEFAULT now() NOT NULL,
                                            updated_at TIMESTAMP DEFAULT now() NOT NULL,
                                            body TEXT NOT NULL,
                                            provider_transaction_uid uuid,
                                            type VARCHAR(255),
                                            provider VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS verification_callbacks (
                                        uid  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                        created_at TIMESTAMP DEFAULT now() NOT NULL,
                                        modified_at TIMESTAMP DEFAULT now() NOT NULL,
                                        body TEXT NOT NULL,
                                        transaction_uid uuid,
                                        profile_uid uuid NOT NULL,
                                        status VARCHAR(25),
                                        type VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS unknown_callbacks (
                                   uid  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                   created_at TIMESTAMP DEFAULT now() NOT NULL,
                                   updated_at TIMESTAMP DEFAULT now() NOT NULL,
                                   body TEXT NOT NULL
);