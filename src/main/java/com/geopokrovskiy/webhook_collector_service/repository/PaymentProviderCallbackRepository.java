package com.geopokrovskiy.webhook_collector_service.repository;

import com.geopokrovskiy.webhook_collector_service.model.PaymentProviderCallback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentProviderCallbackRepository extends JpaRepository<PaymentProviderCallback, UUID> {
}
