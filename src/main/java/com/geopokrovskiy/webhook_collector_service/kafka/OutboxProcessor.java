package com.geopokrovskiy.webhook_collector_service.kafka;

import com.geopokrovskiy.webhook_collector_service.model.EventStatus;
import com.geopokrovskiy.webhook_collector_service.model.PaymentProviderOutboxEvent;
import com.geopokrovskiy.webhook_collector_service.service.OutboxService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
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

    private final KafkaTemplate<String, GenericRecord> kafkaTemplate;

    private static final Schema OUTBOX_EVENT_SCHEMA = new Schema.Parser().parse("""
            {
              "type": "record",
              "name": "OutboxEvent",
              "namespace": "com.geopokrovskiy.avro",
              "fields": [
                {
                  "name": "uid",
                  "type": "string"
                },
                {
                  "name": "provider_transaction_uid",
                  "type": "string"
                },
                {
                  "name": "transaction_type",
                  "type": "string"
                },
                {
                  "name": "transaction_status",
                  "type": "string"
                }
              ]
            }
            """);

    @Value("${outbox-processor.kafka-topic}")
    private String kafkaTopic;

    @Scheduled(fixedRateString = "${outbox-processor.delay}")
    public void sendToKafka() {
        List<PaymentProviderOutboxEvent> unprocessedEvents = outboxService.getUnprocessedEvents();
        for (PaymentProviderOutboxEvent event : unprocessedEvents) {
            GenericRecord record = new GenericData.Record(OUTBOX_EVENT_SCHEMA);
            record.put("uid", event.getUid().toString());
            record.put("provider_transaction_uid", event.getProviderTransactionUid().toString());
            record.put("transaction_type", event.getType().toString());
            record.put("transaction_status", event.getTransactionStatus().toString());

            CompletableFuture<SendResult<String, GenericRecord>> sendResultFuture = kafkaTemplate.send(kafkaTopic, record);

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
