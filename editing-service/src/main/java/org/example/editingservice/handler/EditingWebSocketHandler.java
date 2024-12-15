package org.example.editingservice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.editingservice.dto.DocEditDelta;
import org.example.editingservice.service.EditingService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class EditingWebSocketHandler implements WebSocketHandler {
    private final EditingService editingService;
    private final ObjectMapper objectMapper;

    // Store docId with WebSocket
    private final ConcurrentHashMap<Long, ConcurrentHashMap<String, WebSocketSession>> docSessions = new ConcurrentHashMap<>();

    public EditingWebSocketHandler(EditingService editingService, ObjectMapper objectMapper) {
        this.editingService = editingService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        // 从查询参数/子协议中获取docId和userId（这里假设通过URI参数传入，如 ws://.../ws/edit?docId=123&userId=456）
        Long docId = extractDocId(session);
        Long userId = extractUserId(session);

        // 注册会话
        docSessions.computeIfAbsent(docId, k -> new ConcurrentHashMap<>()).put(session.getId(), session);

        // 处理接收到的文本消息
        Mono<Void> input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(msg -> {
                    try {
                        DocEditDelta delta = objectMapper.readValue(msg, DocEditDelta.class);
                        // 如需可在此处校正timestamp或补充信息
                        delta.setUserId(userId);
                        delta.setDocId(docId);
                        return editingService.handleEditDelta(delta);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                }).then();

        // 输出流 - 本例中无需服务器主动发送消息给客户端，则使用空Flux
        Flux<WebSocketMessage> output = Flux.empty();

        return session.send(output)
                .and(input)
                .doFinally(signal -> {
                    // 会话结束时移除
                    docSessions.getOrDefault(docId, new ConcurrentHashMap<>()).remove(session.getId());
                });
    }

    private Long extractDocId(WebSocketSession session) {
        return session.getHandshakeInfo().getUri().getQuery()
                .chars()
                .filter(ch -> ch == '&') // just an example, parse properly
                .count() > 0
                ? Long.parseLong(session.getHandshakeInfo().getUri().getQuery().replaceAll(".*docId=(\\d+).*", "$1"))
                : 0L;
    }

    private Long extractUserId(WebSocketSession session) {
        return session.getHandshakeInfo().getUri().getQuery().contains("userId=")
                ? Long.parseLong(session.getHandshakeInfo().getUri().getQuery().replaceAll(".*userId=(\\d+).*", "$1"))
                : 0L;
    }
}
