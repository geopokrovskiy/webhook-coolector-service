package com.geopokrovskiy.webhook_collector_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(schema = "public", name = "payment_provider_callbacks_outbox")
public class PaymentProviderOutboxEvent {
    @Id
    private UUID uid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID providerTransactionUid;
    @Column(name = "payment_provider_callback_uid")
    private UUID paymentProviderCallbackId;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
    private String provider;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
}
