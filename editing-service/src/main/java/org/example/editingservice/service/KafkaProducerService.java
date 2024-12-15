package org.example.editingservice.service;

import org.example.editingservice.dto.DocEditDelta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, DocEditDelta> kafkaTemplate;
    private final String topicName;

    public KafkaProducerService(
            KafkaTemplate<String, DocEditDelta> kafkaTemplate,
            @Value("${app.kafka.topic}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendDelta(DocEditDelta delta) {
        kafkaTemplate.send(topicName, delta.getDocId().toString(), delta);
    }
}
