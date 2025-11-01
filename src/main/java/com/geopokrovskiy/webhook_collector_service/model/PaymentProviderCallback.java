package com.geopokrovskiy.webhook_collector_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(schema = "public", name = "payment_provider_callbacks")
public class PaymentProviderCallback {
    @Id
    private UUID uid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String body;
    private UUID providerTransactionUid;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private String provider;
}
