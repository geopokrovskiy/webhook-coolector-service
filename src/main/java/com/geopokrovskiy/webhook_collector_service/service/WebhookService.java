package com.geopokrovskiy.webhook_collector_service.service;

import com.geopokrovskiy.webhook_collector_service.model.PaymentProviderCallback;
import com.geopokrovskiy.webhook_collector_service.model.UnknownCallback;
import com.geopokrovskiy.webhook_collector_service.repository.PaymentProviderCallbackRepository;
import com.geopokrovskiy.webhook_collector_service.repository.UnknownCallbackRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Data
@Slf4j
public class WebhookService {
    private final PaymentProviderCallbackRepository paymentProviderCallbackRepository;
    private final UnknownCallbackRepository unknownCallbackRepository;
    private final OutboxService outboxService;

    public PaymentProviderCallback processPaymentProviderCallback(PaymentProviderCallback paymentProviderCallback) {
        PaymentProviderCallback savedCallback = paymentProviderCallbackRepository.save(paymentProviderCallback);
        log.info("A new PaymentProviderCallback {} has been successfully saved", paymentProviderCallback);
        outboxService.addNewEventToOutbox(savedCallback);
        return savedCallback;
    }

    public UnknownCallback processIncorrectCallback(PaymentProviderCallback paymentProviderCallback) {
        UnknownCallback unknownCallback = new UnknownCallback();
        unknownCallback.setBody(paymentProviderCallback.getBody());
        unknownCallback.setUid(UUID.randomUUID());
        unknownCallback.setCreatedAt(LocalDateTime.now());
        unknownCallback.setUpdatedAt(LocalDateTime.now());

        log.warn("A new UnknownCallback {} has been successfully saved", unknownCallback);
        UnknownCallback savedCallback = unknownCallbackRepository.save(unknownCallback);
        return savedCallback;
    }

    public UnknownCallback createUnknownCallback(String body) {
        UnknownCallback unknownCallback = new UnknownCallback();
        unknownCallback.setBody(body);
        unknownCallback.setUid(UUID.randomUUID());
        unknownCallback.setCreatedAt(LocalDateTime.now());
        unknownCallback.setUpdatedAt(LocalDateTime.now());

        log.warn("A new UnknownCallback {} has been successfully saved", unknownCallback);
        UnknownCallback savedCallback = unknownCallbackRepository.save(unknownCallback);
        return savedCallback;
    }


}
