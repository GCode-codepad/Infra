package org.example.editingservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.editingservice.dto.DocEditDelta;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EditingService {
    private final ReactiveStringRedisTemplate redisTemplate;
    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper;

    public EditingService(ReactiveStringRedisTemplate redisTemplate,
                          KafkaProducerService kafkaProducerService,
                          ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.kafkaProducerService = kafkaProducerService;
        this.objectMapper = objectMapper;
    }

    public Mono<Void> handleEditDelta(DocEditDelta delta) {
// 将delta转换为json字符串存入Redis
        return Mono.fromCallable(() -> {
            try {
                return objectMapper.writeValueAsString(delta);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize delta", e);
            }
        }).flatMap(json -> {
            String redisKey = "doc:" + delta.getDocId() + ":edits";
            String redisField = String.valueOf(delta.getTimestamp());

            // 使用HSET存储
            return redisTemplate.opsForHash().put(redisKey, redisField, json);
        }).doOnSuccess(success -> {
            // 成功存入Redis后发送至Kafka
            kafkaProducerService.sendDelta(delta);
        }).then();
    }
}
