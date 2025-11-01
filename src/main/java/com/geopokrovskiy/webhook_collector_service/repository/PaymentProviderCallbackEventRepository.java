package com.geopokrovskiy.webhook_collector_service.repository;

import com.geopokrovskiy.webhook_collector_service.model.PaymentProviderOutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PaymentProviderCallbackEventRepository extends JpaRepository<PaymentProviderOutboxEvent, UUID> {

    @Query("SELECT event FROM PaymentProviderOutboxEvent event WHERE event.eventStatus = 'CREATED'")
    List<PaymentProviderOutboxEvent> findAllUnprocessEvents();
}
