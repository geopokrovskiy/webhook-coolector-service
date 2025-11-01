package com.geopokrovskiy.webhook_collector_service.mapper;

import com.geopokrovskiy.dto.webhook_service.response.CallbackResponseDto;
import com.geopokrovskiy.webhook_collector_service.model.PaymentProviderCallback;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WebhookMapper {
    public CallbackResponseDto map(PaymentProviderCallback paymentProviderCallback);
}
