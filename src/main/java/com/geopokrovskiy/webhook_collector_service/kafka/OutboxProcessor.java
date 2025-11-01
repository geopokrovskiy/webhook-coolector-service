package com.geopokrovskiy.webhook_collector_service.kafka;

import com.geopokrovskiy.webhook_collector_service.model.EventStatus;
import com.geopokrovskiy.webhook_collector_service.model.PaymentProviderOutboxEvent;
import com.geopokrovskiy.webhook_collector_service.service.OutboxService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@EnableScheduling
@Slf4j
@Data
public class OutboxProcessor {
    private final OutboxService outboxService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${outbox-processor.kafka-topic}")
    private String kafkaTopic;

    @Scheduled(fixedRateString = "${outbox-processor.delay}")
    public void sendToKafka() {
        List<PaymentProviderOutboxEvent> unprocessedEvents = outboxService.getUnprocessedEvents();
        for (PaymentProviderOutboxEvent event : unprocessedEvents) {
            CompletableFuture<SendResult<String, String>> sendResultFuture = kafkaTemplate.send(kafkaTopic, event.toString());

            sendResultFuture.whenComplete((result, exception) -> {
                if (exception == null) {
                    RecordMetadata meta = result.getRecordMetadata();
                    log.info("Sent event {} to Kafka (partition={}, offset={})",
                            event, meta.partition(), meta.offset());

                    outboxService.updateStatus(event, EventStatus.PROCESSED);
                } else {
                    log.error("Error occurred during sending of the event {} to kafka. The message is {} ", event, exception.getMessage());
                }
            });
        }
    }
}
