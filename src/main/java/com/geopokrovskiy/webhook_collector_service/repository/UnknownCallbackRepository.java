package com.geopokrovskiy.webhook_collector_service.repository;

import com.geopokrovskiy.webhook_collector_service.model.UnknownCallback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnknownCallbackRepository extends JpaRepository<UnknownCallback, UUID> {
}
