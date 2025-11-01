package com.geopokrovskiy.webhook_collector_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(schema = "public", name = "unknown_callbacks")
public class UnknownCallback {

    @Id
    private UUID uid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String body;


}
