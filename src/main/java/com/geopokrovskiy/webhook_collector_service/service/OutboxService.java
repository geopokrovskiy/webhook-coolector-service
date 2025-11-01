package com.geopokrovskiy.webhook_collector_service.service;

import com.geopokrovskiy.webhook_collector_service.model.EventStatus;
import com.geopokrovskiy.webhook_collector_service.model.PaymentProviderCallback;
import com.geopokrovskiy.webhook_collector_service.model.PaymentProviderOutboxEvent;
import com.geopokrovskiy.webhook_collector_service.repository.PaymentProviderCallbackEventRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Data
public class OutboxService {
    private final PaymentProviderCallbackEventRepository paymentProviderCallbackEventRepository;

    public PaymentProviderOutboxEvent addNewEventToOutbox(PaymentProviderCallback callback) {
        PaymentProviderOutboxEvent event = new PaymentProviderOutboxEvent();
        event.setType(callback.getType());
        event.setUid(UUID.randomUUID());
        event.setProvider(callback.getProvider());
        event.setProviderTransactionUid(callback.getProviderTransactionUid());
        event.setTransactionStatus(callback.getTransactionStatus());
        event.setPaymentProviderCallbackId(callback.getUid());
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        event.setEventStatus(EventStatus.CREATED);

        log.info("A new PaymentProviderOutboxEvent {} has been successfully created", event);
        return paymentProviderCallbackEventRepository.save(event);
    }

    public List<PaymentProviderOutboxEvent> getUnprocessedEvents() {
        return paymentProviderCallbackEventRepository.findAllUnprocessEvents();
    }

    public PaymentProviderOutboxEvent updateStatus(PaymentProviderOutboxEvent event, EventStatus status) {
        event.setEventStatus(status);
        return paymentProviderCallbackEventRepository.save(event);
    }
}
