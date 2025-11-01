package com.geopokrovskiy.webhook_collector_service.controller;

import com.geopokrovskiy.dto.webhook_service.create.CreateCallbackDto;
import com.geopokrovskiy.dto.webhook_service.response.CallbackResponseDto;
import com.geopokrovskiy.webhook_collector_service.mapper.WebhookMapper;
import com.geopokrovskiy.webhook_collector_service.model.PaymentProviderCallback;
import com.geopokrovskiy.webhook_collector_service.security.WebhookSecurity;
import com.geopokrovskiy.webhook_collector_service.service.WebhookService;
import com.geopokrovskiy.webhook_collector_service.utils.WebhookDeserializer;
import lombok.Data;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@RequestMapping("/api/v1/webhooks/payment-provider")
public class WebhookController {

    private final WebhookDeserializer webhookDeserializer;
    private final WebhookMapper webhookMapper;
    private final WebhookService webhookService;

    @PostMapping("/fake-provider")
    public ResponseEntity<?> createCallback(@RequestBody CreateCallbackDto createCallbackDto) throws Exception {
        PaymentProviderCallback paymentProviderCallback = webhookDeserializer.mapDtoToCallback(createCallbackDto);
        PaymentProviderCallback savedCallback = webhookService.processPaymentProviderCallback(paymentProviderCallback);
        CallbackResponseDto callbackResponseDto = webhookMapper.map(savedCallback);
        return new ResponseEntity<>(callbackResponseDto, HttpStatusCode.valueOf(201));
    }
}
