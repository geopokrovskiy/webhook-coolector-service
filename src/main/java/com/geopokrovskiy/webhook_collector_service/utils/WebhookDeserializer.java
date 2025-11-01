package com.geopokrovskiy.webhook_collector_service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geopokrovskiy.dto.webhook_service.create.CreateCallbackDto;
import com.geopokrovskiy.webhook_collector_service.model.PaymentProviderCallback;
import com.geopokrovskiy.webhook_collector_service.model.TransactionStatus;
import com.geopokrovskiy.webhook_collector_service.model.TransactionType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * This deserializer is necessary since we need to extract the request body. Therefore,
 * it's impossible to use mapstruct for that purpose.
 */
@Data
@Slf4j
@Component
public class WebhookDeserializer {

    private final ObjectMapper objectMapper;

    public PaymentProviderCallback mapDtoToCallback(CreateCallbackDto createCallbackDto) throws IOException {
        try {
            PaymentProviderCallback paymentProviderCallback = new PaymentProviderCallback();
            paymentProviderCallback.setType(TransactionType.valueOf(createCallbackDto.getType()));
            paymentProviderCallback.setTransactionStatus(TransactionStatus.valueOf(createCallbackDto.getTransactionStatus()));
            paymentProviderCallback.setProvider(createCallbackDto.getProvider());
            paymentProviderCallback.setProviderTransactionUid(UUID.fromString(createCallbackDto.getProviderTransactionUid()));
            paymentProviderCallback.setBody(objectMapper.writeValueAsString(createCallbackDto));
            paymentProviderCallback.setCreatedAt(LocalDateTime.now());
            paymentProviderCallback.setUpdatedAt(LocalDateTime.now());
            paymentProviderCallback.setUid(UUID.randomUUID());

            return paymentProviderCallback;
        } catch (JsonProcessingException exception) {
            log.error("Exception during json serialization: " + exception.getMessage());
            throw new IOException(exception.getMessage());
        }
    }
}
